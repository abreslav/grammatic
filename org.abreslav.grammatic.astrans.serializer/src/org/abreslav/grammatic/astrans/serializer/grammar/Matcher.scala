package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EObject

object Matcher {
  
  type Continuation = Context => Boolean

  
  def matchSymbol(symbol : Symbol, obj : AnyRef) : Boolean = matchExpression(
    AnnotatedSymbolReference(SymbolReference(symbol), AttributeReference(symbol.outputs(0))::Nil, Nil), 
    Context.emptyContext.setAttribute(symbol.outputs(0), obj), c => {
      println("Hooo: " + c); 
      true
    }
  )

  private def checkObjectType(obj : AnyRef, eClassifier : EClassifier) = eClassifier.isInstance(obj)
  
  private def declareObjects(
    context : Context,
    objectDeclarations : Seq[Attribute]) : Context = context
  
  private def applyAssignments(
    context : Context,
    afterAssignments : Seq[Assignment], 
    afterOptionals : Seq[OptionalAssignment]) : Option[Context] = {

      var result : Option[Context] = Some(context)
      afterAssignments.foldRight(result){
        (a, r) => r.flatMap{applyAssignment(_, a)}
      }

      // process optionals
    } 

  private def applyAssignment(context : Context, assignment : Assignment) : Option[Context] = {
    println("apply assignment: " + assignment)
    
    val change : Option[(Context, Attribute, AnyRef)] = assignment match {
      case AttributeAssignment(AttributeReference(host), attr) => context(host).map{(context, attr, _)}
      
      case AttributeAssignment(fr : FeatureReference, attr) => {
        resolveInContext(context, fr).map{case (context, value) => (context, attr, value)}
      }
    }
    change.flatMap{
      case (con, attr, value) => {
        if (checkObjectType(value, attr.eClass))
          Some(con.setAttribute(attr, value))
        else
          None
      }
    }
  }
  
  private def resolveInContext(context : Context, ref : Reference) : Option[(Context, AnyRef)] = ref match {
    case AttributeReference(a) => context(a).map{(context, _)}
    case FeatureReference(host, feature) => {
      val hostObj = context(host).map{_.asInstanceOf[EObject]}
      hostObj.flatMap{
        obj =>
		  if (!feature.getEContainingClass().isInstance(obj))
		    None
		  else if (feature.isMany)
		    hostObj.flatMap{context(_, feature)} 
		  else
		    hostObj.map{
		      o : EObject => (context, o.eGet(feature))
		    }
      }
    }
  } 
  
  private def assignValues[A](context : Context, attributes : List[Attribute], values : List[A])(
    resolve : (Context, A) => Option[(Context, AnyRef)]
  ) : Option[Context] = { 
    val optCont : Option[Context] = Some(context)
    attributes.zip(values).foldLeft(optCont) {
      (optCt, av) => {
        optCt.flatMap{
          val (attr, value) = av
          resolve(_, value).map{
            case (ctx, resVal) => ctx.setAttribute(attr, resVal)
          }
        }
      } 
    }
  }
  
  private def matchExpression(expression : Expression, context : Context, continuation : Continuation) : Boolean = {
    println("\n>>>>>Match expression")
    println(XMLSerializer(expression))
    val result = expression match {
      case StringExpression(str) => continuation(context.print(str))
      case Alternative(list) => matchAlternative(list, context, continuation)
      case Sequence(list) => matchSequence(list, context, continuation)
      case Iteration(e, l, u) => matchIteration(e, l, u, context, continuation)
      
      case AnnotatedSymbolReference(SymbolReference(Symbol(name, prods, ins, outs, token)), assignedTo, args) => {
        println(">>ASR: " + name)
        println(">>ins: " + ins)
        println(">>outs: " + outs)
        println(">>assignedTo: " + assignedTo)
        println(">>args: " + args)
        implicit def seqToList[A](seq : Seq[A]) = seq.toList
        if (assignedTo.size != outs.size || args.size != ins.size)
          throw new IllegalArgumentException()
        
        val outsC = assignValues(context, outs, assignedTo){
          (ctx, ref) => resolveInContext(ctx, ref)
        }
        
        println(">outsC")
        outsC.map{println(_)}
        
        val insC = outsC.flatMap{assignValues(_, ins, args){
            (ctx, attr) => ctx(attr).map{(ctx, _)}
          }
        }

        println(">insC")
        insC.map{println(_)}
        
        insC match {
          case Some(cont) =>
            if (token) {
              println("Printing a token")
              // what does this print() mean?! -- it's for a token
              val c = cont.print(cont(outs.head) match {case Some(o) => o.toString})
              println(c)
              val cres = continuation(c)
              println("Continuation result: " + cres)
              cres
            } else
              matchAlternative(prods.map(_.body), cont, 
                c => {
                  continuation(context.compose(c))
                })
          case None => false
        }
      }
      case AnnotatedExpression(expr, decls, after, afterOpt) => {
        println(">>AE: " + expr.getClass.getSimpleName)
        println(">>after: " + after)
        println(">>context: " + context)
        // check against decls -- ???
        
        val newContext = applyAssignments(context, after, afterOpt)
        
        println(">newContext: " + newContext)
        newContext match {
          case None => {println("here"); false}
          case Some(c) => matchExpression(expr, c, continuation)
        }
      }
      case _ => false
    }
    if (result)
      println("match succeded")
    else 
      println("match failed")
    result
  }
  
  private def matchSequence(sequence : List[Expression], context : Context, continuation : Continuation) : Boolean = {
    sequence match {
      case head :: tail => matchExpression(head, context, matchSequence(tail, _, continuation))  
      case Nil => {println("sequence is over"); continuation(context)} // think
    }
  }
  
  private def matchAlternative(options : List[Expression], context : Context, continuation : Continuation) : Boolean = {
    println("Enter alternative")
    for (option <- options) {
      if (matchExpression(option, context, continuation)) {
        println("Exit alternative : true")
        return true
      }
    }
    println("Exit alternative : false")
    return false
  }
  
  private def matchIteration(expression : Expression, low : Int, up : Int, context : Context, continuation : Continuation) : Boolean = {
    if (up != -1 && up < low) {
      throw new IllegalArgumentException
    }
    println("Matching iteration")
    val first = up match {
      case 1 => matchExpression(expression, context, continuation)
      case -1 => {
        // suspicious: what if lowerBounds is 1 and this does not match at all?
        matchExpression(expression, context, 
          cont => {
            val next = matchIteration(expression, low, up, cont, continuation)
            next || continuation(cont)
          }
        )
      }
    }
    first
//    if (first) {
//      return true
//    } else {
//      if (low == 0) {
//        if (continuation(context)) {
//          return true
//        }
//      }
//      for (i <- low to up) {
//        if (matchExpression(expression, context, matchIteration(expression, low, i, _, continuation)))
//          return true
//      }
//    }
//    false
  }
  
}
