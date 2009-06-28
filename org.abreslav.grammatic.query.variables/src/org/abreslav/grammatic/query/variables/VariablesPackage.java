/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.variables;

import org.abreslav.grammatic.grammar.GrammarPackage;
import org.abreslav.grammatic.metadata.MetadataPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

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
 * @see org.abreslav.grammatic.query.variables.VariablesFactory
 * @generated
 */
public class VariablesPackage extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "variables";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "http://www.abreslav.org/Grammatic/2008/Query/Variables";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "grammatic-query-variables";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final VariablesPackage eINSTANCE = org.abreslav.grammatic.query.variables.VariablesPackage.init();

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.VariableValue <em>Variable Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.VariableValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getVariableValue()
	 * @generated
	 */
	public static final int VARIABLE_VALUE = 0;

	/**
	 * The number of structural features of the '<em>Variable Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int VARIABLE_VALUE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.ItemValue <em>Item Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.ItemValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getItemValue()
	 * @generated
	 */
	public static final int ITEM_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Item</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ITEM_VALUE__ITEM = VARIABLE_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Item Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ITEM_VALUE_FEATURE_COUNT = VARIABLE_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.ListValue <em>List Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.ListValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getListValue()
	 * @generated
	 */
	public static final int LIST_VALUE = 2;

	/**
	 * The feature id for the '<em><b>Items</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LIST_VALUE__ITEMS = VARIABLE_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>List Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LIST_VALUE_FEATURE_COUNT = VARIABLE_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.SequencePartValue <em>Sequence Part Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.SequencePartValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getSequencePartValue()
	 * @generated
	 */
	public static final int SEQUENCE_PART_VALUE = 3;

	/**
	 * The feature id for the '<em><b>Items</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SEQUENCE_PART_VALUE__ITEMS = LIST_VALUE__ITEMS;

	/**
	 * The number of structural features of the '<em>Sequence Part Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SEQUENCE_PART_VALUE_FEATURE_COUNT = LIST_VALUE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.AlternativePartValue <em>Alternative Part Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.AlternativePartValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getAlternativePartValue()
	 * @generated
	 */
	public static final int ALTERNATIVE_PART_VALUE = 4;

	/**
	 * The feature id for the '<em><b>Items</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ALTERNATIVE_PART_VALUE__ITEMS = LIST_VALUE__ITEMS;

	/**
	 * The number of structural features of the '<em>Alternative Part Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ALTERNATIVE_PART_VALUE_FEATURE_COUNT = LIST_VALUE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.RulePartValue <em>Rule Part Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.RulePartValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getRulePartValue()
	 * @generated
	 */
	public static final int RULE_PART_VALUE = 5;

	/**
	 * The feature id for the '<em><b>Items</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RULE_PART_VALUE__ITEMS = LIST_VALUE__ITEMS;

	/**
	 * The number of structural features of the '<em>Rule Part Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RULE_PART_VALUE_FEATURE_COUNT = LIST_VALUE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.ExpressionValue <em>Expression Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.ExpressionValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getExpressionValue()
	 * @generated
	 */
	public static final int EXPRESSION_VALUE = 6;

	/**
	 * The feature id for the '<em><b>Item</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EXPRESSION_VALUE__ITEM = ITEM_VALUE__ITEM;

	/**
	 * The number of structural features of the '<em>Expression Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EXPRESSION_VALUE_FEATURE_COUNT = ITEM_VALUE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.ProductionValue <em>Production Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.ProductionValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getProductionValue()
	 * @generated
	 */
	public static final int PRODUCTION_VALUE = 7;

	/**
	 * The feature id for the '<em><b>Item</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int PRODUCTION_VALUE__ITEM = ITEM_VALUE__ITEM;

	/**
	 * The number of structural features of the '<em>Production Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int PRODUCTION_VALUE_FEATURE_COUNT = ITEM_VALUE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.abreslav.grammatic.query.variables.SymbolValue <em>Symbol Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.abreslav.grammatic.query.variables.SymbolValue
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getSymbolValue()
	 * @generated
	 */
	public static final int SYMBOL_VALUE = 8;

	/**
	 * The feature id for the '<em><b>Item</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SYMBOL_VALUE__ITEM = ITEM_VALUE__ITEM;

	/**
	 * The number of structural features of the '<em>Symbol Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SYMBOL_VALUE_FEATURE_COUNT = ITEM_VALUE_FEATURE_COUNT + 0;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variableValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass itemValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sequencePartValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass alternativePartValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rulePartValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass productionValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass symbolValueEClass = null;

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
	 * @see org.abreslav.grammatic.query.variables.VariablesPackage#eNS_URI
	 * @see #init()
	 * @generated NOT
	 */
	private VariablesPackage() {
		super(eNS_URI, VariablesFactory.INSTANCE);
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
	public static VariablesPackage init() {
		if (isInited) return (VariablesPackage)EPackage.Registry.INSTANCE.getEPackage(VariablesPackage.eNS_URI);

		// Obtain or create and register package
		VariablesPackage theVariablesPackage = (VariablesPackage)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof VariablesPackage ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new VariablesPackage());

		isInited = true;

		// Initialize simple dependencies
		GrammarPackage.eINSTANCE.eClass();
		MetadataPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theVariablesPackage.createPackageContents();

		// Initialize created meta-data
		theVariablesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theVariablesPackage.freeze();

		return theVariablesPackage;
	}


	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.VariableValue <em>Variable Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.VariableValue
	 * @generated
	 */
	public EClass getVariableValue() {
		return variableValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.ItemValue <em>Item Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Item Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.ItemValue
	 * @generated
	 */
	public EClass getItemValue() {
		return itemValueEClass;
	}

	/**
	 * Returns the meta object for the reference '{@link org.abreslav.grammatic.query.variables.ItemValue#getItem <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Item</em>'.
	 * @see org.abreslav.grammatic.query.variables.ItemValue#getItem()
	 * @see #getItemValue()
	 * @generated
	 */
	public EReference getItemValue_Item() {
		return (EReference)itemValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.ListValue <em>List Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>List Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.ListValue
	 * @generated
	 */
	public EClass getListValue() {
		return listValueEClass;
	}

	/**
	 * Returns the meta object for the reference list '{@link org.abreslav.grammatic.query.variables.ListValue#getItems <em>Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Items</em>'.
	 * @see org.abreslav.grammatic.query.variables.ListValue#getItems()
	 * @see #getListValue()
	 * @generated
	 */
	public EReference getListValue_Items() {
		return (EReference)listValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.SequencePartValue <em>Sequence Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence Part Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.SequencePartValue
	 * @generated
	 */
	public EClass getSequencePartValue() {
		return sequencePartValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.AlternativePartValue <em>Alternative Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alternative Part Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.AlternativePartValue
	 * @generated
	 */
	public EClass getAlternativePartValue() {
		return alternativePartValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.RulePartValue <em>Rule Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Part Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.RulePartValue
	 * @generated
	 */
	public EClass getRulePartValue() {
		return rulePartValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.ExpressionValue <em>Expression Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.ExpressionValue
	 * @generated
	 */
	public EClass getExpressionValue() {
		return expressionValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.ProductionValue <em>Production Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Production Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.ProductionValue
	 * @generated
	 */
	public EClass getProductionValue() {
		return productionValueEClass;
	}

	/**
	 * Returns the meta object for class '{@link org.abreslav.grammatic.query.variables.SymbolValue <em>Symbol Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Symbol Value</em>'.
	 * @see org.abreslav.grammatic.query.variables.SymbolValue
	 * @generated
	 */
	public EClass getSymbolValue() {
		return symbolValueEClass;
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public VariablesFactory getVariablesFactory() {
		return (VariablesFactory)getEFactoryInstance();
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
		variableValueEClass = createEClass(VARIABLE_VALUE);

		itemValueEClass = createEClass(ITEM_VALUE);
		createEReference(itemValueEClass, ITEM_VALUE__ITEM);

		listValueEClass = createEClass(LIST_VALUE);
		createEReference(listValueEClass, LIST_VALUE__ITEMS);

		sequencePartValueEClass = createEClass(SEQUENCE_PART_VALUE);

		alternativePartValueEClass = createEClass(ALTERNATIVE_PART_VALUE);

		rulePartValueEClass = createEClass(RULE_PART_VALUE);

		expressionValueEClass = createEClass(EXPRESSION_VALUE);

		productionValueEClass = createEClass(PRODUCTION_VALUE);

		symbolValueEClass = createEClass(SYMBOL_VALUE);
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
	 * @generated NOT
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

		// Create type parameters
		ETypeParameter itemValueEClass_T = addETypeParameter(itemValueEClass, "T");
		ETypeParameter listValueEClass_T = addETypeParameter(listValueEClass, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		itemValueEClass.getESuperTypes().add(this.getVariableValue());
		listValueEClass.getESuperTypes().add(this.getVariableValue());
		EGenericType g1 = createEGenericType(this.getListValue());
		EGenericType g2 = createEGenericType(theGrammarPackage.getExpression());
		g1.getETypeArguments().add(g2);
		sequencePartValueEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getListValue());
		g2 = createEGenericType(theGrammarPackage.getExpression());
		g1.getETypeArguments().add(g2);
		alternativePartValueEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getListValue());
		g2 = createEGenericType(theGrammarPackage.getProduction());
		g1.getETypeArguments().add(g2);
		rulePartValueEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getItemValue());
		g2 = createEGenericType(theGrammarPackage.getExpression());
		g1.getETypeArguments().add(g2);
		expressionValueEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getItemValue());
		g2 = createEGenericType(theGrammarPackage.getProduction());
		g1.getETypeArguments().add(g2);
		productionValueEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getItemValue());
		g2 = createEGenericType(theGrammarPackage.getSymbol());
		g1.getETypeArguments().add(g2);
		symbolValueEClass.getEGenericSuperTypes().add(g1);

		// Initialize classes and features; add operations and parameters
		initEClass(variableValueEClass, VariableValue.class, "VariableValue", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(itemValueEClass, ItemValue.class, "ItemValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(itemValueEClass_T);
		initEReference(getItemValue_Item(), g1, null, "item", null, 1, 1, ItemValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listValueEClass, ListValue.class, "ListValue", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(listValueEClass_T);
		initEReference(getListValue_Items(), g1, null, "items", null, 0, -1, ListValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sequencePartValueEClass, SequencePartValue.class, "SequencePartValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(alternativePartValueEClass, AlternativePartValue.class, "AlternativePartValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(rulePartValueEClass, RulePartValue.class, "RulePartValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(expressionValueEClass, ExpressionValue.class, "ExpressionValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(productionValueEClass, ProductionValue.class, "ProductionValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(symbolValueEClass, SymbolValue.class, "SymbolValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

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
	public interface Literals {
		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.VariableValue <em>Variable Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.VariableValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getVariableValue()
		 * @generated
		 */
		public static final EClass VARIABLE_VALUE = eINSTANCE.getVariableValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.ItemValue <em>Item Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.ItemValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getItemValue()
		 * @generated
		 */
		public static final EClass ITEM_VALUE = eINSTANCE.getItemValue();

		/**
		 * The meta object literal for the '<em><b>Item</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference ITEM_VALUE__ITEM = eINSTANCE.getItemValue_Item();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.ListValue <em>List Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.ListValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getListValue()
		 * @generated
		 */
		public static final EClass LIST_VALUE = eINSTANCE.getListValue();

		/**
		 * The meta object literal for the '<em><b>Items</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference LIST_VALUE__ITEMS = eINSTANCE.getListValue_Items();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.SequencePartValue <em>Sequence Part Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.SequencePartValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getSequencePartValue()
		 * @generated
		 */
		public static final EClass SEQUENCE_PART_VALUE = eINSTANCE.getSequencePartValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.AlternativePartValue <em>Alternative Part Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.AlternativePartValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getAlternativePartValue()
		 * @generated
		 */
		public static final EClass ALTERNATIVE_PART_VALUE = eINSTANCE.getAlternativePartValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.RulePartValue <em>Rule Part Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.RulePartValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getRulePartValue()
		 * @generated
		 */
		public static final EClass RULE_PART_VALUE = eINSTANCE.getRulePartValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.ExpressionValue <em>Expression Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.ExpressionValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getExpressionValue()
		 * @generated
		 */
		public static final EClass EXPRESSION_VALUE = eINSTANCE.getExpressionValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.ProductionValue <em>Production Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.ProductionValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getProductionValue()
		 * @generated
		 */
		public static final EClass PRODUCTION_VALUE = eINSTANCE.getProductionValue();

		/**
		 * The meta object literal for the '{@link org.abreslav.grammatic.query.variables.SymbolValue <em>Symbol Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.abreslav.grammatic.query.variables.SymbolValue
		 * @see org.abreslav.grammatic.query.variables.VariablesPackage#getSymbolValue()
		 * @generated
		 */
		public static final EClass SYMBOL_VALUE = eINSTANCE.getSymbolValue();

	}

} //VariablesPackage
