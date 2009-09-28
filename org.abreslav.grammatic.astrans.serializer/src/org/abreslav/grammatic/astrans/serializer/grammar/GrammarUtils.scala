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
      case ExpressionBuilder(AnnotatedExpression(ref : AbstractSymbolReference, Nil, aA, oA)) 
        => ExpressionBuilder(AnnotatedExpression(AnnotatedSymbolReference(ref, AttributeReference(attribute) :: Nil, Nil), Nil, aA, oA))
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
//  import org.eclipse.emf.ecore.EClass 
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
    	  val Expr = EClass("Expr")
	      val Sum = EClass("Sum")
	      val children = EReference("children")
	      val Mult = EClass("Mult")
	      val left = EReference("left")
	      val right = EReference("right")
	      val Num = EClass("Num")
	      val value = EReference("value")
	      
	      val result = Attribute('result, Expr)
	      val tmp = Attribute('mult_results, Expr)
	      
          val s_this = Attribute('this, Sum)
          val m_this = Attribute('this, Mult)
          val n_this = Attribute('this, Num)
          val v = Attribute('value, null)
       
	      'sum -> result ::= (s_this -: (
	        (tmp==='mult){
	          s_this->children += tmp
	        } - 
	          ("+" - (tmp==='mult){
	            s_this->children += tmp
	          }).*)){
	    	  result := s_this
	        }
       
	      'mult -> result ::= m_this -: (
	        (tmp==='factor){
	          m_this->left := tmp
	        }  - "*" - (tmp==='mult{
	          m_this->right := tmp
	        })){
	          result := m_this
	        } | result==='factor
       
	      'factor -> result ::= "(" - (result==='sum) - ")" | 
             n_this -: ((v==='NUM{
               n_this->value := v
             }){
               result := n_this
             })
       
	      token('NUM) ::= "1";
      }
    }
}
