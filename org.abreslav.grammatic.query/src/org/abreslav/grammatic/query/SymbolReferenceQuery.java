/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Symbol Reference Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.SymbolReferenceQuery#getSymbol <em>Symbol</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getSymbolReferenceQuery()
 * @generated
 */
public interface SymbolReferenceQuery extends ExpressionQuery {

	/**
	 * Returns the value of the '<em><b>Symbol</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Symbol</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbol</em>' containment reference.
	 * @see #setSymbol(SymbolQuery)
	 * @see org.abreslav.grammatic.query.QueryPackage#getSymbolReferenceQuery_Symbol()
	 * @generated
	 */
	SymbolQuery getSymbol();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.SymbolReferenceQuery#getSymbol <em>Symbol</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbol</em>' containment reference.
	 * @see #getSymbol()
	 * @generated
	 */
	void setSymbol(SymbolQuery value);
} // SymbolReferenceQuery
