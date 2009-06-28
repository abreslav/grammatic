/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.abreslav.grammatic.grammar.LexicalDefinition;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lexical Wildcard Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.LexicalWildcardQuery#getDefinition <em>Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getLexicalWildcardQuery()
 * @generated
 */
public interface LexicalWildcardQuery extends ExpressionQuery {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' containment reference.
	 * @see #setDefinition(LexicalDefinition)
	 * @see org.abreslav.grammatic.query.QueryPackage#getLexicalWildcardQuery_Definition()
	 * @generated
	 */
	LexicalDefinition getDefinition();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.LexicalWildcardQuery#getDefinition <em>Definition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' containment reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(LexicalDefinition value);

} // LexicalWildcardQuery
