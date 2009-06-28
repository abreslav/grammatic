package org.abreslav.grammatic.template.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.util.EcoreValidator;

public class GenericBasedEObjectValidator {

	public static final int DOES_NOT_BOUND = 1;
	
	private final GenericBasedTypeResolver myTypeResolver;
	
	public GenericBasedEObjectValidator(GenericBasedTypeResolver typeResolver) {
		myTypeResolver = typeResolver;
	}

	/**
	 * Validates all generic-related type conformance constraints on a given {@code eObject}
	 * @param eObject an object being validated
	 * @return a result of validation
	 */
	public Diagnostic validate(EObject eObject) {
		List<Diagnostic> diagnostics = new ArrayList<Diagnostic>();
		myTypeResolver.resolveType(eObject);
		validateEObject(eObject, diagnostics);
		diagnostics.addAll(myTypeResolver.getErrors());
		
		return new BasicDiagnostic(this.getClass().getCanonicalName(), 0, diagnostics, "", new Object[] {});
	}
	
	private void validateEObject(EObject object, List<Diagnostic> diagnostics) {
		EList<EReference> references = object.eClass().getEAllReferences();
		for (EReference reference : references) {
			validateReferenceValue(reference, object, diagnostics);
		}
	}

	/**
	 * Obtains {@code reference}'s actual type (by using type parameter {@code substitution}) and checks
	 * every instance referenced by the {@code reference} of a particular {@code object} for conformance to
	 * this actual type. 
	 * @param reference a reference to be validated
	 * @param object an object that holds actual data
	 * @param diagnostics a place to report errors to
	 */
	private void validateReferenceValue(EReference reference,
			EObject object, List<Diagnostic> diagnostics) {
		EGenericType actualReferenceType = myTypeResolver.resolveFeatureType(object, reference.getEGenericType());
		Object value = object.eGet(reference);
		if (reference.isMany()) {
			@SuppressWarnings("unchecked")
			Collection<EObject> eObjects = (Collection<EObject>) value;
			for (EObject subValue : eObjects) {
				checkEObject(subValue, actualReferenceType, diagnostics, reference.isContainment());
			}
		} else {
			checkEObject((EObject) value, actualReferenceType, diagnostics, reference.isContainment());
		}
	}

	private void checkEObject(EObject subValue, EGenericType referenceType,
			List<Diagnostic> diagnostics, boolean doValidate) {
		if (subValue == null) {
			return;
		}
		EGenericType objectType = myTypeResolver.resolveType(subValue);
		if (doValidate) {
			validateEObject(subValue, diagnostics);
		}
		boolean bounded = EcoreValidator.isBounded(objectType, referenceType, Collections.<ETypeParameter, EGenericType>emptyMap());
		if (!bounded) {
			reportError(diagnostics, DOES_NOT_BOUND, "Type " + GenericTypeRenderer.render(referenceType) + " does not bound " + GenericTypeRenderer.render(objectType), referenceType, objectType);
		}
	}
	
	private void reportError(List<Diagnostic> diagnostics, int code, String message, Object... data) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, this.getClass().getCanonicalName(), DOES_NOT_BOUND, message, data));
	}

}
