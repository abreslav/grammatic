/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.abreslav.grammatic.query.impl;

import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeValueQuery;
import org.abreslav.grammatic.query.QueryPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.abreslav.grammatic.query.impl.AttributeQueryImpl#getAttributeValue <em>Attribute Value</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.AttributeQueryImpl#getAttributeName <em>Attribute Name</em>}</li>
 *   <li>{@link org.abreslav.grammatic.query.impl.AttributeQueryImpl#getNamespaceUri <em>Namespace Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeQueryImpl extends EObjectImpl implements AttributeQuery {
	/**
	 * The cached value of the '{@link #getAttributeValue() <em>Attribute Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeValue()
	 * @generated
	 * @ordered
	 */
	protected AttributeValueQuery attributeValue;

	/**
	 * The default value of the '{@link #getAttributeName() <em>Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeName()
	 * @generated
	 * @ordered
	 */
	protected static final String ATTRIBUTE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAttributeName() <em>Attribute Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeName()
	 * @generated
	 * @ordered
	 */
	protected String attributeName = ATTRIBUTE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespaceUri() <em>Namespace Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespaceUri()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespaceUri() <em>Namespace Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespaceUri()
	 * @generated
	 * @ordered
	 */
	protected String namespaceUri = NAMESPACE_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QueryPackage.Literals.ATTRIBUTE_QUERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeValueQuery getAttributeValue() {
		return attributeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAttributeValue(AttributeValueQuery newAttributeValue, NotificationChain msgs) {
		AttributeValueQuery oldAttributeValue = attributeValue;
		attributeValue = newAttributeValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE, oldAttributeValue, newAttributeValue);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAttributeValue(AttributeValueQuery newAttributeValue) {
		if (newAttributeValue != attributeValue) {
			NotificationChain msgs = null;
			if (attributeValue != null)
				msgs = ((InternalEObject)attributeValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE, null, msgs);
			if (newAttributeValue != null)
				msgs = ((InternalEObject)newAttributeValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE, null, msgs);
			msgs = basicSetAttributeValue(newAttributeValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE, newAttributeValue, newAttributeValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAttributeName(String newAttributeName) {
		String oldAttributeName = attributeName;
		attributeName = newAttributeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_NAME, oldAttributeName, attributeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespaceUri() {
		return namespaceUri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespaceUri(String newNamespaceUri) {
		String oldNamespaceUri = namespaceUri;
		namespaceUri = newNamespaceUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.ATTRIBUTE_QUERY__NAMESPACE_URI, oldNamespaceUri, namespaceUri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE:
				return basicSetAttributeValue(null, msgs);
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
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE:
				return getAttributeValue();
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_NAME:
				return getAttributeName();
			case QueryPackage.ATTRIBUTE_QUERY__NAMESPACE_URI:
				return getNamespaceUri();
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
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE:
				setAttributeValue((AttributeValueQuery)newValue);
				return;
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_NAME:
				setAttributeName((String)newValue);
				return;
			case QueryPackage.ATTRIBUTE_QUERY__NAMESPACE_URI:
				setNamespaceUri((String)newValue);
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
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE:
				setAttributeValue((AttributeValueQuery)null);
				return;
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_NAME:
				setAttributeName(ATTRIBUTE_NAME_EDEFAULT);
				return;
			case QueryPackage.ATTRIBUTE_QUERY__NAMESPACE_URI:
				setNamespaceUri(NAMESPACE_URI_EDEFAULT);
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
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_VALUE:
				return attributeValue != null;
			case QueryPackage.ATTRIBUTE_QUERY__ATTRIBUTE_NAME:
				return ATTRIBUTE_NAME_EDEFAULT == null ? attributeName != null : !ATTRIBUTE_NAME_EDEFAULT.equals(attributeName);
			case QueryPackage.ATTRIBUTE_QUERY__NAMESPACE_URI:
				return NAMESPACE_URI_EDEFAULT == null ? namespaceUri != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceUri);
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
		result.append(" (attributeName: ");
		result.append(attributeName);
		result.append(", namespaceUri: ");
		result.append(namespaceUri);
		result.append(')');
		return result.toString();
	}

} //AttributeQueryImpl
