/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import java.util.Collection;

import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.query.QueryPackage;
import org.abreslav.grammatic.query.TypedRegExpValue;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Typed Reg Exp Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.TypedRegExpValueImpl#getWildCardSeparatedFragments <em>Wild Card Separated Fragments</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TypedRegExpValueImpl extends AttributeValueQueryImpl implements TypedRegExpValue {
	/**
	 * The cached value of the '{@link #getWildCardSeparatedFragments() <em>Wild Card Separated Fragments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWildCardSeparatedFragments()
	 * @generated
	 * @ordered
	 */
	protected EList<Value> wildCardSeparatedFragments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TypedRegExpValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.TYPED_REG_EXP_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Value> getWildCardSeparatedFragments() {
		if (wildCardSeparatedFragments == null) {
			wildCardSeparatedFragments = new EObjectContainmentEList<Value>(Value.class, this, QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS);
		}
		return wildCardSeparatedFragments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS:
				return ((InternalEList<?>)getWildCardSeparatedFragments()).basicRemove(otherEnd, msgs);
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
			case QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS:
				return getWildCardSeparatedFragments();
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
			case QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS:
				getWildCardSeparatedFragments().clear();
				getWildCardSeparatedFragments().addAll((Collection<? extends Value>)newValue);
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
			case QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS:
				getWildCardSeparatedFragments().clear();
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
			case QueryPackage.TYPED_REG_EXP_VALUE__WILD_CARD_SEPARATED_FRAGMENTS:
				return wildCardSeparatedFragments != null && !wildCardSeparatedFragments.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TypedRegExpValueImpl
