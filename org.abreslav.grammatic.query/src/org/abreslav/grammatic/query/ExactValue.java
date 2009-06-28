/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.abreslav.grammatic.metadata.Value;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exact Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.ExactValue#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getExactValue()
 * @generated
 */
public interface ExactValue extends AttributeValueQuery {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(Value)
	 * @see org.abreslav.grammatic.query.QueryPackage#getExactValue_Value()
	 * @generated
	 */
	Value getValue();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.ExactValue#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Value value);

} // ExactValue
