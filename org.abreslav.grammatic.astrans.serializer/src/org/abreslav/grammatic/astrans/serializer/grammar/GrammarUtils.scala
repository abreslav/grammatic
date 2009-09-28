package org.abreslav.grammatic.astrans.serializer.grammar

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.Queue

object GrammarUtils {
  
  
  case class ExpressionBuilder(val expression : Expression) {

    def * () =
      new ExpressionBuilder(Iteration(expression, 0, -1))

    def + () =
      new ExpressionBuilder(Iteration(expression, 1, -1))
    
    def ? () =
      new ExpressionBuilder(Iteration(expression, 0, 1))
    
    def | (right : ExpressionBuilder) =
      new ExpressionBuilder(
        (expression, right.expression) match {
          case (Alternative(a), Alternative(b)) => Alternative(a ::: b)
          case (Alternative(alt), e) => Alternative(alt ::: List(e))
          case (e, Alternative(alt)) => Alternative(e :: alt)
          case (a, b) => Alternative(a :: b :: Nil)
        })

    def - (right : ExpressionBuilder) =
      new ExpressionBuilder(
        (expression, right.expression) match {
          case (Sequence(a), Sequence(b)) => Sequence(a ::: b)
          case (Sequence(seq), e) => Sequence(seq ::: List(e))
          case (e, Sequence(seq)) => Sequence(e :: seq)
          case (a, b) => Sequence(a :: b :: Nil)
        })

    def apply (afterAssignments : Seq[Assignment], afterOptionals : OptionalAssignment*) = expression match { 
      case AnnotatedExpression(e, oD, aA, aO) 
        => ExpressionBuilder(AnnotatedExpression(e, oD, aA ::: afterAssignments.toList, aO ::: afterOptionals.toList))
      case e => ExpressionBuilder(AnnotatedExpression(e, Nil, afterAssignments.toList, afterOptionals.toList))
    }
      
    def -: (attribute : Attribute) = expression match {
      case AnnotatedExpression(e, oD, aA, aO) => ExpressionBuilder(AnnotatedExpression(e, attribute :: oD, aA, aO))
      case e => ExpressionBuilder(AnnotatedExpression(e, attribute :: Nil, Nil, Nil)) 
    }
//    def apply (afterAssignments : Assignment*) : ExpressionBuilder = 
//  	  ExpressionBuilder(AnnotatedExpression(expression, Nil, afterAssignments, Nil))
    	  
  }
  
  case class SymbolReferenceBuilder(val reference : AbstractSymbolReference) extends ExpressionBuilder(reference) {
    def apply(attributes : Attribute*) = ExpressionBuilder(AnnotatedSymbolReference(reference, null, attributes)) 
  } 
  
  def empty() = ExpressionBuilder(Empty())
  implicit def stringToStringExpr(s : String) : ExpressionBuilder = new ExpressionBuilder(StringExpression(s))
  implicit def exprBuilderToExpr(builder : ExpressionBuilder) : Expression = builder.expression
  implicit def assignmentToSeq(assignment : Assignment) = assignment :: Nil
  
  class CallBuilder(val attribute : Attribute) {
    def === (expBuilder : ExpressionBuilder) = expBuilder match {
      case ExpressionBuilder(AnnotatedSymbolReference(ref, null, attrs)) 
        => ExpressionBuilder(AnnotatedSymbolReference(ref, AttributeReference(attribute) :: Nil, attrs))
      case SymbolReferenceBuilder(ref) 
        => ExpressionBuilder(AnnotatedSymbolReference(ref, AttributeReference(attribute) :: Nil, Nil))
      case _ => throw new IllegalArgumentException()
    }
  }
  
  implicit def attributeToCallBuilder(attribute : Attribute) = new CallBuilder(attribute)
  
  class GrammarBuilder {
    private var productions : Map[scala.Symbol, (List[Expression], List[Attribute], List[Attribute], Boolean)] = Map()
    
    def symbolList : List[Symbol] = {
      val dirtySymbols = for ((scala.Symbol(name), (prods, ins, outs, token)) <- productions)
        yield Symbol(name, prods.reverse.map(Production(_)), ins, outs, token)
      var symbolMap : Map[String, Symbol] = Map() 
      for (symbol <- dirtySymbols; name = symbol.name)
        symbolMap += name -> symbol
      dirtySymbols.map(Translator.resolveNames(_, symbolMap)).toList 
    }
    
	class SymbolBuilder(val name : scala.Symbol, token : Boolean) {
	  
	  productions += name -> (Nil, Nil, Nil, token) 
	  
	  def ::=(expressions : List[ExpressionBuilder]) {
	    for (e <- expressions)
	      this ::= e
	  } 
	    
	  def ::=(expression : ExpressionBuilder) { 
	    val (exprs, ins, outs, token) = productions(name)
	    productions += name -> (expression.expression :: exprs, ins, outs, token)
	  }
   
      def ::(attributes : Attribute*) : SymbolBuilder = {
	    val (exprs, ins, outs, token) = productions(name)
	    productions += name -> (exprs, attributes.toList, outs, token)
        this
      }

      def ->(attributes : Attribute*) : SymbolBuilder = {
	    val (exprs, ins, outs, token) = productions(name)
	    productions += name -> (exprs, ins, attributes.toList, token) 
        this
      }
   }
  
    implicit def symbolToSymbolBuilder(symbol : scala.Symbol) = new SymbolBuilder(symbol, false)
    
    def token (symbol : scala.Symbol) = new SymbolBuilder(symbol, true)
  }
  
  def grammar (name : String) (builder : => GrammarBuilder) = Grammar(builder.symbolList) 
  implicit def symbolToRef(s : scala.Symbol) = new SymbolReferenceBuilder(SymbolReference_(s))
  
}

object Experiment {
  import org.eclipse.emf.ecore.EClass 
  import org.eclipse.emf.ecore.EStructuralFeature
  import org.eclipse.emf.ecore.EEnumLiteral
  import org.eclipse.emf.ecore.EcoreFactory
  
  def EClass(s : String) = {
    val result = EcoreFactory.eINSTANCE.createEClass()
    result.setName(s)
    result
  }
  
  def EReference(s : String) = {
    val result = EcoreFactory.eINSTANCE.createEReference()
    result.setName(s)
    result
  }
  
  def EAttribute(s : String) = {
	  val result = EcoreFactory.eINSTANCE.createEAttribute()
	  result.setName(s)
	  result
  }
  
  import GrammarUtils._
  import ReferenceBuilder._
  val theGrammar = grammar ("Test") {
      new GrammarBuilder() {
	      val Sum : EClass = EClass("Sum")
	      val children : EStructuralFeature = EReference("children")
	      val Mult : EClass = EClass("Mult")
	      val left : EStructuralFeature = EReference("left")
	      val right : EStructuralFeature = EReference("right")
	      val Num : EClass = EClass("Num")
	      val value : EStructuralFeature = EReference("value")
	      
	      val sum_result = Attribute('result, Sum)
	      val sum_object = Attribute('sum_this, Sum)
	      val mult_result = Attribute('mult_results, Sum)
	      'sum -> (sum_result) ::= (sum_object -: (
	        (mult_result==='mult){
	          sum_object->children+=mult_result
	        } - 
	          ("+" - (mult_result==='mult){
	            sum_object->children+=mult_result
	          }).*)){
	    	  sum_result := sum_object
	        }
	      'mult -> mult_result ::= ('factor - "*" - 'mult) | 'factor
	      'factor -> () ::= "(" - 'sum - ")" | 'NUM
	      token('NUM) ::= "1";
      }
    }
}
