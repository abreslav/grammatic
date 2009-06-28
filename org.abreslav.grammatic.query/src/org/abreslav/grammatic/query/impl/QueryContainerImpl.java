/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import java.util.Collection;

import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.VariableDefinition;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.QueryContainerImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.QueryContainerImpl#getVariableDefinitions <em>Variable Definitions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryContainerImpl<T extends Query> extends EObjectImpl implements QueryContainer<T> {
	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected T query;

	/**
	 * The cached value of the '{@link #getVariableDefinitions() <em>Variable Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariableDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<VariableDefinition> variableDefinitions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QueryContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.QUERY_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public T getQuery() {
		return query;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetQuery(T newQuery, NotificationChain msgs) {
		T oldQuery = query;
		query = newQuery;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.QUERY_CONTAINER__QUERY, oldQuery, newQuery);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuery(T newQuery) {
		if (newQuery != query) {
			NotificationChain msgs = null;
			if (query != null)
				msgs = ((InternalEObject)query).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.QUERY_CONTAINER__QUERY, null, msgs);
			if (newQuery != null)
				msgs = ((InternalEObject)newQuery).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.QUERY_CONTAINER__QUERY, null, msgs);
			msgs = basicSetQuery(newQuery, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.QUERY_CONTAINER__QUERY, newQuery, newQuery));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VariableDefinition> getVariableDefinitions() {
		if (variableDefinitions == null) {
			variableDefinitions = new EObjectContainmentEList<VariableDefinition>(VariableDefinition.class, this, QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS);
		}
		return variableDefinitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.QUERY_CONTAINER__QUERY:
				return basicSetQuery(null, msgs);
			case QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS:
				return ((InternalEList<?>)getVariableDefinitions()).basicRemove(otherEnd, msgs);
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
			case QueryPackage.QUERY_CONTAINER__QUERY:
				return getQuery();
			case QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS:
				return getVariableDefinitions();
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
			case QueryPackage.QUERY_CONTAINER__QUERY:
				setQuery((T)newValue);
				return;
			case QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS:
				getVariableDefinitions().clear();
				getVariableDefinitions().addAll((Collection<? extends VariableDefinition>)newValue);
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
			case QueryPackage.QUERY_CONTAINER__QUERY:
				setQuery((T)null);
				return;
			case QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS:
				getVariableDefinitions().clear();
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
			case QueryPackage.QUERY_CONTAINER__QUERY:
				return query != null;
			case QueryPackage.QUERY_CONTAINER__VARIABLE_DEFINITIONS:
				return variableDefinitions != null && !variableDefinitions.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //QueryContainerImpl
