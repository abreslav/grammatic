/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeValue <em>Attribute Value</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.AttributeQuery#getNamespaceUri <em>Namespace Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeQuery()
 * @generated
 */
public interface AttributeQuery extends EObject {
	/**
	 * Returns the value of the '<em><b>Attribute Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Value</em>' containment reference.
	 * @see #setAttributeValue(AttributeValueQuery)
	 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeQuery_AttributeValue()
	 * @generated
	 */
	AttributeValueQuery getAttributeValue();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeValue <em>Attribute Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Value</em>' containment reference.
	 * @see #getAttributeValue()
	 * @generated
	 */
	void setAttributeValue(AttributeValueQuery value);

	/**
	 * Returns the value of the '<em><b>Attribute Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Name</em>' attribute.
	 * @see #setAttributeName(String)
	 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeQuery_AttributeName()
	 * @generated
	 */
	String getAttributeName();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeName <em>Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Name</em>' attribute.
	 * @see #getAttributeName()
	 * @generated
	 */
	void setAttributeName(String value);

	/**
	 * Returns the value of the '<em><b>Namespace Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Namespace Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespace Uri</em>' attribute.
	 * @see #setNamespaceUri(String)
	 * @see org.abreslav.grammatic.query.QueryPackage#getAttributeQuery_NamespaceUri()
	 * @generated
	 */
	String getNamespaceUri();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.AttributeQuery#getNamespaceUri <em>Namespace Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace Uri</em>' attribute.
	 * @see #getNamespaceUri()
	 * @generated
	 */
	void setNamespaceUri(String value);

} // AttributeQuery
