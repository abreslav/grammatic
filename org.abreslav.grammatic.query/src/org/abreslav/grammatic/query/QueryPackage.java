/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.QueryFactory
 * @generated
 */
public interface QueryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "query";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.abreslav.org/Grammatic/2008/Query";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "grammatic-query";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	QueryPackage eINSTANCE = org.abreslav.grammatic.query.impl.QueryPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.QueryContainerImpl <em>Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.QueryContainerImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getQueryContainer()
	 * @generated
	 */
	int QUERY_CONTAINER = 0;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_CONTAINER__QUERY = 0;

	/**
	 * The feature id for the '<em><b>Variable Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_CONTAINER__VARIABLE_DEFINITIONS = 1;

	/**
	 * The number of structural features of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_CONTAINER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.QueryImpl <em>Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.QueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getQuery()
	 * @generated
	 */
	int QUERY = 1;

	/**
	 * The number of structural features of the '<em>Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl <em>Commutative Operation Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getCommutativeOperationQuery()
	 * @generated
	 */
	int COMMUTATIVE_OPERATION_QUERY = 2;

	/**
	 * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUTATIVE_OPERATION_QUERY__DEFINITIONS = QUERY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Open</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUTATIVE_OPERATION_QUERY__OPEN = QUERY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Wildcard Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE = QUERY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Commutative Operation Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUTATIVE_OPERATION_QUERY_FEATURE_COUNT = QUERY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.RuleQueryImpl <em>Rule Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.RuleQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getRuleQuery()
	 * @generated
	 */
	int RULE_QUERY = 3;

	/**
	 * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY__DEFINITIONS = QUERY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Open</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY__OPEN = QUERY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Wildcard Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY__WILDCARD_VARIABLE = QUERY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Symbol</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY__SYMBOL = QUERY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Symbol Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY__SYMBOL_VARIABLE = QUERY_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Rule Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_QUERY_FEATURE_COUNT = QUERY_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.MetadataQueryImpl <em>Metadata Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.MetadataQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getMetadataQuery()
	 * @generated
	 */
	int METADATA_QUERY = 18;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_QUERY__ATTRIBUTES = QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Metadata Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_QUERY_FEATURE_COUNT = QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.SymbolQueryImpl <em>Symbol Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.SymbolQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSymbolQuery()
	 * @generated
	 */
	int SYMBOL_QUERY = 4;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_QUERY__ATTRIBUTES = METADATA_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_QUERY__NAME = METADATA_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Symbol Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_QUERY_FEATURE_COUNT = METADATA_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.ProductionQueryImpl <em>Production Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.ProductionQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getProductionQuery()
	 * @generated
	 */
	int PRODUCTION_QUERY = 5;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCTION_QUERY__ATTRIBUTES = METADATA_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCTION_QUERY__DEFINITION = METADATA_QUERY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCTION_QUERY__VARIABLE = METADATA_QUERY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Production Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCTION_QUERY_FEATURE_COUNT = METADATA_QUERY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.ExpressionQueryImpl <em>Expression Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.ExpressionQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExpressionQuery()
	 * @generated
	 */
	int EXPRESSION_QUERY = 6;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_QUERY__ATTRIBUTES = METADATA_QUERY__ATTRIBUTES;

	/**
	 * The number of structural features of the '<em>Expression Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_QUERY_FEATURE_COUNT = METADATA_QUERY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.EmptyQueryImpl <em>Empty Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.EmptyQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getEmptyQuery()
	 * @generated
	 */
	int EMPTY_QUERY = 7;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPTY_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The number of structural features of the '<em>Empty Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPTY_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.SymbolReferenceQueryImpl <em>Symbol Reference Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.SymbolReferenceQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSymbolReferenceQuery()
	 * @generated
	 */
	int SYMBOL_REFERENCE_QUERY = 8;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_REFERENCE_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Symbol</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_REFERENCE_QUERY__SYMBOL = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Symbol Reference Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYMBOL_REFERENCE_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.ExactExpressionQueryImpl <em>Exact Expression Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.ExactExpressionQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExactExpressionQuery()
	 * @generated
	 */
	int EXACT_EXPRESSION_QUERY = 9;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXACT_EXPRESSION_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXACT_EXPRESSION_QUERY__EXPRESSION = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Exact Expression Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXACT_EXPRESSION_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.LexicalWildcardQueryImpl <em>Lexical Wildcard Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.LexicalWildcardQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getLexicalWildcardQuery()
	 * @generated
	 */
	int LEXICAL_WILDCARD_QUERY = 10;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXICAL_WILDCARD_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXICAL_WILDCARD_QUERY__DEFINITION = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Lexical Wildcard Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXICAL_WILDCARD_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.VariableDefinitionImpl <em>Variable Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.VariableDefinitionImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getVariableDefinition()
	 * @generated
	 */
	int VARIABLE_DEFINITION = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_DEFINITION__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Variable Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_DEFINITION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.VariableReferenceImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getVariableReference()
	 * @generated
	 */
	int VARIABLE_REFERENCE = 12;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__VARIABLE = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Variable Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AlternativeQueryImpl <em>Alternative Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AlternativeQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAlternativeQuery()
	 * @generated
	 */
	int ALTERNATIVE_QUERY = 13;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_QUERY__DEFINITIONS = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Open</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_QUERY__OPEN = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Wildcard Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_QUERY__WILDCARD_VARIABLE = EXPRESSION_QUERY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Alternative Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.SequenceQueryImpl <em>Sequence Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.SequenceQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSequenceQuery()
	 * @generated
	 */
	int SEQUENCE_QUERY = 14;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_QUERY__DEFINITIONS = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sequence Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.SequenceWildcardImpl <em>Sequence Wildcard</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.SequenceWildcardImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSequenceWildcard()
	 * @generated
	 */
	int SEQUENCE_WILDCARD = 15;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_WILDCARD__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_WILDCARD__VARIABLE = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sequence Wildcard</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_WILDCARD_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AlternativeWildcardImpl <em>Alternative Wildcard</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AlternativeWildcardImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAlternativeWildcard()
	 * @generated
	 */
	int ALTERNATIVE_WILDCARD = 16;

	/**
	 * The feature id for the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_WILDCARD__VARIABLE = QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Alternative Wildcard</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_WILDCARD_FEATURE_COUNT = QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.IterationQueryImpl <em>Iteration Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.IterationQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getIterationQuery()
	 * @generated
	 */
	int ITERATION_QUERY = 17;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATION_QUERY__ATTRIBUTES = EXPRESSION_QUERY__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATION_QUERY__DEFINITION = EXPRESSION_QUERY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATION_QUERY__LOWER_BOUND = EXPRESSION_QUERY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATION_QUERY__UPPER_BOUND = EXPRESSION_QUERY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Iteration Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATION_QUERY_FEATURE_COUNT = EXPRESSION_QUERY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AttributeQueryImpl <em>Attribute Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AttributeQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeQuery()
	 * @generated
	 */
	int ATTRIBUTE_QUERY = 19;

	/**
	 * The feature id for the '<em><b>Attribute Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_QUERY__ATTRIBUTE_VALUE = 0;

	/**
	 * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_QUERY__ATTRIBUTE_NAME = 1;

	/**
	 * The feature id for the '<em><b>Namespace Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_QUERY__NAMESPACE_URI = 2;

	/**
	 * The number of structural features of the '<em>Attribute Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_QUERY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AttributeValueQueryImpl <em>Attribute Value Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AttributeValueQueryImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeValueQuery()
	 * @generated
	 */
	int ATTRIBUTE_VALUE_QUERY = 20;

	/**
	 * The number of structural features of the '<em>Attribute Value Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AttributeTypeImpl <em>Attribute Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AttributeTypeImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeType()
	 * @generated
	 */
	int ATTRIBUTE_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_TYPE__TYPE = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_TYPE_FEATURE_COUNT = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.AttributePresenceImpl <em>Attribute Presence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.AttributePresenceImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributePresence()
	 * @generated
	 */
	int ATTRIBUTE_PRESENCE = 22;

	/**
	 * The feature id for the '<em><b>Present</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_PRESENCE__PRESENT = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Attribute Presence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_PRESENCE_FEATURE_COUNT = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.ExactValueImpl <em>Exact Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.ExactValueImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExactValue()
	 * @generated
	 */
	int EXACT_VALUE = 23;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXACT_VALUE__VALUE = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Exact Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXACT_VALUE_FEATURE_COUNT = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.impl.TypedRegExpValueImpl <em>Typed Reg Exp Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.impl.TypedRegExpValueImpl
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getTypedRegExpValue()
	 * @generated
	 */
	int TYPED_REG_EXP_VALUE = 24;

	/**
	 * The feature id for the '<em><b>Wild Card Separated Fragments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Typed Reg Exp Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_REG_EXP_VALUE_FEATURE_COUNT = ATTRIBUTE_VALUE_QUERY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.AttributeTypeOptions <em>Attribute Type Options</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.AttributeTypeOptions
	 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeTypeOptions()
	 * @generated
	 */
	int ATTRIBUTE_TYPE_OPTIONS = 25;


	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.QueryContainer <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container</em>'.
	 * @see org.abreslav.grammatic.query.QueryContainer
	 * @generated
	 */
	EClass getQueryContainer();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.QueryContainer#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see org.abreslav.grammatic.query.QueryContainer#getQuery()
	 * @see #getQueryContainer()
	 * @generated
	 */
	EReference getQueryContainer_Query();

	/**
	 * Returns the meta object for the containment reference list '{@link org.abreslav.grammatic.query.QueryContainer#getVariableDefinitions <em>Variable Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Variable Definitions</em>'.
	 * @see org.abreslav.grammatic.query.QueryContainer#getVariableDefinitions()
	 * @see #getQueryContainer()
	 * @generated
	 */
	EReference getQueryContainer_VariableDefinitions();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.Query <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query</em>'.
	 * @see org.abreslav.grammatic.query.Query
	 * @generated
	 */
	EClass getQuery();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.CommutativeOperationQuery <em>Commutative Operation Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commutative Operation Query</em>'.
	 * @see org.abreslav.grammatic.query.CommutativeOperationQuery
	 * @generated
	 */
	EClass getCommutativeOperationQuery();

	/**
	 * Returns the meta object for the containment reference list '{@link org.abreslav.grammatic.query.CommutativeOperationQuery#getDefinitions <em>Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Definitions</em>'.
	 * @see org.abreslav.grammatic.query.CommutativeOperationQuery#getDefinitions()
	 * @see #getCommutativeOperationQuery()
	 * @generated
	 */
	EReference getCommutativeOperationQuery_Definitions();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.CommutativeOperationQuery#isOpen <em>Open</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Open</em>'.
	 * @see org.abreslav.grammatic.query.CommutativeOperationQuery#isOpen()
	 * @see #getCommutativeOperationQuery()
	 * @generated
	 */
	EAttribute getCommutativeOperationQuery_Open();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.CommutativeOperationQuery#getWildcardVariable <em>Wildcard Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Wildcard Variable</em>'.
	 * @see org.abreslav.grammatic.query.CommutativeOperationQuery#getWildcardVariable()
	 * @see #getCommutativeOperationQuery()
	 * @generated
	 */
	EReference getCommutativeOperationQuery_WildcardVariable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.RuleQuery <em>Rule Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Query</em>'.
	 * @see org.abreslav.grammatic.query.RuleQuery
	 * @generated
	 */
	EClass getRuleQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.RuleQuery#getSymbol <em>Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Symbol</em>'.
	 * @see org.abreslav.grammatic.query.RuleQuery#getSymbol()
	 * @see #getRuleQuery()
	 * @generated
	 */
	EReference getRuleQuery_Symbol();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.RuleQuery#getSymbolVariable <em>Symbol Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Symbol Variable</em>'.
	 * @see org.abreslav.grammatic.query.RuleQuery#getSymbolVariable()
	 * @see #getRuleQuery()
	 * @generated
	 */
	EReference getRuleQuery_SymbolVariable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.SymbolQuery <em>Symbol Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Symbol Query</em>'.
	 * @see org.abreslav.grammatic.query.SymbolQuery
	 * @generated
	 */
	EClass getSymbolQuery();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.SymbolQuery#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.abreslav.grammatic.query.SymbolQuery#getName()
	 * @see #getSymbolQuery()
	 * @generated
	 */
	EAttribute getSymbolQuery_Name();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.ProductionQuery <em>Production Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Production Query</em>'.
	 * @see org.abreslav.grammatic.query.ProductionQuery
	 * @generated
	 */
	EClass getProductionQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.ProductionQuery#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Definition</em>'.
	 * @see org.abreslav.grammatic.query.ProductionQuery#getDefinition()
	 * @see #getProductionQuery()
	 * @generated
	 */
	EReference getProductionQuery_Definition();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.ProductionQuery#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Variable</em>'.
	 * @see org.abreslav.grammatic.query.ProductionQuery#getVariable()
	 * @see #getProductionQuery()
	 * @generated
	 */
	EReference getProductionQuery_Variable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.ExpressionQuery <em>Expression Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression Query</em>'.
	 * @see org.abreslav.grammatic.query.ExpressionQuery
	 * @generated
	 */
	EClass getExpressionQuery();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.EmptyQuery <em>Empty Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Empty Query</em>'.
	 * @see org.abreslav.grammatic.query.EmptyQuery
	 * @generated
	 */
	EClass getEmptyQuery();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.SymbolReferenceQuery <em>Symbol Reference Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Symbol Reference Query</em>'.
	 * @see org.abreslav.grammatic.query.SymbolReferenceQuery
	 * @generated
	 */
	EClass getSymbolReferenceQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.SymbolReferenceQuery#getSymbol <em>Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Symbol</em>'.
	 * @see org.abreslav.grammatic.query.SymbolReferenceQuery#getSymbol()
	 * @see #getSymbolReferenceQuery()
	 * @generated
	 */
	EReference getSymbolReferenceQuery_Symbol();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.ExactExpressionQuery <em>Exact Expression Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exact Expression Query</em>'.
	 * @see org.abreslav.grammatic.query.ExactExpressionQuery
	 * @generated
	 */
	EClass getExactExpressionQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.ExactExpressionQuery#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Expression</em>'.
	 * @see org.abreslav.grammatic.query.ExactExpressionQuery#getExpression()
	 * @see #getExactExpressionQuery()
	 * @generated
	 */
	EReference getExactExpressionQuery_Expression();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.LexicalWildcardQuery <em>Lexical Wildcard Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lexical Wildcard Query</em>'.
	 * @see org.abreslav.grammatic.query.LexicalWildcardQuery
	 * @generated
	 */
	EClass getLexicalWildcardQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.LexicalWildcardQuery#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Definition</em>'.
	 * @see org.abreslav.grammatic.query.LexicalWildcardQuery#getDefinition()
	 * @see #getLexicalWildcardQuery()
	 * @generated
	 */
	EReference getLexicalWildcardQuery_Definition();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.VariableDefinition <em>Variable Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Definition</em>'.
	 * @see org.abreslav.grammatic.query.VariableDefinition
	 * @generated
	 */
	EClass getVariableDefinition();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.VariableDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.abreslav.grammatic.query.VariableDefinition#getName()
	 * @see #getVariableDefinition()
	 * @generated
	 */
	EAttribute getVariableDefinition_Name();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.VariableDefinition#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.abreslav.grammatic.query.VariableDefinition#getValue()
	 * @see #getVariableDefinition()
	 * @generated
	 */
	EReference getVariableDefinition_Value();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.VariableReference <em>Variable Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Reference</em>'.
	 * @see org.abreslav.grammatic.query.VariableReference
	 * @generated
	 */
	EClass getVariableReference();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.VariableReference#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Variable</em>'.
	 * @see org.abreslav.grammatic.query.VariableReference#getVariable()
	 * @see #getVariableReference()
	 * @generated
	 */
	EReference getVariableReference_Variable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AlternativeQuery <em>Alternative Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alternative Query</em>'.
	 * @see org.abreslav.grammatic.query.AlternativeQuery
	 * @generated
	 */
	EClass getAlternativeQuery();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.SequenceQuery <em>Sequence Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence Query</em>'.
	 * @see org.abreslav.grammatic.query.SequenceQuery
	 * @generated
	 */
	EClass getSequenceQuery();

	/**
	 * Returns the meta object for the containment reference list '{@link org.abreslav.grammatic.query.SequenceQuery#getDefinitions <em>Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Definitions</em>'.
	 * @see org.abreslav.grammatic.query.SequenceQuery#getDefinitions()
	 * @see #getSequenceQuery()
	 * @generated
	 */
	EReference getSequenceQuery_Definitions();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.SequenceWildcard <em>Sequence Wildcard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence Wildcard</em>'.
	 * @see org.abreslav.grammatic.query.SequenceWildcard
	 * @generated
	 */
	EClass getSequenceWildcard();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.SequenceWildcard#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Variable</em>'.
	 * @see org.abreslav.grammatic.query.SequenceWildcard#getVariable()
	 * @see #getSequenceWildcard()
	 * @generated
	 */
	EReference getSequenceWildcard_Variable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AlternativeWildcard <em>Alternative Wildcard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alternative Wildcard</em>'.
	 * @see org.abreslav.grammatic.query.AlternativeWildcard
	 * @generated
	 */
	EClass getAlternativeWildcard();

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.AlternativeWildcard#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Variable</em>'.
	 * @see org.abreslav.grammatic.query.AlternativeWildcard#getVariable()
	 * @see #getAlternativeWildcard()
	 * @generated
	 */
	EReference getAlternativeWildcard_Variable();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.IterationQuery <em>Iteration Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Iteration Query</em>'.
	 * @see org.abreslav.grammatic.query.IterationQuery
	 * @generated
	 */
	EClass getIterationQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.IterationQuery#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Definition</em>'.
	 * @see org.abreslav.grammatic.query.IterationQuery#getDefinition()
	 * @see #getIterationQuery()
	 * @generated
	 */
	EReference getIterationQuery_Definition();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.IterationQuery#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see org.abreslav.grammatic.query.IterationQuery#getLowerBound()
	 * @see #getIterationQuery()
	 * @generated
	 */
	EAttribute getIterationQuery_LowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.IterationQuery#getUpperBound <em>Upper Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper Bound</em>'.
	 * @see org.abreslav.grammatic.query.IterationQuery#getUpperBound()
	 * @see #getIterationQuery()
	 * @generated
	 */
	EAttribute getIterationQuery_UpperBound();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.MetadataQuery <em>Metadata Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata Query</em>'.
	 * @see org.abreslav.grammatic.query.MetadataQuery
	 * @generated
	 */
	EClass getMetadataQuery();

	/**
	 * Returns the meta object for the containment reference list '{@link org.abreslav.grammatic.query.MetadataQuery#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.abreslav.grammatic.query.MetadataQuery#getAttributes()
	 * @see #getMetadataQuery()
	 * @generated
	 */
	EReference getMetadataQuery_Attributes();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AttributeQuery <em>Attribute Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Query</em>'.
	 * @see org.abreslav.grammatic.query.AttributeQuery
	 * @generated
	 */
	EClass getAttributeQuery();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeValue <em>Attribute Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Attribute Value</em>'.
	 * @see org.abreslav.grammatic.query.AttributeQuery#getAttributeValue()
	 * @see #getAttributeQuery()
	 * @generated
	 */
	EReference getAttributeQuery_AttributeValue();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.AttributeQuery#getAttributeName <em>Attribute Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute Name</em>'.
	 * @see org.abreslav.grammatic.query.AttributeQuery#getAttributeName()
	 * @see #getAttributeQuery()
	 * @generated
	 */
	EAttribute getAttributeQuery_AttributeName();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.AttributeQuery#getNamespaceUri <em>Namespace Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace Uri</em>'.
	 * @see org.abreslav.grammatic.query.AttributeQuery#getNamespaceUri()
	 * @see #getAttributeQuery()
	 * @generated
	 */
	EAttribute getAttributeQuery_NamespaceUri();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AttributeValueQuery <em>Attribute Value Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Value Query</em>'.
	 * @see org.abreslav.grammatic.query.AttributeValueQuery
	 * @generated
	 */
	EClass getAttributeValueQuery();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AttributeType <em>Attribute Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Type</em>'.
	 * @see org.abreslav.grammatic.query.AttributeType
	 * @generated
	 */
	EClass getAttributeType();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.AttributeType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.abreslav.grammatic.query.AttributeType#getType()
	 * @see #getAttributeType()
	 * @generated
	 */
	EAttribute getAttributeType_Type();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.AttributePresence <em>Attribute Presence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute Presence</em>'.
	 * @see org.abreslav.grammatic.query.AttributePresence
	 * @generated
	 */
	EClass getAttributePresence();

	/**
	 * Returns the meta object for the attribute '{@link org.abreslav.grammatic.query.AttributePresence#isPresent <em>Present</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Present</em>'.
	 * @see org.abreslav.grammatic.query.AttributePresence#isPresent()
	 * @see #getAttributePresence()
	 * @generated
	 */
	EAttribute getAttributePresence_Present();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.ExactValue <em>Exact Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exact Value</em>'.
	 * @see org.abreslav.grammatic.query.ExactValue
	 * @generated
	 */
	EClass getExactValue();

	/**
	 * Returns the meta object for the containment reference '{@link org.abreslav.grammatic.query.ExactValue#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.abreslav.grammatic.query.ExactValue#getValue()
	 * @see #getExactValue()
	 * @generated
	 */
	EReference getExactValue_Value();

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.TypedRegExpValue <em>Typed Reg Exp Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Reg Exp Value</em>'.
	 * @see org.abreslav.grammatic.query.TypedRegExpValue
	 * @generated
	 */
	EClass getTypedRegExpValue();

	/**
	 * Returns the meta object for the containment reference list '{@link org.abreslav.grammatic.query.TypedRegExpValue#getWildCardSeparatedFragments <em>Wild Card Separated Fragments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Wild Card Separated Fragments</em>'.
	 * @see org.abreslav.grammatic.query.TypedRegExpValue#getWildCardSeparatedFragments()
	 * @see #getTypedRegExpValue()
	 * @generated
	 */
	EReference getTypedRegExpValue_WildCardSeparatedFragments();

	/**
	 * Returns the meta object for enum '{@link org.abreslav.grammatic.query.AttributeTypeOptions <em>Attribute Type Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Attribute Type Options</em>'.
	 * @see org.abreslav.grammatic.query.AttributeTypeOptions
	 * @generated
	 */
	EEnum getAttributeTypeOptions();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	QueryFactory getQueryFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.QueryContainerImpl <em>Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.QueryContainerImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getQueryContainer()
		 * @generated
		 */
		EClass QUERY_CONTAINER = eINSTANCE.getQueryContainer();

		/**
		 * The meta object literal for the '<em><b>Query</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY_CONTAINER__QUERY = eINSTANCE.getQueryContainer_Query();

		/**
		 * The meta object literal for the '<em><b>Variable Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY_CONTAINER__VARIABLE_DEFINITIONS = eINSTANCE.getQueryContainer_VariableDefinitions();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.QueryImpl <em>Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.QueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getQuery()
		 * @generated
		 */
		EClass QUERY = eINSTANCE.getQuery();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl <em>Commutative Operation Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getCommutativeOperationQuery()
		 * @generated
		 */
		EClass COMMUTATIVE_OPERATION_QUERY = eINSTANCE.getCommutativeOperationQuery();

		/**
		 * The meta object literal for the '<em><b>Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMUTATIVE_OPERATION_QUERY__DEFINITIONS = eINSTANCE.getCommutativeOperationQuery_Definitions();

		/**
		 * The meta object literal for the '<em><b>Open</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUTATIVE_OPERATION_QUERY__OPEN = eINSTANCE.getCommutativeOperationQuery_Open();

		/**
		 * The meta object literal for the '<em><b>Wildcard Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE = eINSTANCE.getCommutativeOperationQuery_WildcardVariable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.RuleQueryImpl <em>Rule Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.RuleQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getRuleQuery()
		 * @generated
		 */
		EClass RULE_QUERY = eINSTANCE.getRuleQuery();

		/**
		 * The meta object literal for the '<em><b>Symbol</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE_QUERY__SYMBOL = eINSTANCE.getRuleQuery_Symbol();

		/**
		 * The meta object literal for the '<em><b>Symbol Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE_QUERY__SYMBOL_VARIABLE = eINSTANCE.getRuleQuery_SymbolVariable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.SymbolQueryImpl <em>Symbol Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.SymbolQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSymbolQuery()
		 * @generated
		 */
		EClass SYMBOL_QUERY = eINSTANCE.getSymbolQuery();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SYMBOL_QUERY__NAME = eINSTANCE.getSymbolQuery_Name();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.ProductionQueryImpl <em>Production Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.ProductionQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getProductionQuery()
		 * @generated
		 */
		EClass PRODUCTION_QUERY = eINSTANCE.getProductionQuery();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRODUCTION_QUERY__DEFINITION = eINSTANCE.getProductionQuery_Definition();

		/**
		 * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRODUCTION_QUERY__VARIABLE = eINSTANCE.getProductionQuery_Variable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.ExpressionQueryImpl <em>Expression Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.ExpressionQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExpressionQuery()
		 * @generated
		 */
		EClass EXPRESSION_QUERY = eINSTANCE.getExpressionQuery();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.EmptyQueryImpl <em>Empty Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.EmptyQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getEmptyQuery()
		 * @generated
		 */
		EClass EMPTY_QUERY = eINSTANCE.getEmptyQuery();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.SymbolReferenceQueryImpl <em>Symbol Reference Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.SymbolReferenceQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSymbolReferenceQuery()
		 * @generated
		 */
		EClass SYMBOL_REFERENCE_QUERY = eINSTANCE.getSymbolReferenceQuery();

		/**
		 * The meta object literal for the '<em><b>Symbol</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYMBOL_REFERENCE_QUERY__SYMBOL = eINSTANCE.getSymbolReferenceQuery_Symbol();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.ExactExpressionQueryImpl <em>Exact Expression Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.ExactExpressionQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExactExpressionQuery()
		 * @generated
		 */
		EClass EXACT_EXPRESSION_QUERY = eINSTANCE.getExactExpressionQuery();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXACT_EXPRESSION_QUERY__EXPRESSION = eINSTANCE.getExactExpressionQuery_Expression();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.LexicalWildcardQueryImpl <em>Lexical Wildcard Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.LexicalWildcardQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getLexicalWildcardQuery()
		 * @generated
		 */
		EClass LEXICAL_WILDCARD_QUERY = eINSTANCE.getLexicalWildcardQuery();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEXICAL_WILDCARD_QUERY__DEFINITION = eINSTANCE.getLexicalWildcardQuery_Definition();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.VariableDefinitionImpl <em>Variable Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.VariableDefinitionImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getVariableDefinition()
		 * @generated
		 */
		EClass VARIABLE_DEFINITION = eINSTANCE.getVariableDefinition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE_DEFINITION__NAME = eINSTANCE.getVariableDefinition_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_DEFINITION__VALUE = eINSTANCE.getVariableDefinition_Value();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.VariableReferenceImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getVariableReference()
		 * @generated
		 */
		EClass VARIABLE_REFERENCE = eINSTANCE.getVariableReference();

		/**
		 * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_REFERENCE__VARIABLE = eINSTANCE.getVariableReference_Variable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AlternativeQueryImpl <em>Alternative Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AlternativeQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAlternativeQuery()
		 * @generated
		 */
		EClass ALTERNATIVE_QUERY = eINSTANCE.getAlternativeQuery();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.SequenceQueryImpl <em>Sequence Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.SequenceQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSequenceQuery()
		 * @generated
		 */
		EClass SEQUENCE_QUERY = eINSTANCE.getSequenceQuery();

		/**
		 * The meta object literal for the '<em><b>Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE_QUERY__DEFINITIONS = eINSTANCE.getSequenceQuery_Definitions();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.SequenceWildcardImpl <em>Sequence Wildcard</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.SequenceWildcardImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getSequenceWildcard()
		 * @generated
		 */
		EClass SEQUENCE_WILDCARD = eINSTANCE.getSequenceWildcard();

		/**
		 * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE_WILDCARD__VARIABLE = eINSTANCE.getSequenceWildcard_Variable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AlternativeWildcardImpl <em>Alternative Wildcard</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AlternativeWildcardImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAlternativeWildcard()
		 * @generated
		 */
		EClass ALTERNATIVE_WILDCARD = eINSTANCE.getAlternativeWildcard();

		/**
		 * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALTERNATIVE_WILDCARD__VARIABLE = eINSTANCE.getAlternativeWildcard_Variable();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.IterationQueryImpl <em>Iteration Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.IterationQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getIterationQuery()
		 * @generated
		 */
		EClass ITERATION_QUERY = eINSTANCE.getIterationQuery();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ITERATION_QUERY__DEFINITION = eINSTANCE.getIterationQuery_Definition();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITERATION_QUERY__LOWER_BOUND = eINSTANCE.getIterationQuery_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Upper Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITERATION_QUERY__UPPER_BOUND = eINSTANCE.getIterationQuery_UpperBound();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.MetadataQueryImpl <em>Metadata Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.MetadataQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getMetadataQuery()
		 * @generated
		 */
		EClass METADATA_QUERY = eINSTANCE.getMetadataQuery();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference METADATA_QUERY__ATTRIBUTES = eINSTANCE.getMetadataQuery_Attributes();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AttributeQueryImpl <em>Attribute Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AttributeQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeQuery()
		 * @generated
		 */
		EClass ATTRIBUTE_QUERY = eINSTANCE.getAttributeQuery();

		/**
		 * The meta object literal for the '<em><b>Attribute Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_QUERY__ATTRIBUTE_VALUE = eINSTANCE.getAttributeQuery_AttributeValue();

		/**
		 * The meta object literal for the '<em><b>Attribute Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_QUERY__ATTRIBUTE_NAME = eINSTANCE.getAttributeQuery_AttributeName();

		/**
		 * The meta object literal for the '<em><b>Namespace Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_QUERY__NAMESPACE_URI = eINSTANCE.getAttributeQuery_NamespaceUri();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AttributeValueQueryImpl <em>Attribute Value Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AttributeValueQueryImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeValueQuery()
		 * @generated
		 */
		EClass ATTRIBUTE_VALUE_QUERY = eINSTANCE.getAttributeValueQuery();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AttributeTypeImpl <em>Attribute Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AttributeTypeImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeType()
		 * @generated
		 */
		EClass ATTRIBUTE_TYPE = eINSTANCE.getAttributeType();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_TYPE__TYPE = eINSTANCE.getAttributeType_Type();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.AttributePresenceImpl <em>Attribute Presence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.AttributePresenceImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributePresence()
		 * @generated
		 */
		EClass ATTRIBUTE_PRESENCE = eINSTANCE.getAttributePresence();

		/**
		 * The meta object literal for the '<em><b>Present</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_PRESENCE__PRESENT = eINSTANCE.getAttributePresence_Present();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.ExactValueImpl <em>Exact Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.ExactValueImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getExactValue()
		 * @generated
		 */
		EClass EXACT_VALUE = eINSTANCE.getExactValue();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXACT_VALUE__VALUE = eINSTANCE.getExactValue_Value();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.impl.TypedRegExpValueImpl <em>Typed Reg Exp Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.impl.TypedRegExpValueImpl
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getTypedRegExpValue()
		 * @generated
		 */
		EClass TYPED_REG_EXP_VALUE = eINSTANCE.getTypedRegExpValue();

		/**
		 * The meta object literal for the '<em><b>Wild Card Separated Fragments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS = eINSTANCE.getTypedRegExpValue_WildCardSeparatedFragments();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.AttributeTypeOptions <em>Attribute Type Options</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.AttributeTypeOptions
		 * @see org.abreslav.grammatic.query.impl.QueryPackageImpl#getAttributeTypeOptions()
		 * @generated
		 */
		EEnum ATTRIBUTE_TYPE_OPTIONS = eINSTANCE.getAttributeTypeOptions();

	}

} //QueryPackage
