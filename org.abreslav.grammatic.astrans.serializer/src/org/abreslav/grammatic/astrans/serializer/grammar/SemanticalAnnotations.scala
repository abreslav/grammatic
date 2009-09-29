package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EEnumLiteral

//class EClass(val name : scala.Symbol, val features : EStructuralFeature[_, _]*)
//class EStructuralFeature[C, T](val get : C => T, val set : (C, T) => Unit)
//class EEnumLiteral(val name : scala.Symbol)

object ReferenceBuilder {
  implicit def attributeToRef(attribute : Attribute) = AttributeReference(attribute)
}
                                                              
case class Attribute(name : scala.Symbol, eClass : EClassifier) {
  def -> (feature : EStructuralFeature) = FeatureReference(this, feature)
  def ifNotNull[A, B](a : A, f : A => B) = if (a == null) null else f(a)
  
  override def toString = name.name + " : " + ifNotNull(eClass, (_ : EClassifier).getName) 
}

abstract class Reference {
  def := (literal : EEnumLiteral) = EnumLiteralAssignment(this, literal)
  def := (attribute : Attribute) = AttributeAssignment(this, attribute)
}

case class AttributeReference(attribute : Attribute) extends Reference

case class FeatureReference(attribute : Attribute, feature : EStructuralFeature) extends Reference {
  def ?:= (attribute : Attribute) = OptionalAssignment(this, attribute)
  def += (attribute : Attribute) = AttributeAssignment(this, attribute)
}

abstract class Assignment
case class EnumLiteralAssignment(reference : Reference, literal : EEnumLiteral) extends Assignment
case class AttributeAssignment(reference : Reference, value : Attribute) extends Assignment
case class OptionalAssignment(featureReference : FeatureReference, value : Attribute)