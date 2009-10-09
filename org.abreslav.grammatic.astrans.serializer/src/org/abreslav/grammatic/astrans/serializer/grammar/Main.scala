package org.abreslav.grammatic.astrans.serializer.grammar

object Main {
  def main(args : Array[String]) : Unit = {
    val grammar = Experiment.theGrammar
    scala.xml.XML.saveFull("debug.xml", XMLSerializer(grammar), "UTF-8", true, null)

    // 1 * (2 + 3)
//    val expr = Mult(Num("1"), Sum(Num("2"), Num("3")))
    val expr = Mult(Mult(Num("1"), Sum(Num("4"), Num("111"))), Sum(Num("2"), Num("3"), Mult(Num("5"), Num("8"))))
//    val expr = Sum(Num("2"), Num("3"), Num("4"), Num("5"), Num("13"), Num("14"), Num("15"))
//    val expr = Sum(Num("2"), Num("3"), Num("4"), Num("5"), Num("6"), Num("7"))
//    val expr = Sum(Num("1"), Num("2"), Num("3"))
//    val expr = Num("1")
//    val expr = Mult(Num("1"), Num("2"))
    
    println(Matcher.matchSymbol(grammar.symbols(0), expr)) 
    
    if (2 > 3) {
      import Context._
      val context = emptyContext
      println(context.print("S").print("1").print("2"))
      val eFactory = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
      val ePackage = org.eclipse.emf.ecore.EcorePackage.eINSTANCE
      val o = eFactory.createEClass()
      o.getEStructuralFeatures().add(eFactory.createEReference())
      val eReferences = o.eClass().getEStructuralFeature("eReferences")
      val Some((context1, r)) = context(o, eReferences)
      println(r)
      println(context1(o, eReferences))
    }
  }

  def Num(s : String) = {
    val one = arith.ArithFactory.eINSTANCE.createNum()
    one.setValue(s)
    one
  }

  def Sum(e : arith.Expr*) = {
    val one = arith.ArithFactory.eINSTANCE.createSum()
    one.getExpressions.addAll(
      e.foldLeft(new java.util.ArrayList[arith.Expr]){
        (l, e) => {l.add(e); l}
      }
    )
    one
  }
  
  def Mult(a : arith.Expr, b : arith.Expr) = {
    val mult = arith.ArithFactory.eINSTANCE.createMult()
    mult setLeft a
    mult setRight b
    mult
  }
    
}
