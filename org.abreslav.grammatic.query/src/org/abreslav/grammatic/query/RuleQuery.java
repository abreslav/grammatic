/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.RuleQuery#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.RuleQuery#getSymbolVariable <em>Symbol Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.abreslav.grammatic.query.QueryPackage#getRuleQuery()
 * @generated
 */
public interface RuleQuery extends Query, CommutativeOperationQuery<ProductionQuery> {
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
	 * @see org.abreslav.grammatic.query.QueryPackage#getRuleQuery_Symbol()
	 * @generated
	 */
	SymbolQuery getSymbol();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.RuleQuery#getSymbol <em>Symbol</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbol</em>' containment reference.
	 * @see #getSymbol()
	 * @generated
	 */
	void setSymbol(SymbolQuery value);

	/**
	 * Returns the value of the '<em><b>Symbol Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Symbol Variable</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Symbol Variable</em>' reference.
	 * @see #setSymbolVariable(VariableDefinition)
	 * @see org.abreslav.grammatic.query.QueryPackage#getRuleQuery_SymbolVariable()
	 * @generated
	 */
	VariableDefinition getSymbolVariable();

	/**
	 * Sets the value of the '{@link org.abreslav.grammatic.query.RuleQuery#getSymbolVariable <em>Symbol Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Symbol Variable</em>' reference.
	 * @see #getSymbolVariable()
	 * @generated
	 */
	void setSymbolVariable(VariableDefinition value);

} // RuleQuery
