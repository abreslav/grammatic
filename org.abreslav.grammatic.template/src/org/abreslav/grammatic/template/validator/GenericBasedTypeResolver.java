package org.abreslav.grammatic.template.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreValidator;

public class GenericBasedTypeResolver {

	private static final EGenericType CYCLE = EcoreFactory.eINSTANCE.createEGenericType();
	private final Map<ETypeParameter, IInferer> myInferers = new HashMap<ETypeParameter, IInferer>();
	private final Collection<Diagnostic> myErrors = new ArrayList<Diagnostic>();
	/**
	 * A cache for {@link #processEObject(EObject, Diagnostic)}
	 */
	private final Map<EObject, EGenericType> myActualTypes = new HashMap<EObject, EGenericType>();
	private final Map<EObject, ISubstitution> mySubstitutions = new HashMap<EObject, ISubstitution>();
	
	public EGenericType resolveType(EObject object) {
		return processEObject(object);
	}
	
	public EGenericType resolveFeatureType(EObject object, EGenericType type) {
		ISubstitution substitution = mySubstitutions.get(object);
		if (substitution == null) {
			return type;
		}
		return substitution.applyTo(type);
	}
	
	public void addInferer(ETypeParameter typeParameter, IInferer inferer) {
		myInferers.put(typeParameter, inferer);
	}
	
	public Collection<Diagnostic> getErrors() {
		return Collections.unmodifiableCollection(myErrors);
	}
	
	/**
	 * A do-method for validation. Makes all validation-related stuff, returns a reconstructed generic type
	 * for the given {@code object}. If this method was already called on a specific object, it will get previously
	 * calculated result from cache and will produce no diagnostic.
	 * @param object an object to be validated
	 * @return full generic type of {@object} 
	 */
	private EGenericType processEObject(EObject object) {
		// If an actual type is already known, then we can skip this object since we have 
		// already examined it before
		EGenericType genericType = myActualTypes.get(object);
		if (genericType != null) {
			return genericType;
		}
		// If a cycle appears -- break it
		if (myActualTypes.containsKey(object)) {
			return CYCLE;
		}
		myActualTypes.put(object, null);
		
		EClass rawClass = object.eClass();
		ISubstitution substitution = collectAllTypeParameters(rawClass);
		mySubstitutions.put(object, substitution);
		
		// We need to copy since this set will be changed during further steps of the algorithm
		Set<ETypeParameter> unsubstituted = new HashSet<ETypeParameter>(substitution.getUnsubstitutedParameters());
		for (ETypeParameter typeParameter : unsubstituted) {
			inferTypeParameterSubstitution(typeParameter, object, substitution);
		}
		if (!substitution.getUnsubstitutedParameters().isEmpty()) {
			throw new IllegalStateException();
		}
		
		genericType = substitution.getGenericType(rawClass);
		if (genericType == null) {
			throw new IllegalStateException();
		}
		myActualTypes.put(object, genericType);
		return genericType;
	}
	
	/**
	 * Traverses inheritance tree of a given EClass {@code rawClass} and gathers all the type parameters defined by its
	 * ancestors. Some of those parameters are substituted by inheritance (Class extends Super<Type>), 
	 * and in the end only parameters defined by {@code rawClass} are left unsubstituted (although some of ancestors'
	 * parameters might be substituted by {@code rawClass}' parameters.
	 * @param rawClass a class to collect parameters for
	 * @return a substitution of gathered parameters, where {@code rawClass}' own parameters are left unsubstituted 
	 */
	private ISubstitution collectAllTypeParameters(EClass rawClass) {
		ISubstitution substitution = new Substitution();
		EList<ETypeParameter> typeParameters = rawClass.getETypeParameters();
		for (ETypeParameter typeParameter : typeParameters) {
			substitution.addTypeParameter(typeParameter);
		}
		EList<EGenericType> allGenericSuperTypes = rawClass.getEAllGenericSuperTypes();
		for (EGenericType superType : allGenericSuperTypes) {
			if (!fillInTypeParameterValues(substitution, superType)) {
				
			}
		}
		return substitution;
	}

	private boolean fillInTypeParameterValues(ISubstitution substitution,
			EGenericType type) {
		if (type.getEClassifier().getETypeParameters().size() != type.getETypeArguments().size()) {
			reportError("Parameters and Arguments collections' sizes differ for " + GenericTypeRenderer.render(type), type);
			// TODO: restore consistent state or exit forever
			return false;
		}
		Iterator<ETypeParameter> superTypeParameters = type.getEClassifier().getETypeParameters().iterator();
		Iterator<EGenericType> superTypeArguments = type.getETypeArguments().iterator();
		while (superTypeArguments.hasNext()) {
			substitution.addSubstitution(superTypeParameters.next(), superTypeArguments.next());
		}
		return true;
	}

	private void reportError(String message, Object... data) {
		myErrors.add(new BasicDiagnostic(Diagnostic.ERROR, this.getClass().getName(), 0, message, data));
	}

