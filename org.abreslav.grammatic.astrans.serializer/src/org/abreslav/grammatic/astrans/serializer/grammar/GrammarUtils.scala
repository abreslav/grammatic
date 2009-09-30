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
  implicit def symbolToRef(s : scala.Symbol) = new GrammarUtils.SymbolReferenceBuilder(SymbolReference_(s))
  
}

object Experiment {
  import org.eclipse.emf.ecore.EClass 
  import org.eclipse.emf.ecore.EStructuralFeature
  import org.eclipse.emf.ecore.EEnumLiteral
  import org.eclipse.emf.ecore.EcoreFactory
  import org.eclipse.emf.ecore.EcorePackage
  

  def EClass(s : String) = {
//    val result = EcoreFactory.eINSTANCE.createEClass()
//    result.setName(s)
//    result
    val result = arith.ArithPackage.eINSTANCE.getEClassifier(s).asInstanceOf[EClass]
    assert(result != null)
    result
  }
  
  def EReference(c : EClass, s : String) = {
    c.getEStructuralFeature(s)
  }
  
  def EAttribute(c : EClass, s : String) = {
//	  val result = EcoreFactory.eINSTANCE.createEAttribute()
//	  result.setName(s)
//	  result 
    c.getEStructuralFeature(s)
  }
  
  import GrammarUtils._
  import ReferenceBuilder._
  val theGrammar = grammar ("Test") {
      new GrammarBuilder() {
    	  val Expr = EClass("Expr")
	      val Sum = EClass("Sum")
	      val children = EReference(Sum, "expressions")
	      val Mult = EClass("Mult")
	      val left = EReference(Mult, "left")
	      val right = EReference(Mult, "right")
	      val Num = EClass("Num")
	      val value = EReference(Num, "value")
	      
	      val s_result = Attribute('s_result, Expr)
	      val m_result = Attribute('m_result, Expr)
	      val f_result = Attribute('f_result, Expr)
	      val tmp = Attribute('tmp, Expr)
	      
          val s_this = Attribute('s_this, Sum)
          val m_this = Attribute('m_this, Mult)
          val n_this = Attribute('n_this, Num)
          val EString = EcorePackage.eINSTANCE.getEString()
          val v = Attribute('value, EString)
          
          val str = Attribute('str, EString)
       
	      'sum -> s_result ::= (s_this -: (
	        (tmp==='mult){
	          s_this->children += tmp
	        } - 
	          ("+" - (tmp==='mult){
	            s_this->children += tmp
	          }).*)){
	    	  s_result := s_this
	        } | s_result==='mult 
       
          'mult -> m_result ::= m_this -: (
	        (tmp==='factor){
	          m_this->left := tmp
	        }  - "*" - (tmp==='mult{
	          m_this->right := tmp
	        })){
	          m_result := m_this
	        } | m_result==='factor
       
	      'factor -> f_result ::=  
             n_this -: ((v==='NUM{
               n_this->value := v
             }){
               f_result := n_this
             }) | "(" - (f_result==='sum) - ")"
       
	      token('NUM) -> str ::= "1";
      }
    }
}
