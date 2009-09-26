package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EEnumLiteral

case class Attribute(name : String, eClass : EClass)

abstract class Reference
case class AttributeReference(attribute : Attribute) extends Reference
case class FeatureReference(attribute : Attribute, feature : EStructuralFeature) extends Reference

abstract class Assignment
case class EnumLiteralAssignment(reference : Reference, literal : EEnumLiteral) extends Assignment
case class OptionalAssignment(featureReference : FeatureReference, value : Attribute)