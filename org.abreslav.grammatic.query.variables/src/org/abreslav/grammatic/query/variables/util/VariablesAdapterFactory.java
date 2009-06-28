/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.variables.util;

import org.abreslav.grammatic.query.variables.AlternativePartValue;
import org.abreslav.grammatic.query.variables.ExpressionValue;
import org.abreslav.grammatic.query.variables.ItemValue;
import org.abreslav.grammatic.query.variables.ListValue;
import org.abreslav.grammatic.query.variables.ProductionValue;
import org.abreslav.grammatic.query.variables.RulePartValue;
import org.abreslav.grammatic.query.variables.SequencePartValue;
import org.abreslav.grammatic.query.variables.SymbolValue;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.abreslav.grammatic.query.variables.VariablesPackage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.variables.VariablesPackage
 * @generated
 */
public class VariablesAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static VariablesPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariablesAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = VariablesPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariablesSwitch<Adapter, Void> modelSwitch =
		new VariablesSwitch<Adapter, Void>() {
			@Override
			public Adapter caseVariableValue(VariableValue object, Void data) {
				return createVariableValueAdapter();
			}
			@Override
			public <T> Adapter caseItemValue(ItemValue<T> object, Void data) {
				return createItemValueAdapter();
			}
			@Override
			public <T> Adapter caseListValue(ListValue<T> object, Void data) {
				return createListValueAdapter();
			}
			@Override
			public Adapter caseSequencePartValue(SequencePartValue object, Void data) {
				return createSequencePartValueAdapter();
			}
			@Override
			public Adapter caseAlternativePartValue(AlternativePartValue object, Void data) {
				return createAlternativePartValueAdapter();
			}
			@Override
			public Adapter caseRulePartValue(RulePartValue object, Void data) {
				return createRulePartValueAdapter();
			}
			@Override
			public Adapter caseExpressionValue(ExpressionValue object, Void data) {
				return createExpressionValueAdapter();
			}
			@Override
			public Adapter caseProductionValue(ProductionValue object, Void data) {
				return createProductionValueAdapter();
			}
			@Override
			public Adapter caseSymbolValue(SymbolValue object, Void data) {
				return createSymbolValueAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object, Void data) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target, null);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.VariableValue <em>Variable Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.VariableValue
	 * @generated
	 */
	public Adapter createVariableValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.ItemValue <em>Item Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.ItemValue
	 * @generated
	 */
	public Adapter createItemValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.ListValue <em>List Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.ListValue
	 * @generated
	 */
	public Adapter createListValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.SequencePartValue <em>Sequence Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.SequencePartValue
	 * @generated
	 */
	public Adapter createSequencePartValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.AlternativePartValue <em>Alternative Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.AlternativePartValue
	 * @generated
	 */
	public Adapter createAlternativePartValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.RulePartValue <em>Rule Part Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.RulePartValue
	 * @generated
	 */
	public Adapter createRulePartValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.ExpressionValue <em>Expression Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.ExpressionValue
	 * @generated
	 */
	public Adapter createExpressionValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.ProductionValue <em>Production Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.ProductionValue
	 * @generated
	 */
	public Adapter createProductionValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.abreslav.grammatic.query.variables.SymbolValue <em>Symbol Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.abreslav.grammatic.query.variables.SymbolValue
	 * @generated
	 */
	public Adapter createSymbolValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //VariablesAdapterFactory
