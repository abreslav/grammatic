package org.abreslav.grammatic.astrans.serializer.grammar

final case class Grammar(symbols : List[Symbol])

final case class Symbol(name : String, var productions : List[Production])

final case class Production(body : Expression)

sealed abstract class Expression
abstract class AbstractSymbolReference extends Expression
case class SymbolReference(symbol : Symbol) extends AbstractSymbolReference
private case class SymbolReference_(var symbol : scala.Symbol) extends AbstractSymbolReference
case class Empty() extends Expression
case class Iteration(
    val expression : Expression,
    val lowerBound : Int,
    val upperBound : Int	
  ) extends Expression
case class Alternative(expressions : List[Expression]) extends Expression
case class Sequence(expressions : List[Expression]) extends Expression

abstract class LexicalDefinition extends Expression
case class CharacterRange(lowerBound : Int, upperBound : Int) extends LexicalDefinition
case class StringExpression(string : String) extends LexicalDefinition 
case class LexicalExpression(expression : Expression) extends LexicalDefinition

case class AnnotatedExpression(
    expression : Expression, 
    objectDeclarations : List[Attribute],
    afterAssignments : List[Assignment],
    afterOptionals : List[OptionalAssignment]
  ) extends Expression

case class AnnotatedSymbolReference(
    symbolReference : AbstractSymbolReference, 
    assignedTo : Reference, 
    arguments : Seq[Attribute]) extends Expression