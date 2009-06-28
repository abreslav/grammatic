package org.abreslav.grammatic.template.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class Substitution implements ISubstitution {

	private final Map<ETypeParameter, EGenericType> mySubstitutions = new HashMap<ETypeParameter, EGenericType>();
	private final Set<ETypeParameter> myUnsubstitutedParameters = new HashSet<ETypeParameter>();
	
	@Override
	public EGenericType getGenericType(EClassifier rawType) {
		EGenericType genericType = EcoreFactory.eINSTANCE.createEGenericType();
		genericType.setEClassifier(rawType);
		EList<ETypeParameter> typeParameters = rawType.getETypeParameters();
		for (ETypeParameter typeParameter : typeParameters) {
			EGenericType typeArgument = getSubstitutionFor(typeParameter, true);
			genericType.getETypeArguments().add((EGenericType) EcoreUtil.copy(typeArgument));
		}
		return genericType;
	}

	private EGenericType getSubstitutionFor(ETypeParameter typeParameter, boolean emitWildcards) {
		EGenericType substitution = mySubstitutions.get(typeParameter);
		if (substitution == null) {
			EGenericType genericType = EcoreFactory.eINSTANCE.createEGenericType();
			if (!emitWildcards) { 
				genericType.setETypeParameter(typeParameter);
			}
			return genericType;
		}
		
		// TODO: Probably this should be cached
		// TODO: Surely this should be transformed to a cycle 
		if (substitution.getETypeParameter() != null) {
			return getSubstitutionFor(substitution.getETypeParameter(), emitWildcards);
		}
		return substitution;
	}

	@Override
	public ISubstitutionReason getSubstitutionReason(
			ETypeParameter typeParameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ETypeParameter> getUnsubstitutedParameters() {
		return Collections.unmodifiableSet(myUnsubstitutedParameters);
	}

	@Override
	public void addTypeParameter(ETypeParameter typeParameter) {
		if (mySubstitutions.keySet().contains(typeParameter)) {
			return;
		}
		myUnsubstitutedParameters.add(typeParameter);
	}

	@Override
	public void addSubstitution(ETypeParameter typeParameter, EGenericType value) {
		if (mySubstitutions.keySet().contains(typeParameter)) {
			throw new IllegalArgumentException();
		}
		mySubstitutions.put(typeParameter, value);
		myUnsubstitutedParameters.remove(typeParameter);
	}

	@Override
	public EGenericType applyTo(EGenericType genericType) {
		// T
		if (genericType.getETypeParameter() != null) {
			return (EGenericType) EcoreUtil.copy(getSubstitutionFor(genericType.getETypeParameter(), false));
		}
		// EClassifier
		EGenericType result = EcoreFactory.eINSTANCE.createEGenericType();
		if (genericType.getEClassifier() != null) {
			result.setEClassifier(genericType.getEClassifier());
			EList<EGenericType> typeArguments = genericType.getETypeArguments();
			for (EGenericType typeArgument : typeArguments) {
				result.getETypeArguments().add(applyTo(typeArgument));
			}
			return result;
		}
		// ? extends ...
		if (genericType.getEUpperBound() != null) {
			EGenericType bound = applyTo(genericType.getEUpperBound());
			if (isWildcard(bound)) {
				processWildcardBound(genericType, result, bound);
			} else {
				result.setEUpperBound(bound);
			}
			return result;
		}
		// ? extends ...
		if (genericType.getELowerBound() != null) {
			EGenericType bound = applyTo(genericType.getELowerBound());
			if (isWildcard(bound)) {
				processWildcardBound(genericType, result, bound);
			} else {
				result.setELowerBound(bound);
			}
			return result;
		}
		// ?
		return result;
	}

	private void processWildcardBound(EGenericType genericType,
			EGenericType result, EGenericType bound) {
		if (!isWildcard(genericType)) {
			throw new IllegalArgumentException();
		}
		result.setEUpperBound(bound.getEUpperBound());
		result.setELowerBound(bound.getELowerBound());
	}

	private boolean isWildcard(EGenericType bound) {
		return bound.getEClassifier() == null && bound.getETypeParameter() == null;
	}

}
