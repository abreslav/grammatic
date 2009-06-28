/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Iteration Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.IterationQuery#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.IterationQuery#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.IterationQuery#getUpperBound <em>Upper Bound</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getIterationQuery()
 * @generated
 */
public interface IterationQuery extends ExpressionQuery {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' containment reference.
	 * @see #setDefinition(ExpressionQuery)
	 * @see org.abreslav.grammatic.query.QueryPackage#getIterationQuery_Definition()
	 * @generated
	 */
	ExpressionQuery getDefinition();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.IterationQuery#getDefinition <em>Definition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' containment reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(ExpressionQuery value);

	/**
	 * Returns the value of the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lower Bound</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lower Bound</em>' attribute.
	 * @see #setLowerBound(int)
	 * @see org.abreslav.grammatic.query.QueryPackage#getIterationQuery_LowerBound()
	 * @generated
	 */
	int getLowerBound();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.IterationQuery#getLowerBound <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lower Bound</em>' attribute.
	 * @see #getLowerBound()
	 * @generated
	 */
	void setLowerBound(int value);

	/**
	 * Returns the value of the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Upper Bound</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upper Bound</em>' attribute.
	 * @see #setUpperBound(int)
	 * @see org.abreslav.grammatic.query.QueryPackage#getIterationQuery_UpperBound()
	 * @generated
	 */
	int getUpperBound();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.IterationQuery#getUpperBound <em>Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upper Bound</em>' attribute.
	 * @see #getUpperBound()
	 * @generated
	 */
	void setUpperBound(int value);

} // IterationQuery
