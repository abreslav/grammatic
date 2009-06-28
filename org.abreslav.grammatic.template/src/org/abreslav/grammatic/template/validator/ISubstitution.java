package org.abreslav.grammatic.template.validator;

import java.util.Set;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;

/**
 * Describes a type parameter substitution - a set of pairs (ETypeParameter, ETypeArgument)
 * which allows to find a substituting argument for any parameter 
 */
interface ISubstitution {
	/**
	 * @return a set of parameters mentioned in this substitution, which do not have corresponding arguments
	 */
	Set<ETypeParameter> getUnsubstitutedParameters();

	/**
	 * Reconstruct a generic type with all the parameters declared by {@code rawType} appropriately set
	 * @param rawType a classifier to reconstruct a generic type for
	 * @return a reconstructed type
	 */
	EGenericType getGenericType(EClassifier rawType);

	/**
	 * Returns an object describing why the {@code typeParameter} was substituted with a particular argument
	 * within this substitution  
	 * @param typeParameter a parameter to return a reason for
	 * @return the reason object
	 */
	ISubstitutionReason getSubstitutionReason(ETypeParameter typeParameter);

	/**
	 * Adds an unsubstituted parameter to this substitution
	 * @param typeParameter a parameter to be added
	 */
	void addTypeParameter(ETypeParameter typeParameter);

	/**
	 * Adds a substitution for a particular parameter.
	 * Upon this action {@code parameter} is removed from {@link #getUnsubstitutedParameters()}
	 * @param typeParameter ETypeParameter being substituted
	 * @param value a substitution for the {@code parameter}
	 */
	void addSubstitution(ETypeParameter typeParameter, EGenericType value);

	/**
	 * Returns a EGenericType which is equal to {@code genericType) but with all substituted parameters replaced
	 * by their known substitutions. Unsubstituted parameters are left unchanged.
	 * @param genericType a type to apply substitution to
	 * @return a generic type which takes this substitution into account
	 */
	EGenericType applyTo(EGenericType genericType);
}