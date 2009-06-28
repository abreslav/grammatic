/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.abreslav.grammatic.grammar.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exact Expression Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.ExactExpressionQuery#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getExactExpressionQuery()
 * @generated
 */
public interface ExactExpressionQuery extends ExpressionQuery {
	/**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference.
	 * @see #setExpression(Expression)
	 * @see org.abreslav.grammatic.query.QueryPackage#getExactExpressionQuery_Expression()
	 * @generated
	 */
	Expression getExpression();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.ExactExpressionQuery#getExpression <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' containment reference.
	 * @see #getExpression()
	 * @generated
	 */
	void setExpression(Expression value);

} // ExactExpressionQuery
