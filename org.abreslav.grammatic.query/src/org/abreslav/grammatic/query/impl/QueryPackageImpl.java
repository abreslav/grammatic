package org.abreslav.grammatic.query.impl;


import org.abreslav.grammatic.grammar.GrammarPackage;
import org.abreslav.grammatic.metadata.MetadataPackage;
import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.AlternativeWildcard;
import org.abreslav.grammatic.query.AttributePresence;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeType;
import org.abreslav.grammatic.query.AttributeTypeOptions;
import org.abreslav.grammatic.query.AttributeValueQuery;
import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExactValue;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.LexicalWildcardQuery;
import org.abreslav.grammatic.query.MetadataQuery;
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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QueryPackageImpl extends EPackageImpl implements QueryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commutativeOperationQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass symbolQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass productionQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emptyQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass symbolReferenceQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exactExpressionQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lexicalWildcardQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variableDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variableReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass alternativeQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sequenceQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sequenceWildcardEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass alternativeWildcardEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iterationQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metadataQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeValueQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributePresenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exactValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typedRegExpValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum attributeTypeOptionsEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.abreslav.grammatic.query.QueryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private QueryPackageImpl() {
		super(eNS_URI, QueryFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static QueryPackage init() {
		if (isInited) return (QueryPackage)EPackage.Registry.INSTANCE.getEPackage(QueryPackage.eNS_URI);

		// Obtain or create and register package
		QueryPackageImpl theQueryPackage = (QueryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof QueryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new QueryPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		GrammarPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theQueryPackage.createPackageContents();

		// Initialize created meta-data
		theQueryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theQueryPackage.freeze();

		return theQueryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQueryContainer() {
		return queryContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQueryContainer_Query() {
		return (EReference)queryContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQueryContainer_VariableDefinitions() {
		return (EReference)queryContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQuery() {
		return queryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommutativeOperationQuery() {
		return commutativeOperationQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommutativeOperationQuery_Definitions() {
		return (EReference)commutativeOperationQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommutativeOperationQuery_Open() {
		return (EAttribute)commutativeOperationQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommutativeOperationQuery_WildcardVariable() {
		return (EReference)commutativeOperationQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuleQuery() {
		return ruleQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuleQuery_Symbol() {
		return (EReference)ruleQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuleQuery_SymbolVariable() {
		return (EReference)ruleQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSymbolQuery() {
		return symbolQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSymbolQuery_Name() {
		return (EAttribute)symbolQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProductionQuery() {
		return productionQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProductionQuery_Definition() {
		return (EReference)productionQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProductionQuery_Variable() {
		return (EReference)productionQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpressionQuery() {
		return expressionQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEmptyQuery() {
		return emptyQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSymbolReferenceQuery() {
		return symbolReferenceQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSymbolReferenceQuery_Symbol() {
		return (EReference)symbolReferenceQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExactExpressionQuery() {
		return exactExpressionQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExactExpressionQuery_Expression() {
		return (EReference)exactExpressionQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLexicalWildcardQuery() {
		return lexicalWildcardQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLexicalWildcardQuery_Definition() {
		return (EReference)lexicalWildcardQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVariableDefinition() {
		return variableDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariableDefinition_Name() {
		return (EAttribute)variableDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVariableDefinition_Value() {
		return (EReference)variableDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVariableReference() {
		return variableReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVariableReference_Variable() {
		return (EReference)variableReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAlternativeQuery() {
		return alternativeQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSequenceQuery() {
		return sequenceQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequenceQuery_Definitions() {
		return (EReference)sequenceQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSequenceWildcard() {
		return sequenceWildcardEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequenceWildcard_Variable() {
		return (EReference)sequenceWildcardEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAlternativeWildcard() {
		return alternativeWildcardEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAlternativeWildcard_Variable() {
		return (EReference)alternativeWildcardEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIterationQuery() {
		return iterationQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIterationQuery_Definition() {
		return (EReference)iterationQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIterationQuery_LowerBound() {
		return (EAttribute)iterationQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIterationQuery_UpperBound() {
		return (EAttribute)iterationQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetadataQuery() {
		return metadataQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetadataQuery_Attributes() {
		return (EReference)metadataQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeQuery() {
		return attributeQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAttributeQuery_AttributeValue() {
		return (EReference)attributeQueryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeQuery_AttributeName() {
		return (EAttribute)attributeQueryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeQuery_NamespaceUri() {
		return (EAttribute)attributeQueryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeValueQuery() {
		return attributeValueQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributeType() {
		return attributeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributeType_Type() {
		return (EAttribute)attributeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttributePresence() {
		return attributePresenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttributePresence_Present() {
		return (EAttribute)attributePresenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExactValue() {
		return exactValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExactValue_Value() {
		return (EReference)exactValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypedRegExpValue() {
		return typedRegExpValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypedRegExpValue_WildCardSeparatedFragments() {
		return (EReference)typedRegExpValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAttributeTypeOptions() {
		return attributeTypeOptionsEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryFactory getQueryFactory() {
		return (QueryFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		queryContainerEClass = createEClass(QUERY_CONTAINER);
		createEReference(queryContainerEClass, QUERY_CONTAINER__QUERY);
		createEReference(queryContainerEClass, QUERY_CONTAINER__VARIABLE_DEFINITIONS);

		queryEClass = createEClass(QUERY);

		commutativeOperationQueryEClass = createEClass(COMMUTATIVE_OPERATION_QUERY);
		createEReference(commutativeOperationQueryEClass, COMMUTATIVE_OPERATION_QUERY__DEFINITIONS);
		createEAttribute(commutativeOperationQueryEClass, COMMUTATIVE_OPERATION_QUERY__OPEN);
		createEReference(commutativeOperationQueryEClass, COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE);

		ruleQueryEClass = createEClass(RULE_QUERY);
		createEReference(ruleQueryEClass, RULE_QUERY__SYMBOL);
		createEReference(ruleQueryEClass, RULE_QUERY__SYMBOL_VARIABLE);

		symbolQueryEClass = createEClass(SYMBOL_QUERY);
		createEAttribute(symbolQueryEClass, SYMBOL_QUERY__NAME);

		productionQueryEClass = createEClass(PRODUCTION_QUERY);
		createEReference(productionQueryEClass, PRODUCTION_QUERY__DEFINITION);
		createEReference(productionQueryEClass, PRODUCTION_QUERY__VARIABLE);

		expressionQueryEClass = createEClass(EXPRESSION_QUERY);

		emptyQueryEClass = createEClass(EMPTY_QUERY);

		symbolReferenceQueryEClass = createEClass(SYMBOL_REFERENCE_QUERY);
		createEReference(symbolReferenceQueryEClass, SYMBOL_REFERENCE_QUERY__SYMBOL);

		exactExpressionQueryEClass = createEClass(EXACT_EXPRESSION_QUERY);
		createEReference(exactExpressionQueryEClass, EXACT_EXPRESSION_QUERY__EXPRESSION);

		lexicalWildcardQueryEClass = createEClass(LEXICAL_WILDCARD_QUERY);
		createEReference(lexicalWildcardQueryEClass, LEXICAL_WILDCARD_QUERY__DEFINITION);

		variableDefinitionEClass = createEClass(VARIABLE_DEFINITION);
		createEAttribute(variableDefinitionEClass, VARIABLE_DEFINITION__NAME);
		createEReference(variableDefinitionEClass, VARIABLE_DEFINITION__VALUE);

		variableReferenceEClass = createEClass(VARIABLE_REFERENCE);
		createEReference(variableReferenceEClass, VARIABLE_REFERENCE__VARIABLE);

		alternativeQueryEClass = createEClass(ALTERNATIVE_QUERY);

		sequenceQueryEClass = createEClass(SEQUENCE_QUERY);
		createEReference(sequenceQueryEClass, SEQUENCE_QUERY__DEFINITIONS);

		sequenceWildcardEClass = createEClass(SEQUENCE_WILDCARD);
		createEReference(sequenceWildcardEClass, SEQUENCE_WILDCARD__VARIABLE);

		alternativeWildcardEClass = createEClass(ALTERNATIVE_WILDCARD);
		createEReference(alternativeWildcardEClass, ALTERNATIVE_WILDCARD__VARIABLE);

		iterationQueryEClass = createEClass(ITERATION_QUERY);
		createEReference(iterationQueryEClass, ITERATION_QUERY__DEFINITION);
		createEAttribute(iterationQueryEClass, ITERATION_QUERY__LOWER_BOUND);
		createEAttribute(iterationQueryEClass, ITERATION_QUERY__UPPER_BOUND);

		metadataQueryEClass = createEClass(METADATA_QUERY);
		createEReference(metadataQueryEClass, METADATA_QUERY__ATTRIBUTES);

		attributeQueryEClass = createEClass(ATTRIBUTE_QUERY);
		createEReference(attributeQueryEClass, ATTRIBUTE_QUERY__ATTRIBUTE_VALUE);
		createEAttribute(attributeQueryEClass, ATTRIBUTE_QUERY__ATTRIBUTE_NAME);
		createEAttribute(attributeQueryEClass, ATTRIBUTE_QUERY__NAMESPACE_URI);

		attributeValueQueryEClass = createEClass(ATTRIBUTE_VALUE_QUERY);

		attributeTypeEClass = createEClass(ATTRIBUTE_TYPE);
		createEAttribute(attributeTypeEClass, ATTRIBUTE_TYPE__TYPE);

		attributePresenceEClass = createEClass(ATTRIBUTE_PRESENCE);
		createEAttribute(attributePresenceEClass, ATTRIBUTE_PRESENCE__PRESENT);

		exactValueEClass = createEClass(EXACT_VALUE);
		createEReference(exactValueEClass, EXACT_VALUE__VALUE);

		typedRegExpValueEClass = createEClass(TYPED_REG_EXP_VALUE);
		createEReference(typedRegExpValueEClass, TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS);

		// Create enums
		attributeTypeOptionsEEnum = createEEnum(ATTRIBUTE_TYPE_OPTIONS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		GrammarPackage theGrammarPackage = (GrammarPackage)EPackage.Registry.INSTANCE.getEPackage(GrammarPackage.eNS_URI);
		MetadataPackage theMetadataPackage = (MetadataPackage)EPackage.Registry.INSTANCE.getEPackage(MetadataPackage.eNS_URI);

		// Create type parameters
		ETypeParameter queryContainerEClass_T = addETypeParameter(queryContainerEClass, "T");
		ETypeParameter commutativeOperationQueryEClass_D = addETypeParameter(commutativeOperationQueryEClass, "D");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getQuery());
		queryContainerEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(this.getQuery());
		commutativeOperationQueryEClass_D.getEBounds().add(g1);

		// Add supertypes to classes
		commutativeOperationQueryEClass.getESuperTypes().add(this.getQuery());
		g1 = createEGenericType(this.getQuery());
		ruleQueryEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getCommutativeOperationQuery());
		EGenericType g2 = createEGenericType(this.getProductionQuery());
		g1.getETypeArguments().add(g2);
		ruleQueryEClass.getEGenericSuperTypes().add(g1);
		symbolQueryEClass.getESuperTypes().add(this.getMetadataQuery());
		productionQueryEClass.getESuperTypes().add(this.getMetadataQuery());
		expressionQueryEClass.getESuperTypes().add(this.getMetadataQuery());
		emptyQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		symbolReferenceQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		exactExpressionQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		lexicalWildcardQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		variableReferenceEClass.getESuperTypes().add(this.getExpressionQuery());
		g1 = createEGenericType(this.getExpressionQuery());
		alternativeQueryEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getCommutativeOperationQuery());
		g2 = createEGenericType(this.getExpressionQuery());
		g1.getETypeArguments().add(g2);
		alternativeQueryEClass.getEGenericSuperTypes().add(g1);
		sequenceQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		sequenceWildcardEClass.getESuperTypes().add(this.getExpressionQuery());
		alternativeWildcardEClass.getESuperTypes().add(this.getQuery());
		iterationQueryEClass.getESuperTypes().add(this.getExpressionQuery());
		metadataQueryEClass.getESuperTypes().add(this.getQuery());
		attributeTypeEClass.getESuperTypes().add(this.getAttributeValueQuery());
		attributePresenceEClass.getESuperTypes().add(this.getAttributeValueQuery());
		exactValueEClass.getESuperTypes().add(this.getAttributeValueQuery());
		typedRegExpValueEClass.getESuperTypes().add(this.getAttributeValueQuery());

		// Initialize classes and features; add operations and parameters
		initEClass(queryContainerEClass, QueryContainer.class, "QueryContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(queryContainerEClass_T);
		initEReference(getQueryContainer_Query(), g1, null, "query", null, 1, 1, QueryContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQueryContainer_VariableDefinitions(), this.getVariableDefinition(), null, "variableDefinitions", null, 0, -1, QueryContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryEClass, Query.class, "Query", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(commutativeOperationQueryEClass, CommutativeOperationQuery.class, "CommutativeOperationQuery", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(commutativeOperationQueryEClass_D);
		initEReference(getCommutativeOperationQuery_Definitions(), g1, null, "definitions", null, 0, -1, CommutativeOperationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommutativeOperationQuery_Open(), ecorePackage.getEBoolean(), "open", null, 0, 1, CommutativeOperationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommutativeOperationQuery_WildcardVariable(), this.getVariableDefinition(), null, "wildcardVariable", null, 0, 1, CommutativeOperationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ruleQueryEClass, RuleQuery.class, "RuleQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRuleQuery_Symbol(), this.getSymbolQuery(), null, "symbol", null, 1, 1, RuleQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRuleQuery_SymbolVariable(), this.getVariableDefinition(), null, "symbolVariable", null, 0, 1, RuleQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(symbolQueryEClass, SymbolQuery.class, "SymbolQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSymbolQuery_Name(), ecorePackage.getEString(), "name", null, 0, 1, SymbolQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(productionQueryEClass, ProductionQuery.class, "ProductionQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProductionQuery_Definition(), this.getExpressionQuery(), null, "definition", null, 0, 1, ProductionQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProductionQuery_Variable(), this.getVariableDefinition(), null, "variable", null, 0, 1, ProductionQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(expressionQueryEClass, ExpressionQuery.class, "ExpressionQuery", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(emptyQueryEClass, EmptyQuery.class, "EmptyQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(symbolReferenceQueryEClass, SymbolReferenceQuery.class, "SymbolReferenceQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSymbolReferenceQuery_Symbol(), this.getSymbolQuery(), null, "symbol", null, 1, 1, SymbolReferenceQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exactExpressionQueryEClass, ExactExpressionQuery.class, "ExactExpressionQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExactExpressionQuery_Expression(), theGrammarPackage.getExpression(), null, "expression", null, 1, 1, ExactExpressionQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lexicalWildcardQueryEClass, LexicalWildcardQuery.class, "LexicalWildcardQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLexicalWildcardQuery_Definition(), theGrammarPackage.getLexicalDefinition(), null, "definition", null, 0, 1, LexicalWildcardQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(variableDefinitionEClass, VariableDefinition.class, "VariableDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVariableDefinition_Name(), ecorePackage.getEString(), "name", null, 0, 1, VariableDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVariableDefinition_Value(), this.getQuery(), null, "value", null, 1, 1, VariableDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(variableReferenceEClass, VariableReference.class, "VariableReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVariableReference_Variable(), this.getVariableDefinition(), null, "variable", null, 0, 1, VariableReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(alternativeQueryEClass, AlternativeQuery.class, "AlternativeQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(sequenceQueryEClass, SequenceQuery.class, "SequenceQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSequenceQuery_Definitions(), this.getExpressionQuery(), null, "definitions", null, 0, -1, SequenceQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sequenceWildcardEClass, SequenceWildcard.class, "SequenceWildcard", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSequenceWildcard_Variable(), this.getVariableDefinition(), null, "variable", null, 0, 1, SequenceWildcard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(alternativeWildcardEClass, AlternativeWildcard.class, "AlternativeWildcard", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAlternativeWildcard_Variable(), this.getVariableDefinition(), null, "variable", null, 0, 1, AlternativeWildcard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iterationQueryEClass, IterationQuery.class, "IterationQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIterationQuery_Definition(), this.getExpressionQuery(), null, "definition", null, 1, 1, IterationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIterationQuery_LowerBound(), ecorePackage.getEInt(), "lowerBound", null, 0, 1, IterationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIterationQuery_UpperBound(), ecorePackage.getEInt(), "upperBound", null, 0, 1, IterationQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metadataQueryEClass, MetadataQuery.class, "MetadataQuery", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMetadataQuery_Attributes(), this.getAttributeQuery(), null, "attributes", null, 0, -1, MetadataQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeQueryEClass, AttributeQuery.class, "AttributeQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAttributeQuery_AttributeValue(), this.getAttributeValueQuery(), null, "attributeValue", null, 1, 1, AttributeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttributeQuery_AttributeName(), ecorePackage.getEString(), "attributeName", null, 0, 1, AttributeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttributeQuery_NamespaceUri(), ecorePackage.getEString(), "namespaceUri", null, 0, 1, AttributeQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeValueQueryEClass, AttributeValueQuery.class, "AttributeValueQuery", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(attributeTypeEClass, AttributeType.class, "AttributeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttributeType_Type(), this.getAttributeTypeOptions(), "type", null, 0, 1, AttributeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributePresenceEClass, AttributePresence.class, "AttributePresence", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttributePresence_Present(), ecorePackage.getEBoolean(), "present", "true", 0, 1, AttributePresence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exactValueEClass, ExactValue.class, "ExactValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExactValue_Value(), theMetadataPackage.getValue(), null, "value", null, 1, 1, ExactValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typedRegExpValueEClass, TypedRegExpValue.class, "TypedRegExpValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTypedRegExpValue_WildCardSeparatedFragments(), theMetadataPackage.getValue(), null, "wildCardSeparatedFragments", null, 0, -1, TypedRegExpValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(attributeTypeOptionsEEnum, AttributeTypeOptions.class, "AttributeTypeOptions");
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.STRING);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.INTEGER);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.ID);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.PUNCTUATION);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.TUPLE);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.MULTI);
		addEEnumLiteral(attributeTypeOptionsEEnum, AttributeTypeOptions.EXPRESSION);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// my
		createMyAnnotations();
	}

	/**
	 * Initializes the annotations for <b>my</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createMyAnnotations() {
		String source = "my";		
		addAnnotation
		  (productionQueryEClass, 
		   source, 
		   new String[] {
			 "label", "definition._label()"
		   });		
		addAnnotation
		  (emptyQueryEClass, 
		   source, 
		   new String[] {
			 "label", "\'#empty\'"
		   });		
		addAnnotation
		  (symbolReferenceQueryEClass, 
		   source, 
		   new String[] {
			 "label", "symbol._label()"
		   });
	}

} //QueryPackageImpl