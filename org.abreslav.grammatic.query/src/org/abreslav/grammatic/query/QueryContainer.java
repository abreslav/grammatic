/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.QueryContainer#getQuery <em>Query</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.QueryContainer#getVariableDefinitions <em>Variable Definitions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getQueryContainer()
 * @generated
 */
public interface QueryContainer<T extends Query> extends EObject {
	/**
	 * Returns the value of the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Query</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Query</em>' containment reference.
	 * @see #setQuery(Query)
	 * @see org.abreslav.grammatic.query.QueryPackage#getQueryContainer_Query()
	 * @generated
	 */
	T getQuery();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.QueryContainer#getQuery <em>Query</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query</em>' containment reference.
	 * @see #getQuery()
	 * @generated
	 */
	void setQuery(T value);

	/**
	 * Returns the value of the '<em><b>Variable Definitions</b></em>' containment reference list.
	 * The list contents are of type {@link org.abreslav.grammatic.query.VariableDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variable Definitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variable Definitions</em>' containment reference list.
	 * @see org.abreslav.grammatic.query.QueryPackage#getQueryContainer_VariableDefinitions()
	 * @generated
	 */
	EList<VariableDefinition> getVariableDefinitions();

} // QueryContainer
