package org.abreslav.grammatic.astrans.serializer.grammar

object Sme {

  object :: {
    def unapply[E](list : java.util.List[E]) : Option[(E, java.util.List[E])] = {
      if (list.isEmpty)
        None
      else
        Some((list.get(0), list.subList(1, list.size)))
    }
  }
  
  object Empty {
	  def unapply[E](list : java.util.List[E]) : Boolean = list.isEmpty
  }
  
  def on (s : => String) {
    println(s)
  }
  
  def main(args : Array[String]) : Unit = {

    val l = new java.util.ArrayList[Int]();
    l.add(1)
    l.add(2)
    l match {
      case 1 :: _ :: Empty() => print("!!!")
      case Empty() => print("fail")
    }
    on {
      val x = new X("x")
      val y = new Y(x)
      x.toString + y.toString
    }
  }
}


class X(override val toString : String)
class Y(val x : X)