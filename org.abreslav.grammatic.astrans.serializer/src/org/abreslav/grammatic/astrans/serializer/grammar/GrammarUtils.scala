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
        right match {
          case ExpressionBuilder(Alternative(expressions)) => Alternative(expressions ::: List(right.expression))
          case ExpressionBuilder(expr) => Alternative(expr :: right.expression :: Nil)
        })

    def - (right : ExpressionBuilder) =
      new ExpressionBuilder(
        right match {
    	  case ExpressionBuilder(Sequence(expressions)) => Sequence(expressions ::: List(right.expression))
    	  case ExpressionBuilder(expr) => Sequence(expr :: right.expression :: Nil)
        })

    def apply (afterAssignments : Assignment*)(afterOptionals : OptionalAssignment*) : ExpressionBuilder = 
      ExpressionBuilder(AnnotatedExpression(expression, Nil, afterAssignments, afterOptionals))
      
//    def apply (afterAssignments : Assignment*) : ExpressionBuilder = 
//  	  ExpressionBuilder(AnnotatedExpression(expression, Nil, afterAssignments, Nil))
    	  
  }
  
  case class SymbolReferenceBuilder(val reference : AbstractSymbolReference) extends ExpressionBuilder(reference) {
    def apply(attributes : Attribute*) = ExpressionBuilder(AnnotatedSymbolReference(reference, null, attributes)) 
  } 
  
  def empty() = ExpressionBuilder(Empty())
  implicit def symbolToRef(s : scala.Symbol) = new SymbolReferenceBuilder(SymbolReference_(s))
  implicit def stringToStringExpr(s : String) : ExpressionBuilder = new ExpressionBuilder(StringExpression(s))
  implicit def exprBuilderToExpr(builder : ExpressionBuilder) : Expression = builder.expression
  
  class CallBuilder(val attribute : Attribute) {
    def === (expBuilder : ExpressionBuilder) = expBuilder match {
      case ExpressionBuilder(AnnotatedSymbolReference(ref, null, attrs)) => ExpressionBuilder(AnnotatedSymbolReference(ref, AttributeReference(attribute), attrs))
      case SymbolReferenceBuilder(ref) => ExpressionBuilder(AnnotatedSymbolReference(ref, AttributeReference(attribute), Nil))
      case _ => throw new IllegalArgumentException()
    }
  }
  
  implicit def attributeToCallBuilder(attribute : Attribute) = new CallBuilder(attribute)
  
  class GrammarBuilder {
    private var productions : Map[scala.Symbol, List[Expression]] = Map()
    
    def symbolList : List[Symbol] = {
      val dirtySymbols = for ((scala.Symbol(name), prods) <- productions)
        yield Symbol(name, prods.map(Production(_)))
      var symbolMap : Map[String, Symbol] = Map() 
      for (symbol <- dirtySymbols; name = symbol.name)
        symbolMap += name -> symbol
      dirtySymbols.map(Translator.resolveNames(_, symbolMap)).toList 
    }
    
	class SymbolBuilder(val name : scala.Symbol, val grammarBuilder : GrammarBuilder) {
	    
	  def ::=(expressions : List[Expression]) {
	    for (e <- expressions)
	      this ::= e
	  } 
	    
	  def ::=(expression : Expression) { 
	    productions += name -> (expression :: productions(name)) 
	  }
    }
  
    implicit def symbolToSymbolBuilder(symbol : scala.Symbol) = new SymbolBuilder(symbol, this)
    
  }
  
  def grammar (name : String) (builder : => GrammarBuilder) = Grammar(builder.symbolList) 
}

object Experiment {
  import org.eclipse.emf.ecore.EClass 
  import org.eclipse.emf.ecore.EStructuralFeature
  import org.eclipse.emf.ecore.EEnumLiteral
  
  import GrammarUtils._
  import ReferenceBuilder._
  {
    grammar ("Test") {
      val v = new GrammarBuilder()
      import v._
      
      val Sum : EClass = null
      val children : EStructuralFeature = null
      val sum_result = Attribute('result, Sum)
      val sum_object = Attribute('this, Sum)
      val mult_result = Attribute('this, Sum)
      'sum ::= ((mult_result === 'mult){sum_object->children += mult_result}() 
                - ("+" - 'mult{sum_object->children += mult_result}()).*())
                {sum_result := sum_object}()
      'mult ::= 'factor | 'factor - "*" - 'mult
      'factor ::= "(" - 'sum - ")" | 'NUM
      'NUM ::= "1" - empty;
      v
    }
  }
}
