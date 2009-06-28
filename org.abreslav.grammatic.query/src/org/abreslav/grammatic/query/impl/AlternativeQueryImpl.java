/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import java.util.Collection;

import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryPackage;
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
 * An implementation of the model object '<em><b>Alternative Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.AlternativeQueryImpl#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.AlternativeQueryImpl#isOpen <em>Open</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.AlternativeQueryImpl#getWildcardVariable <em>Wildcard Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AlternativeQueryImpl extends ExpressionQueryImpl implements AlternativeQuery {
	/**
	 * The cached value of the '{@link #getDefinitions() <em>Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<ExpressionQuery> definitions;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AlternativeQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.ALTERNATIVE_QUERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExpressionQuery> getDefinitions() {
		if (definitions == null) {
			definitions = new EObjectContainmentEList<ExpressionQuery>(Query.class, this, QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS);
		}
		return definitions;
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
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.ALTERNATIVE_QUERY__OPEN, oldOpen, open));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
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
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS:
				return ((InternalEList<?>)getDefinitions()).basicRemove(otherEnd, msgs);
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
			case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS:
				return getDefinitions();
			case QueryPackage.ALTERNATIVE_QUERY__OPEN:
				return isOpen() ? Boolean.TRUE : Boolean.FALSE;
			case QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE:
				if (resolve) return getWildcardVariable();
				return basicGetWildcardVariable();
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
			case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS:
				getDefinitions().clear();
				getDefinitions().addAll((Collection<? extends ExpressionQuery>)newValue);
				return;
			case QueryPackage.ALTERNATIVE_QUERY__OPEN:
				setOpen(((Boolean)newValue).booleanValue());
				return;
			case QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE:
				setWildcardVariable((VariableDefinition)newValue);
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
			case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS:
				getDefinitions().clear();
				return;
			case QueryPackage.ALTERNATIVE_QUERY__OPEN:
				setOpen(OPEN_EDEFAULT);
				return;
			case QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE:
				setWildcardVariable((VariableDefinition)null);
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
			case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS:
				return definitions != null && !definitions.isEmpty();
			case QueryPackage.ALTERNATIVE_QUERY__OPEN:
				return open != OPEN_EDEFAULT;
			case QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE:
				return wildcardVariable != null;
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
				case QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS;
				case QueryPackage.ALTERNATIVE_QUERY__OPEN: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN;
				case QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE: return QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE;
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
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS: return QueryPackage.ALTERNATIVE_QUERY__DEFINITIONS;
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN: return QueryPackage.ALTERNATIVE_QUERY__OPEN;
				case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE: return QueryPackage.ALTERNATIVE_QUERY__WILDCARD_VARIABLE;
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

} //AlternativeQueryImpl
