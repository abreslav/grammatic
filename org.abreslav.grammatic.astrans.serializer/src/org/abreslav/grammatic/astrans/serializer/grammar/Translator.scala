package org.abreslav.grammatic.astrans.serializer.grammar

object Translator {
  
  def traverse(e : Expression, cont : Expression => Expression) : Expression = {
    e match {
      case Alternative(exps) => Alternative(exps.map(traverse(_, cont)))
      case Sequence(exps) => Sequence(exps.map(traverse(_, cont)))
      case Iteration(exp, x, y) => Iteration(traverse(exp, cont), x, y)
      case LexicalExpression(exp) => LexicalExpression(traverse(exp, cont))
      case AnnotatedExpression(exp, od, aa, ao) => AnnotatedExpression(traverse(exp, cont), od, aa, ao) 
      case _ => e
    }
  }
  
  def resolveNames(s : Symbol, symbols : Map[String, Symbol]) : Symbol = {
	s.productions = s.productions.map(resolveNames(_, symbols))
    s
  }

  def resolveNames(p : Production, symbols : Map[String, Symbol]) : Production = {
    Production(resolveNames(p.body, symbols))
  }
  
  def resolveNames(e : Expression, symbols : Map[String, Symbol]) : Expression = {
    def symbol(name : String) : Symbol = symbols.getOrElse(name, throw new IllegalArgumentException())
    e match {
      case SymbolReference_(scala.Symbol(name)) => SymbolReference(symbol(name))
      case _ => traverse(e, resolveNames(_, symbols))
    } 
  }
  
  
}
