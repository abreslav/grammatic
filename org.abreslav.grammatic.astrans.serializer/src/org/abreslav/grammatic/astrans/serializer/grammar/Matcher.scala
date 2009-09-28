package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject

class Matcher {
  
  type Continuation = Context => Boolean

  def checkObjectType(obj : EObject, eClass : EClass) = eClass.isInstance(obj)
  
  def declareObjects(
    context : Context,
    objectDeclarations : Seq[Attribute]) : Context = null
  
  def applyAssignments(
    context : Context,
    afterAssignments : Seq[Assignment], 
    afterOptionals : Seq[OptionalAssignment]) : Option[Context] = {
      // !!! Consider EAttributes!!!
      var result = context
      for (assignment <- afterAssignments.reverse) {
        val (attr, obj) = assignment match {
          case AttributeAssignment(AttributeReference(attr), value) => (attr, result(attr))
          case AttributeAssignment(FeatureReference(attr, feature), value) => {
            val obj = result(attr)
            if (!feature.getEContainingClass().isInstance(obj))
              return None
            if (feature.isMany) {
              val (value, newRes) = result(obj, feature)
              value match {
                case Some(o) => {
                  result = newRes
                  (attr, o)
                }
                case _ => return None
              } 
            } else
              (attr, obj.eGet(feature).asInstanceOf[EObject])
          }
        }
        if (checkObjectType(obj, attr.eClass))
          Some(context.setAttribute(attr, obj))
        else
          None
      }
      Some(result)
      // process optionals
    } 
  
  def resolveInContext(context : Context, ref : Reference) : EObject = null
  
  def matchExpression(expression : Expression, context : Context, continuation : Continuation) : Boolean = {
    expression match {
      case StringExpression(str) => continuation(context.print(str))
      case Alternative(list) => matchAlternative(list, context, continuation)
      case Sequence(list) => matchSequence(list, context, continuation)
      
      case AnnotatedSymbolReference(ref, assignedTo, args) => {
        implicit def seqToList[A](seq : Seq[A]) = seq.toList
        ref match {
          case SymbolReference(Symbol(_, prods, ins, outs, token)) => {
            if (assignedTo.size != outs.size || args.size != ins.size)
              throw new IllegalArgumentException()
            // this might break
            val assignedObjects = assignedTo.map(resolveInContext(context, _))
            // this mights break
            val outsC = outs.zip(assignedObjects)
              .foldLeft(context){
                (c, b) => c.setAttribute(b._1, b._2)
              }
            // this might break
            val insC = ins.zip(args.map(context(_)))
              .foldLeft(outsC){
                (c, b) => c.setAttribute(b._1, b._2)
              }
            
            if (token)
              continuation(insC.print(insC(outs.head).toString))
            else
              matchAlternative(prods.map(p => p.body), insC, continuation)
          }
        }
      }
      case AnnotatedExpression(expr, decls, after, afterOpt) => {
        // check against decls -- ???
        
        val newContext = applyAssignments(context, after, afterOpt)
        newContext match {
          case None => false
          case Some(c) => matchExpression(expr, c, continuation)
        }
      }
      case _ => false
    }
  }
  
  def matchSequence(sequence : List[Expression], context : Context, continuation : Continuation) : Boolean = {
    sequence match {
      case head :: tail => matchExpression(head, context, matchSequence(tail, _, continuation))  
      case Nil => false // think
    }
  }
  
  def matchAlternative(options : List[Expression], context : Context, continuation : Continuation) : Boolean = {
    for (option <- options)
      if (matchExpression(option, context, continuation))
        return true
    return false
  }
  
  def matchIteration(expression : Expression, low : Int, up : Int, context : Context, continuation : Continuation) : Boolean = {
    if (up != -1 && up < low) {
      return false
    }
    val first = up match {
      case 1 => matchExpression(expression, context, continuation)
      case -1 => {
        // suspicious: what if lowerBounds is 1 and this does not match at all?
        matchExpression(expression, context, matchIteration(expression, low, up, _, continuation));
        true
      }
    }
    if (first) {
      return true
    } else {
      if (low == 0) {
        if (continuation(context)) {
          return true
        }
      }
      for (i <- low to up) {
        if (matchExpression(expression, context, matchIteration(expression, low, i, _, continuation)))
          return true
      }
    }
    false
  }
  
}
