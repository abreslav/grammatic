/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import java.util.Collection;

import org.abreslav.grammatic.query.CommutativeOperationQuery;
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
 * An implementation of the model object '<em><b>Commutative Operation Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl#isOpen <em>Open</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.CommutativeOperationQueryImpl#getWildcardVariable <em>Wildcard Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CommutativeOperationQueryImpl<D extends Query> extends QueryImpl implements CommutativeOperationQuery<D> {
	/**
	 * The cached value of the '{@link #getDefinitions() <em>Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<D> definitions;

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
	protected CommutativeOperationQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.COMMUTATIVE_OPERATION_QUERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<D> getDefinitions() {
		if (definitions == null) {
			definitions = new EObjectContainmentEList<D>(Query.class, this, QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS);
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
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN, oldOpen, open));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
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
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE, oldWildcardVariable, wildcardVariable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS:
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
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS:
				return getDefinitions();
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN:
				return isOpen() ? Boolean.TRUE : Boolean.FALSE;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE:
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
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS:
				getDefinitions().clear();
				getDefinitions().addAll((Collection<? extends D>)newValue);
				return;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN:
				setOpen(((Boolean)newValue).booleanValue());
				return;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE:
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
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS:
				getDefinitions().clear();
				return;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN:
				setOpen(OPEN_EDEFAULT);
				return;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE:
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
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__DEFINITIONS:
				return definitions != null && !definitions.isEmpty();
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__OPEN:
				return open != OPEN_EDEFAULT;
			case QueryPackage.COMMUTATIVE_OPERATION_QUERY__WILDCARD_VARIABLE:
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
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (open: ");
		result.append(open);
		result.append(')');
		return result.toString();
	}

} //CommutativeOperationQueryImpl
