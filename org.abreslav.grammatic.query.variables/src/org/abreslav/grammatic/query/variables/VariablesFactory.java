/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.variables;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @generated
 */
public class VariablesFactory extends EFactoryImpl {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final VariablesFactory eINSTANCE = init();

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final VariablesFactory INSTANCE = org.abreslav.grammatic.query.variables.VariablesFactory.eINSTANCE;

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static VariablesFactory init() {
		try {
			VariablesFactory theVariablesFactory = (VariablesFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.abreslav.org/Grammatic/2008/Query/Variables"); 
			if (theVariablesFactory != null) {
				return theVariablesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new VariablesFactory();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariablesFactory() {
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
			case VariablesPackage.ITEM_VALUE: return createItemValue();
			case VariablesPackage.SEQUENCE_PART_VALUE: return createSequencePartValue();
			case VariablesPackage.ALTERNATIVE_PART_VALUE: return createAlternativePartValue();
			case VariablesPackage.RULE_PART_VALUE: return createRulePartValue();
			case VariablesPackage.EXPRESSION_VALUE: return createExpressionValue();
			case VariablesPackage.PRODUCTION_VALUE: return createProductionValue();
			case VariablesPackage.SYMBOL_VALUE: return createSymbolValue();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T> ItemValue<T> createItemValue() {
		ItemValue<T> itemValue = new ItemValue<T>();
		return itemValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SequencePartValue createSequencePartValue() {
		SequencePartValue sequencePartValue = new SequencePartValue();
		return sequencePartValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlternativePartValue createAlternativePartValue() {
		AlternativePartValue alternativePartValue = new AlternativePartValue();
		return alternativePartValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RulePartValue createRulePartValue() {
		RulePartValue rulePartValue = new RulePartValue();
		return rulePartValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExpressionValue createExpressionValue() {
		ExpressionValue expressionValue = new ExpressionValue();
		return expressionValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProductionValue createProductionValue() {
		ProductionValue productionValue = new ProductionValue();
		return productionValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymbolValue createSymbolValue() {
		SymbolValue symbolValue = new SymbolValue();
		return symbolValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariablesPackage getVariablesPackage() {
		return (VariablesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static VariablesPackage getPackage() {
		return VariablesPackage.eINSTANCE;
	}

} //VariablesFactory
