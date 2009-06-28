/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import java.util.Collection;

import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.RuleQueryImpl#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.RuleQueryImpl#isOpen <em>Open</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.RuleQueryImpl#getWildcardVariable <em>Wildcard Variable</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.RuleQueryImpl#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.RuleQueryImpl#getSymbolVariable <em>Symbol Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RuleQueryImpl extends QueryImpl implements RuleQuery {
	/**
	 * The cached value of the '{@link #getDefinitions() <em>Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<ProductionQuery> definitions;

	/**
	 * The default value of the '{@link #isOpen() <em>Open</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpen()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOpen() <em>Open</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpen()
	 * @generated
	 * @ordered
	 */
	protected boolean open = OPEN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getWildcardVariable() <em>Wildcard Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWildcardVariable()
	 * @generated
	 * @ordered
	 */
	protected VariableDefinition wildcardVariable;

	/**
	 * The cached value of the '{@link #getSymbol() <em>Symbol</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbol()
	 * @generated
	 * @ordered
	 */
	protected SymbolQuery symbol;

	/**
	 * The cached value of the '{@link #getSymbolVariable() <em>Symbol Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbolVariable()
	 * @generated
	 * @ordered
	 */
	protected VariableDefinition symbolVariable;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuleQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.RULE_QUERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProductionQuery> getDefinitions() {
		if (definitions == null) {
			definitions = new EObjectContainmentEList<ProductionQuery>(Query.class, this, QueryPackage.RULE_QUERY__DEFINITIONS);
		}
		return definitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SymbolQuery getSymbol() {
		return symbol;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSymbol(SymbolQuery newSymbol, NotificationChain msgs) {
		SymbolQuery oldSymbol = symbol;
		symbol = newSymbol;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.RULE_QUERY__SYMBOL, oldSymbol, newSymbol);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSymbol(SymbolQuery newSymbol) {
		if (newSymbol != symbol) {
			NotificationChain msgs = null;
			if (symbol != null)
				msgs = ((InternalEObject)symbol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.RULE_QUERY__SYMBOL, null, msgs);
			if (newSymbol != null)
				msgs = ((InternalEObject)newSymbol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.RULE_QUERY__SYMBOL, null, msgs);
			msgs = basicSetSymbol(newSymbol, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.RULE_QUERY__SYMBOL, newSymbol, newSymbol));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableDefinition getSymbolVariable() {
		if (symbolVariable != null && symbolVariable.eIsProxy()) {
			InternalEObject oldSymbolVariable = (InternalEObject)symbolVariable;
			symbolVariable = (VariableDefinition)eResolveProxy(oldSymbolVariable);
			if (symbolVariable != oldSymbolVariable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QueryPackage.RULE_QUERY__SYMBOL_VARIABLE, oldSymbolVariable, symbolVariable));
			}
		}
		return symbolVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableDefinition basicGetSymbolVariable() {
		return symbolVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSymbolVariable(VariableDefinition newSymbolVariable) {
		VariableDefinition oldSymbolVariable = symbolVariable;
		symbolVariable = newSymbolVariable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.RULE_QUERY__SYMBOL_VARIABLE, oldSymbolVariable, symbolVariable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOpen(boolean newOpen) {
		boolean oldOpen = open;
		open = newOpen;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.RULE_QUERY__OPEN, oldOpen, open));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableDefinition getWildcardVariable() {
		if (wildcardVariable != null && wildcardVariable.eIsProxy()) {
			InternalEObject oldWildcardVariable = (InternalEObject)wildcardVariable;
			wildcardVariable = (VariableDefinition)eResolveProxy(oldWildcardVariable);
			if (wildcardVariable != oldWildcardVariable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QueryPackage.RULE_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
			}
		}
		return wildcardVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableDefinition basicGetWildcardVariable() {
		return wildcardVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWildcardVariable(VariableDefinition newWildcardVariable) {
		VariableDefinition oldWildcardVariable = wildcardVariable;
		wildcardVariable = newWildcardVariable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.RULE_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.RULE_QUERY__DEFINITIONS:
				return ((InternalEList<?>)getDefinitions()).basicRemove(otherEnd, msgs);
			case QueryPackage.RULE_QUERY__SYMBOL:
				return basicSetSymbol(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case QueryPackage.RULE_QUERY__DEFINITIONS:
				return getDefinitions();
			case QueryPackage.RULE_QUERY__OPEN:
				return isOpen() ? Boolean.TRUE : Boolean.FALSE;
			case QueryPackage.RULE_QUERY__WILDCARD_VARIABLE:
				if (resolve) return getWildcardVariable();
				return basicGetWildcardVariable();
			case QueryPackage.RULE_QUERY__SYMBOL:
				return getSymbol();
			case QueryPackage.RULE_QUERY__SYMBOL_VARIABLE:
				if (resolve) return getSymbolVariable();
				return basicGetSymbolVariable();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case QueryPackage.RULE_QUERY__DEFINITIONS:
				getDefinitions().clear();
				getDefinitions().addAll((Collection<? extends ProductionQuery>)newValue);
				return;
			case QueryPackage.RULE_QUERY__OPEN:
				setOpen(((Boolean)newValue).booleanValue());
				return;
			case QueryPackage.RULE_QUERY__WILDCARD_VARIABLE:
				setWildcardVariable((VariableDefinition)newValue);
				return;
			case QueryPackage.RULE_QUERY__SYMBOL:
				setSymbol((SymbolQuery)newValue);
				return;
			case QueryPackage.RULE_QUERY__SYMBOL_VARIABLE:
				setSymbolVariable((VariableDefinition)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case QueryPackage.RULE_QUERY__DEFINITIONS:
				getDefinitions().clear();
				return;
			case QueryPackage.RULE_QUERY__OPEN:
				setOpen(OPEN_EDEFAULT);
				return;
			case QueryPackage.RULE_QUERY__WILDCARD_VARIABLE:
				setWildcardVariable((VariableDefinition)null);
				return;
			case QueryPackage.RULE_QUERY__SYMBOL:
				setSymbol((SymbolQuery)null);
				return;
			case QueryPackage.RULE_QUERY__SYMBOL_VARIABLE:
				setSymbolVariable((VariableDefinition)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case QueryPackage.RULE_QUERY__DEFINITIONS:
				return definitions != null && !definitions.isEmpty();
			case QueryPackage.RULE_QUERY__OPEN:
				return open != OPEN_EDEFAULT;
			case QueryPackage.RULE_QUERY__WILDCARD_VARIABLE:
				return wildcardVariable != null;
			case QueryPackage.RULE_QUERY__SYMBOL:
				return symbol != null;
			case QueryPackage.RULE_QUERY__SYMBOL_VARIABLE:
				return symbolVariable != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == CommutativeOperationQuery.class) {
			switch (derivedFeatureID) {
				case QueryPackage.RULE_QUERY__DEFINITIONS: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS;
				case QueryPackage.RULE_QUERY__OPEN: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN;
				case QueryPackage.RULE_QUERY__WILDCARD_VARIABLE: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == CommutativeOperationQuery.class) {
			switch (baseFeatureID) {
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS: return QueryPackage.RULE_QUERY__DEFINITIONS;
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN: return QueryPackage.RULE_QUERY__OPEN;
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE: return QueryPackage.RULE_QUERY__WILDCARD_VARIABLE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (open: ");
		result.append(open);
		result.append(')');
		return result.toString();
	}

} //RuleQueryImpl
