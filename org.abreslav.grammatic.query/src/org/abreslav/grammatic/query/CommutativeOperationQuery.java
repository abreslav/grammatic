/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Commutative Operation Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.CommutativeOperationQuery#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.CommutativeOperationQuery#isOpen <em>Open</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.CommutativeOperationQuery#getWildcardVariable <em>Wildcard Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getCommutativeOperationQuery()
 * @generated
 */
public interface CommutativeOperationQuery<D extends Query> extends Query {
	/**
	 * Returns the value of the '<em><b>Definitions</b></em>' containment reference list.
	 * The list contents are of type {@link D}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definitions</em>' containment reference list.
	 * @see org.abreslav.grammatic.query.QueryPackage#getCommutativeOperationQuery_Definitions()
	 * @generated
	 */
	EList<D> getDefinitions();

	/**
	 * Returns the value of the '<em><b>Open</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open</em>' attribute.
	 * @see #setOpen(boolean)
	 * @see org.abreslav.grammatic.query.QueryPackage#getCommutativeOperationQuery_Open()
	 * @generated
	 */
	boolean isOpen();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.CommutativeOperationQuery#isOpen <em>Open</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open</em>' attribute.
	 * @see #isOpen()
	 * @generated
	 */
	void setOpen(boolean value);

	/**
	 * Returns the value of the '<em><b>Wildcard Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wildcard Variable</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wildcard Variable</em>' reference.
	 * @see #setWildcardVariable(VariableDefinition)
	 * @see org.abreslav.grammatic.query.QueryPackage#getCommutativeOperationQuery_WildcardVariable()
	 * @generated
	 */
	VariableDefinition getWildcardVariable();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.CommutativeOperationQuery#getWildcardVariable <em>Wildcard Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wildcard Variable</em>' reference.
	 * @see #getWildcardVariable()
	 * @generated
	 */
	void setWildcardVariable(VariableDefinition value);

} // CommutativeOperationQuery
