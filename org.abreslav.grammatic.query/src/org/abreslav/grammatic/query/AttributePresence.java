/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Presence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.AttributePresence#isPresent <em>Present</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getAttributePresence()
 * @generated
 */
public interface AttributePresence extends AttributeValueQuery {
	/**
	 * Returns the value of the '<em><b>Present</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Present</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Present</em>' attribute.
	 * @see #setPresent(boolean)
	 * @see org.abreslav.grammatic.query.QueryPackage#getAttributePresence_Present()
	 * @generated
	 */
	boolean isPresent();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.AttributePresence#isPresent <em>Present</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Present</em>' attribute.
	 * @see #isPresent()
	 * @generated
	 */
	void setPresent(boolean value);

} // AttributePresence
