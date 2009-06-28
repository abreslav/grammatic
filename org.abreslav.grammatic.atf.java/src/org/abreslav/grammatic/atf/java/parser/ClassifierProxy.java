package org.abreslav.grammatic.atf.java.parser;

import org.abreslav.grammatic.emfutils.EObjectProxy;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.ESuperAdapter;
import org.eclipse.emf.ecore.impl.ESuperAdapter.Holder;

public class ClassifierProxy extends EObjectProxy<EClass> implements EClass, ESuperAdapter.Holder {

	@Override
	public int getClassifierID() {
		return pGetDelegate().getClassifierID();
	}

	@Override
	public Object getDefaultValue() {
		return pGetDelegate().getDefaultValue();
	}

	@Override
	public EPackage getEPackage() {
		return pGetDelegate().getEPackage();
	}

	@Override
	public EList<ETypeParameter> getETypeParameters() {
		return pGetDelegate().getETypeParameters();
	}

	@Override
	public Class<?> getInstanceClass() {
		return pGetDelegate().getInstanceClass();
	}

	@Override
	public String getInstanceClassName() {
		return pGetDelegate().getInstanceClassName();
	}

	@Override
	public String getInstanceTypeName() {
		return pGetDelegate().getInstanceTypeName();
	}

	@Override
	public boolean isInstance(Object object) {
		return pGetDelegate().isInstance(object);
	}

	@Override
	public void setInstanceClass(Class<?> value) {
		pGetDelegate().setInstanceClass(value);
	}

	@Override
	public void setInstanceClassName(String value) {
		pGetDelegate().setInstanceClassName(value);
	}

	@Override
	public void setInstanceTypeName(String value) {
		pGetDelegate().setInstanceTypeName(value);
	}

	@Override
	public String getName() {
		return pGetDelegate().getName();
	}

	@Override
	public void setName(String value) {
		pGetDelegate().setName(value);
	}

	@Override
	public EAnnotation getEAnnotation(String source) {
		return pGetDelegate().getEAnnotation(source);
	}

	@Override
	public EList<EAnnotation> getEAnnotations() {
		return pGetDelegate().getEAnnotations();
	}

	@Override
	public EList<EAttribute> getEAllAttributes() {
		return pGetDelegate().getEAllAttributes();
	}

	@Override
	public EList<EReference> getEAllContainments() {
		return pGetDelegate().getEAllContainments();
	}

	@Override
	public EList<EGenericType> getEAllGenericSuperTypes() {
		return pGetDelegate().getEAllGenericSuperTypes();
	}

	@Override
	public EList<EOperation> getEAllOperations() {
		return pGetDelegate().getEAllOperations();
	}

	@Override
	public EList<EReference> getEAllReferences() {
		return pGetDelegate().getEAllReferences();
	}

	@Override
	public EList<EStructuralFeature> getEAllStructuralFeatures() {
		return pGetDelegate().getEAllStructuralFeatures();
	}

	@Override
	public EList<EClass> getEAllSuperTypes() {
		return pGetDelegate().getEAllSuperTypes();
	}

	@Override
	public EList<EAttribute> getEAttributes() {
		return pGetDelegate().getEAttributes();
	}

	@Override
	public EList<EGenericType> getEGenericSuperTypes() {
		return pGetDelegate().getEGenericSuperTypes();
	}

	@Override
	public EAttribute getEIDAttribute() {
		return pGetDelegate().getEIDAttribute();
	}

	@Override
	public EList<EOperation> getEOperations() {
		return pGetDelegate().getEOperations();
	}

	@Override
	public EList<EReference> getEReferences() {
		return pGetDelegate().getEReferences();
	}

	@Override
	public EStructuralFeature getEStructuralFeature(int featureID) {
		return pGetDelegate().getEStructuralFeature(featureID);
	}

	@Override
	public EStructuralFeature getEStructuralFeature(String featureName) {
		return pGetDelegate().getEStructuralFeature(featureName);
	}

	@Override
	public EList<EStructuralFeature> getEStructuralFeatures() {
		return pGetDelegate().getEStructuralFeatures();
	}

	@Override
	public EList<EClass> getESuperTypes() {
		return pGetDelegate().getESuperTypes();
	}

	@Override
	public int getFeatureCount() {
		return pGetDelegate().getFeatureCount();
	}

	@Override
	public int getFeatureID(EStructuralFeature feature) {
		return pGetDelegate().getFeatureID(feature);
	}

	@Override
	public boolean isAbstract() {
		return pGetDelegate().isAbstract();
	}

	@Override
	public boolean isInterface() {
		return pGetDelegate().isInterface();
	}

	@Override
	public boolean isSuperTypeOf(EClass someClass) {
		return pGetDelegate().isSuperTypeOf(someClass);
	}

	@Override
	public void setAbstract(boolean value) {
		pGetDelegate().setAbstract(value);
	}

	@Override
	public void setInterface(boolean value) {
		pGetDelegate().setInterface(value);
	}

	@Override
	public ESuperAdapter getESuperAdapter() {
		Holder holder = (ESuperAdapter.Holder) pGetDelegate();
		return holder == null ? new ESuperAdapter() : holder.getESuperAdapter();
	}

	@Override
	public boolean isFrozen() {
		Holder holder = (ESuperAdapter.Holder) pGetDelegate();
		return holder == null ? false : holder.isFrozen();
	}

}
