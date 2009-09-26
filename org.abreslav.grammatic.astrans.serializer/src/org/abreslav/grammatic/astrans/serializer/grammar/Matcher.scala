package org.abreslav.grammatic.astrans.serializer.grammar

class Matcher {
  
  type Continuation = Context => Boolean

  def empty(context : Context) = true
  
  def matchExpression(expression : Expression, context : Context, continuation : Continuation) : Boolean = {
    expression match {
      case Alternative(list) => matchAlternative(list, context, continuation)
      case Sequence(list) => matchSequence(list, context, continuation)
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
