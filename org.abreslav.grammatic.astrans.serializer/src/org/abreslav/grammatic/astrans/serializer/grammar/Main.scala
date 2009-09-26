package org.abreslav.grammatic.astrans.serializer.grammar

object Main {
  def main(args : Array[String]) : Unit = {
    import Context._
    val context = emptyContext
    println(context.print("S", "1").print("2"))
    val eFactory = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE
    val ePackage = org.eclipse.emf.ecore.EcorePackage.eINSTANCE
    val o = eFactory.createEClass()
    o.getEStructuralFeatures().add(eFactory.createEReference())
    val eReferences = o.eClass().getEStructuralFeature("eReferences")
    val (Some(r), context1) = context(o, eReferences)
    println(r)
    println(context1(o, eReferences))
  }
}
