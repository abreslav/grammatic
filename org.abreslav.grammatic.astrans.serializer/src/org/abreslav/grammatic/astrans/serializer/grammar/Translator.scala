package org.abreslav.grammatic.astrans.serializer.grammar

object Translator {

  def out[A](s : String, a : A) : A = {
//    println(s); 
    a
  }
  
  def traverse(e : Expression, cont : Expression => Expression) : Expression = {
    e match {
      case Alternative(exps) => out("alt", Alternative(exps.map(traverse(_, cont))))
      case Sequence(exps) => out("seq", Sequence(exps.map(traverse(_, cont))))
      case Iteration(exp, x, y) => out("it", Iteration(traverse(exp, cont), x, y))
      case LexicalExpression(exp) => out("lex", LexicalExpression(traverse(exp, cont)))
      case AnnotatedExpression(exp, od, aa, ao) => AnnotatedExpression(traverse(exp, cont), od, aa, ao) 
      case AnnotatedSymbolReference(ref, aT, args) => AnnotatedSymbolReference(cont(ref).asInstanceOf[SymbolReference], aT, args) 
      case _ => cont(e)
    }
  }
  
  def resolveNames(s : Symbol, symbols : Map[String, Symbol]) : Symbol = {
	s.productions = s.productions.map(resolveNames(_, symbols))
    out(s.name, s)
  }

  def resolveNames(p : Production, symbols : Map[String, Symbol]) : Production = {
    out("p", Production(traverse(p.body, resolveNames(_, symbols))))
  }
  
  def resolveNames(e : Expression, symbols : Map[String, Symbol]) : Expression = {
    def symbol(name : String) : Symbol = symbols.getOrElse(name, throw new IllegalArgumentException())
    e match {
      case SymbolReference_(scala.Symbol(name)) => out(name, SymbolReference(symbol(name)))
      case _ => e
    } 
  }
  
  
}
