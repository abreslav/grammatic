/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.QueryPackage
 * @generated
 */
public interface QueryFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	QueryFactory eINSTANCE = org.abreslav.grammatic.query.impl.QueryFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Container</em>'.
	 * @generated
	 */
	<T extends Query> QueryContainer<T> createQueryContainer();

	/**
	 * Returns a new object of class '<em>Rule Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rule Query</em>'.
	 * @generated
	 */
	RuleQuery createRuleQuery();

	/**
	 * Returns a new object of class '<em>Symbol Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Symbol Query</em>'.
	 * @generated
	 */
	SymbolQuery createSymbolQuery();

	/**
	 * Returns a new object of class '<em>Production Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Production Query</em>'.
	 * @generated
	 */
	ProductionQuery createProductionQuery();

	/**
	 * Returns a new object of class '<em>Empty Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Empty Query</em>'.
	 * @generated
	 */
	EmptyQuery createEmptyQuery();

	/**
	 * Returns a new object of class '<em>Symbol Reference Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Symbol Reference Query</em>'.
	 * @generated
	 */
	SymbolReferenceQuery createSymbolReferenceQuery();

	/**
	 * Returns a new object of class '<em>Exact Expression Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exact Expression Query</em>'.
	 * @generated
	 */
	ExactExpressionQuery createExactExpressionQuery();

	/**
	 * Returns a new object of class '<em>Lexical Wildcard Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Lexical Wildcard Query</em>'.
	 * @generated
	 */
	LexicalWildcardQuery createLexicalWildcardQuery();

	/**
	 * Returns a new object of class '<em>Variable Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Definition</em>'.
	 * @generated
	 */
	VariableDefinition createVariableDefinition();

	/**
	 * Returns a new object of class '<em>Variable Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Reference</em>'.
	 * @generated
	 */
	VariableReference createVariableReference();

	/**
	 * Returns a new object of class '<em>Alternative Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Alternative Query</em>'.
	 * @generated
	 */
	AlternativeQuery createAlternativeQuery();

	/**
	 * Returns a new object of class '<em>Sequence Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sequence Query</em>'.
	 * @generated
	 */
	SequenceQuery createSequenceQuery();

	/**
	 * Returns a new object of class '<em>Sequence Wildcard</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sequence Wildcard</em>'.
	 * @generated
	 */
	SequenceWildcard createSequenceWildcard();

	/**
	 * Returns a new object of class '<em>Alternative Wildcard</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Alternative Wildcard</em>'.
	 * @generated
	 */
	AlternativeWildcard createAlternativeWildcard();

	/**
	 * Returns a new object of class '<em>Iteration Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Iteration Query</em>'.
	 * @generated
	 */
	IterationQuery createIterationQuery();

	/**
	 * Returns a new object of class '<em>Attribute Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Query</em>'.
	 * @generated
	 */
	AttributeQuery createAttributeQuery();

	/**
	 * Returns a new object of class '<em>Attribute Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Type</em>'.
	 * @generated
	 */
	AttributeType createAttributeType();

	/**
	 * Returns a new object of class '<em>Attribute Presence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Presence</em>'.
	 * @generated
	 */
	AttributePresence createAttributePresence();

	/**
	 * Returns a new object of class '<em>Exact Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exact Value</em>'.
	 * @generated
	 */
	ExactValue createExactValue();

	/**
	 * Returns a new object of class '<em>Typed Reg Exp Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Typed Reg Exp Value</em>'.
	 * @generated
	 */
	TypedRegExpValue createTypedRegExpValue();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	QueryPackage getQueryPackage();

} //QueryFactory
