/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.variables.util;

import java.util.List;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.abreslav.grammatic.query.variables.VariablesPackage
 * @generated
 */
public class VariablesSwitch<R, D> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static VariablesPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariablesSwitch() {
		if (modelPackage == null) {
			modelPackage = VariablesPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public R doSwitch(EObject theEObject, D data) {
		return doSwitch(theEObject.eClass(), theEObject, data);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected R doSwitch(EClass theEClass, EObject theEObject, D data) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject, data);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject, data) :
					doSwitch(eSuperTypes.get(0), theEObject, data);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected R doSwitch(int classifierID, EObject theEObject, D data) {
		switch (classifierID) {
			case VariablesPackage.VARIABLE_VALUE: {
				VariableValue variableValue = (VariableValue)theEObject;
				R result = caseVariableValue(variableValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.ITEM_VALUE: {
				ItemValue<?> itemValue = (ItemValue<?>)theEObject;
				R result = caseItemValue(itemValue, data);
				if (result == null) result = caseVariableValue(itemValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.LIST_VALUE: {
				ListValue<?> listValue = (ListValue<?>)theEObject;
				R result = caseListValue(listValue, data);
				if (result == null) result = caseVariableValue(listValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.SEQUENCE_PART_VALUE: {
				SequencePartValue sequencePartValue = (SequencePartValue)theEObject;
				R result = caseSequencePartValue(sequencePartValue, data);
				if (result == null) result = caseListValue(sequencePartValue, data);
				if (result == null) result = caseVariableValue(sequencePartValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.ALTERNATIVE_PART_VALUE: {
				AlternativePartValue alternativePartValue = (AlternativePartValue)theEObject;
				R result = caseAlternativePartValue(alternativePartValue, data);
				if (result == null) result = caseListValue(alternativePartValue, data);
				if (result == null) result = caseVariableValue(alternativePartValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.RULE_PART_VALUE: {
				RulePartValue rulePartValue = (RulePartValue)theEObject;
				R result = caseRulePartValue(rulePartValue, data);
				if (result == null) result = caseListValue(rulePartValue, data);
				if (result == null) result = caseVariableValue(rulePartValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.EXPRESSION_VALUE: {
				ExpressionValue expressionValue = (ExpressionValue)theEObject;
				R result = caseExpressionValue(expressionValue, data);
				if (result == null) result = caseItemValue(expressionValue, data);
				if (result == null) result = caseVariableValue(expressionValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.PRODUCTION_VALUE: {
				ProductionValue productionValue = (ProductionValue)theEObject;
				R result = caseProductionValue(productionValue, data);
				if (result == null) result = caseItemValue(productionValue, data);
				if (result == null) result = caseVariableValue(productionValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			case VariablesPackage.SYMBOL_VALUE: {
				SymbolValue symbolValue = (SymbolValue)theEObject;
				R result = caseSymbolValue(symbolValue, data);
				if (result == null) result = caseItemValue(symbolValue, data);
				if (result == null) result = caseVariableValue(symbolValue, data);
				if (result == null) result = defaultCase(theEObject, data);
				return result;
			}
			default: return defaultCase(theEObject, data);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseVariableValue(VariableValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Item Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> R caseItemValue(ItemValue<T> object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> R caseListValue(ListValue<T> object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence Part Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence Part Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSequencePartValue(SequencePartValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alternative Part Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alternative Part Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseAlternativePartValue(AlternativePartValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule Part Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule Part Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseRulePartValue(RulePartValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Expression Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseExpressionValue(ExpressionValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Production Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Production Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseProductionValue(ProductionValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Symbol Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Symbol Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public R caseSymbolValue(SymbolValue object, D data) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public R defaultCase(EObject object, D data) {
		return null;
	}

} //VariablesSwitch
