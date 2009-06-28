/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Symbol Reference Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.SymbolReferenceQueryImpl#getSymbol <em>Symbol</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SymbolReferenceQueryImpl extends ExpressionQueryImpl implements SymbolReferenceQuery {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SymbolReferenceQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.SYMBOL_REFERENCE_QUERY;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL, oldSymbol, newSymbol);
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
				msgs = ((InternalEObject)symbol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL, null, msgs);
			if (newSymbol != null)
				msgs = ((InternalEObject)newSymbol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL, null, msgs);
			msgs = basicSetSymbol(newSymbol, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL, newSymbol, newSymbol));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL:
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
			case QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL:
				return getSymbol();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL:
				setSymbol((SymbolQuery)newValue);
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
			case QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL:
				setSymbol((SymbolQuery)null);
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
			case QueryPackage.SYMBOL_REFERENCE_QUERY__SYMBOL:
				return symbol != null;
		}
		return super.eIsSet(featureID);
	}

} //SymbolReferenceQueryImpl
