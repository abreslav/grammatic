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

  }
  
  def empty() = ExpressionBuilder(Empty())
  implicit def symbolToRef(s : scala.Symbol) : ExpressionBuilder = new ExpressionBuilder(SymbolReference_(s))
  implicit def stringToStringExpr(s : String) : ExpressionBuilder = new ExpressionBuilder(StringExpression(s))
  implicit def exprBuilderToExpr(builder : ExpressionBuilder) : Expression = builder.expression
  
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

class Experiment {
  import GrammarUtils._
  {
    grammar ("Test") {
      val v = new GrammarBuilder()
      import v._
      'sum ::= 'mult - ("+" - 'mult).* 
      'mult ::= 'factor | 'factor - "*" - 'mult
      'factor ::= "(" - 'sum - ")" | 'NUM
      'NUM ::= "1" - empty;
      v
    }
  }
}
