package org.abreslav.grammatic.query.impl;


import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.AlternativeWildcard;
import org.abreslav.grammatic.query.AttributePresence;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeType;
import org.abreslav.grammatic.query.AttributeTypeOptions;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExactValue;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.LexicalWildcardQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SequenceQuery;
import org.abreslav.grammatic.query.SequenceWildcard;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.abreslav.grammatic.query.TypedRegExpValue;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QueryFactoryImpl extends EFactoryImpl implements QueryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static QueryFactory init() {
		try {
			QueryFactory theQueryFactory = (QueryFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.abreslav.org/Grammatic/2008/Query"); 
			if (theQueryFactory != null) {
				return theQueryFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new QueryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case QueryPackage.QUERY_CONTAINER: return createQueryContainer();
			case QueryPackage.RULE_QUERY: return createRuleQuery();
			case QueryPackage.SYMBOL_QUERY: return createSymbolQuery();
			case QueryPackage.PRODUCTION_QUERY: return createProductionQuery();
			case QueryPackage.EMPTY_QUERY: return createEmptyQuery();
			case QueryPackage.SYMBOL_REFERENCE_QUERY: return createSymbolReferenceQuery();
			case QueryPackage.EXACT_EXPRESSION_QUERY: return createExactExpressionQuery();
			case QueryPackage.LEXICAL_WILDCARD_QUERY: return createLexicalWildcardQuery();
			case QueryPackage.VARIABLE_DEFINITION: return createVariableDefinition();
			case QueryPackage.VARIABLE_REFERENCE: return createVariableReference();
			case QueryPackage.ALTERNATIVE_QUERY: return createAlternativeQuery();
			case QueryPackage.SEQUENCE_QUERY: return createSequenceQuery();
			case QueryPackage.SEQUENCE_WILDCARD: return createSequenceWildcard();
			case QueryPackage.ALTERNATIVE_WILDCARD: return createAlternativeWildcard();
			case QueryPackage.ITERATION_QUERY: return createIterationQuery();
			case QueryPackage.ATTRIBUTE_QUERY: return createAttributeQuery();
			case QueryPackage.ATTRIBUTE_TYPE: return createAttributeType();
			case QueryPackage.ATTRIBUTE_PRESENCE: return createAttributePresence();
			case QueryPackage.EXACT_VALUE: return createExactValue();
			case QueryPackage.TYPED_REG_EXP_VALUE: return createTypedRegExpValue();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case QueryPackage.ATTRIBUTE_TYPE_OPTIONS:
				return createAttributeTypeOptionsFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case QueryPackage.ATTRIBUTE_TYPE_OPTIONS:
				return convertAttributeTypeOptionsToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T extends Query> QueryContainer<T> createQueryContainer() {
		QueryContainerImpl<T> queryContainer = new QueryContainerImpl<T>();
		return queryContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuleQuery createRuleQuery() {
		RuleQueryImpl ruleQuery = new RuleQueryImpl();
		return ruleQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymbolQuery createSymbolQuery() {
		SymbolQueryImpl symbolQuery = new SymbolQueryImpl();
		return symbolQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProductionQuery createProductionQuery() {
		ProductionQueryImpl productionQuery = new ProductionQueryImpl();
		return productionQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmptyQuery createEmptyQuery() {
		EmptyQueryImpl emptyQuery = new EmptyQueryImpl();
		return emptyQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymbolReferenceQuery createSymbolReferenceQuery() {
		SymbolReferenceQueryImpl symbolReferenceQuery = new SymbolReferenceQueryImpl();
		return symbolReferenceQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExactExpressionQuery createExactExpressionQuery() {
		ExactExpressionQueryImpl exactExpressionQuery = new ExactExpressionQueryImpl();
		return exactExpressionQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LexicalWildcardQuery createLexicalWildcardQuery() {
		LexicalWildcardQueryImpl lexicalWildcardQuery = new LexicalWildcardQueryImpl();
		return lexicalWildcardQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableDefinition createVariableDefinition() {
		VariableDefinitionImpl variableDefinition = new VariableDefinitionImpl();
		return variableDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableReference createVariableReference() {
		VariableReferenceImpl variableReference = new VariableReferenceImpl();
		return variableReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlternativeQuery createAlternativeQuery() {
		AlternativeQueryImpl alternativeQuery = new AlternativeQueryImpl();
		return alternativeQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SequenceQuery createSequenceQuery() {
		SequenceQueryImpl sequenceQuery = new SequenceQueryImpl();
		return sequenceQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SequenceWildcard createSequenceWildcard() {
		SequenceWildcardImpl sequenceWildcard = new SequenceWildcardImpl();
		return sequenceWildcard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlternativeWildcard createAlternativeWildcard() {
		AlternativeWildcardImpl alternativeWildcard = new AlternativeWildcardImpl();
		return alternativeWildcard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IterationQuery createIterationQuery() {
		IterationQueryImpl iterationQuery = new IterationQueryImpl();
		return iterationQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeQuery createAttributeQuery() {
		AttributeQueryImpl attributeQuery = new AttributeQueryImpl();
		return attributeQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeType createAttributeType() {
		AttributeTypeImpl attributeType = new AttributeTypeImpl();
		return attributeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributePresence createAttributePresence() {
		AttributePresenceImpl attributePresence = new AttributePresenceImpl();
		return attributePresence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExactValue createExactValue() {
		ExactValueImpl exactValue = new ExactValueImpl();
		return exactValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypedRegExpValue createTypedRegExpValue() {
		TypedRegExpValueImpl typedRegExpValue = new TypedRegExpValueImpl();
		return typedRegExpValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeTypeOptions createAttributeTypeOptionsFromString(EDataType eDataType, String initialValue) {
		AttributeTypeOptions result = AttributeTypeOptions.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAttributeTypeOptionsToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryPackage getQueryPackage() {
		return (QueryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static QueryPackage getPackage() {
		return QueryPackage.eINSTANCE;
	}

} //QueryFactoryImpl