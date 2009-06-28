/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.AttributeType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeType()
 * @generated
 */
public interface AttributeType extends AttributeValueQuery {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.abreslav.grammatic.query.AttributeTypeOptions}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.abreslav.grammatic.query.AttributeTypeOptions
	 * @see #setType(AttributeTypeOptions)
	 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeType_Type()
	 * @generated
	 */
	AttributeTypeOptions getType();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.AttributeType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.abreslav.grammatic.query.AttributeTypeOptions
	 * @see #getType()
	 * @generated
	 */
	void setType(AttributeTypeOptions value);

} // AttributeType
