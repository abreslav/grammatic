/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sequence Wildcard</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.SequenceWildcard#getVariable <em>Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getSequenceWildcard()
 * @generated
 */
public interface SequenceWildcard extends ExpressionQuery {

	/**
	 * Returns the value of the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variable</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variable</em>' reference.
	 * @see #setVariable(VariableDefinition)
	 * @see org.abreslav.grammatic.query.QueryPackage#getSequenceWildcard_Variable()
	 * @generated
	 */
	VariableDefinition getVariable();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.SequenceWildcard#getVariable <em>Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variable</em>' reference.
	 * @see #getVariable()
	 * @generated
	 */
	void setVariable(VariableDefinition value);
} // SequenceWildcard