	/**
	 * Infers {@code typeParameter} for {@code object}'s class from the actual data stored by given {@code object}
	 * and a (partial) {@ substitution}, extending the latter with the result found.
	 * If a type parameter cannot be inferred, it will be substituted with a wildcard with appropriate bounds.
	 * @param typeParameter a type parameter to be inferred, must belong to declaration of {@code object}'s EClass
	 * @param object an object containing actual data
	 * @param substitution a (partial) substitution of other type parameters
	 */
	private void inferTypeParameterSubstitution(ETypeParameter typeParameter,
			EObject object, ISubstitution substitution) {
		IInferer inferer = myInferers.get(typeParameter);
		if (inferer != null) {
			if (inferer.inferTypeParameterSubstitution(typeParameter, object, substitution)) {
				return;
			}
		}
		
		EClass eClass = object.eClass();
		EList<EReference> references = eClass.getEReferences();
		EGenericType typeArgument = null;
		
		for (EReference reference : references) {
			EGenericType referenceType = reference.getEGenericType();
			EGenericType substitutedReferenceType = substitution.applyTo(referenceType);
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				Collection<EObject> values = (Collection<EObject>) object.eGet(reference);
				EGenericType best = null;
				for (EObject value : values) {
					EGenericType actualType = processEObject(value);
					if (actualType == CYCLE) {
						continue;
					}
		
					EGenericType candidate = inferFromFormalType(typeParameter, substitutedReferenceType, actualType);
					if (best == null) {
						best = candidate;
					} else {
						if (EcoreValidator.isBounded(best, candidate, Collections.<ETypeParameter, EGenericType>emptyMap())) {
							// candidate bounds best, the the candidate is better
							best = candidate;
						} else if (!EcoreValidator.isBounded(candidate, best, Collections.<ETypeParameter, EGenericType>emptyMap())) {
							// if candidate and best do not bound each other, then they are inconsistent 
							// and it does not matter whom to choose
							break;
						}
					}
				}
				typeArgument = best;
			} else {
				EObject value = (EObject) object.eGet(reference);
				if (value == null) {
					continue;
				}
	
				EGenericType actualType = processEObject(value);
				if (actualType == CYCLE) {
					continue;
				}
				
				typeArgument = inferFromFormalType(typeParameter, substitutedReferenceType, actualType);
			}
			if (typeArgument != null) {
				break;
			}
		}
		if (typeArgument == null) {
			typeArgument = EcoreFactory.eINSTANCE.createEGenericType();
		}
		substitution.addSubstitution(typeParameter, typeArgument);		
	}

	/**
	 * Returns a substitution for {@code typeParameter} used in {@code genericType} declaration.
	 * NOTE: {@code typeParameter} must occur in declaration of {@code formalType}.
	 * @param typeParameter parameter to find a substitution for
	 * @param formalType a generic type (e. g. feature type) which involves {@code typeParameter} 
	 * @param actualType which substitutes {@code formalType} - a type to look for substitution in
	 * @return a generic type which substitutes {@code typeParameter} in {@code actualType}
	 * 			or {@code null} if {@code typeParameter} does not belong to declaration 
	 * 			of {@code actualType}.
	 * @throws IllegalArgumentException if structure of types is corrupted
	 */
	private EGenericType inferFromFormalType(ETypeParameter typeParameter, EGenericType formalType,
			EGenericType actualType) {
		// F = T -> A
		if (formalType.getETypeParameter() == typeParameter) {
			return actualType; 
		}
		// F = ? extends T -> A
		if (formalType.getEUpperBound() != null) {
			return inferFromFormalType(typeParameter, formalType.getEUpperBound(), actualType);
		}
		// F = ? super T -> A
		if (formalType.getELowerBound() != null) {
			return inferFromFormalType(typeParameter, formalType.getELowerBound(), actualType);
		}
		// raw(F) = raw(A) -> investigate the arguments
		if (formalType.getEClassifier() == actualType.getEClassifier()) {
			if (formalType.getETypeArguments().size() != actualType.getETypeArguments().size()) {
				throw new IllegalArgumentException();
			}
			Iterator<EGenericType> formalArguments = formalType.getETypeArguments().iterator();
			Iterator<EGenericType> actualArguments = actualType.getETypeArguments().iterator();
			while (formalArguments.hasNext()) {
				EGenericType substitution = inferFromFormalType(typeParameter, formalArguments.next(), actualArguments.next());
				if (substitution != null) {
					return substitution;
				}
			}
		}
		// raw(A) extends raw(F) -> find a supertype and use it as actual
		if (actualType.getEClassifier() instanceof EClass) {
			EClass actualClass = (EClass) actualType.getEClassifier();
			
			for (EGenericType superType : actualClass.getEAllGenericSuperTypes()) {
				if (superType.getEClassifier() == formalType.getEClassifier()) {
					// Child<X=Class> ---> Super<X=Class>
					ISubstitution parameters = new Substitution();
					fillInTypeParameterValues(parameters, actualType);
					EGenericType substitutedSuperType = parameters.applyTo(superType);
					return inferFromFormalType(typeParameter, formalType, substitutedSuperType);
				}
			}
		}
		// cannot infer anything reasonable
		return null;
	}

}
