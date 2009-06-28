package org.abreslav.grammatic.emfutils;

import org.abreslav.grammatic.parsingutils.resolve.IProxy;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Internal;

public class EObjectProxy<T extends EObject> implements EObject, InternalEObject, IProxy<T> {

	private T myDelegate;
	private InternalEObject myInternalDelegate;
	
	@Override
	public final T pGetDelegate() {
		return myDelegate;
	}
	
	@Override
	public void pSetDelegate(T delegate) {
		myDelegate = delegate;
		myInternalDelegate = (InternalEObject) delegate;
	}
	
	@Override
	public final boolean equals(Object obj) {
		throw new UnsupportedOperationException("Never call equals() on proxies!!!");
	}
	
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException("Never call hashCode() on proxies!!!");
	}

	@Override
	public String toString() {
		return myDelegate.toString();
	}
	
	@Override
	public TreeIterator<EObject> eAllContents() {
		return myDelegate.eAllContents();
	}

	@Override
	public EClass eClass() {
		return myDelegate.eClass();
	}

	@Override
	public EObject eContainer() {
		return myDelegate.eContainer();
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		return myDelegate.eContainingFeature();
	}

	@Override
	public EReference eContainmentFeature() {
		return myDelegate.eContainmentFeature();
	}

	@Override
	public EList<EObject> eContents() {
		return myDelegate.eContents();
	}

	@Override
	public EList<EObject> eCrossReferences() {
		return myDelegate.eCrossReferences();
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		return myDelegate.eGet(feature);
	}

	@Override
	public Object eGet(EStructuralFeature feature, boolean resolve) {
		return myDelegate.eGet(feature, resolve);
	}

	@Override
	public boolean eIsProxy() {
		// This hack is needed due to EMF getters calling eIsProxy() 
		// on a value before returning it
		return myDelegate == null ? false : myDelegate.eIsProxy();
	}

	@Override
	public boolean eIsSet(EStructuralFeature feature) {
		return myDelegate.eIsSet(feature);
	}

	@Override
	public Resource eResource() {
		return myDelegate.eResource();
	}

	@Override
	public void eSet(EStructuralFeature feature, Object newValue) {
		myDelegate.eSet(feature, newValue);
	}

	@Override
	public void eUnset(EStructuralFeature feature) {
		myDelegate.eUnset(feature);
	}

	@Override
	public EList<Adapter> eAdapters() {
		return myDelegate.eAdapters();
	}

	@Override
	public boolean eDeliver() {
		return myDelegate.eDeliver();
	}

	@Override
	public void eNotify(Notification notification) {
		myDelegate.eNotify(notification);
	}

	@Override
	public void eSetDeliver(boolean deliver) {
		myDelegate.eSetDeliver(deliver);
	}

	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		return myInternalDelegate.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	@Override
	public NotificationChain eBasicRemoveFromContainer(
			NotificationChain notifications) {
		return myInternalDelegate.eBasicRemoveFromContainer(notifications);
	}

	@Override
	public NotificationChain eBasicSetContainer(InternalEObject newContainer,
			int newContainerFeatureID, NotificationChain notifications) {
		return myInternalDelegate.eBasicSetContainer(newContainer, newContainerFeatureID, notifications);
	}

	@Override
	public int eContainerFeatureID() {
		return myInternalDelegate.eContainerFeatureID();
	}

	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		return myInternalDelegate.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	@Override
	public Internal eDirectResource() {
		return myInternalDelegate.eDirectResource();
	}

	@Override
	public Object eGet(EStructuralFeature feature, boolean resolve,
			boolean coreType) {
		return myInternalDelegate.eGet(feature, resolve, coreType);
	}

	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		return myInternalDelegate.eGet(featureID, resolve, coreType);
	}

	@Override
	public InternalEObject eInternalContainer() {
		return myInternalDelegate.eInternalContainer();
	}

	@Override
	public Internal eInternalResource() {
		return myInternalDelegate.eInternalResource();
	}

	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, Class<?> baseClass, NotificationChain notifications) {
		return myInternalDelegate.eInverseAdd(otherEnd, featureID, baseClass, notifications);
	}

	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, Class<?> baseClass, NotificationChain notifications) {
		return myInternalDelegate.eInverseRemove(otherEnd, featureID, baseClass, notifications);
	}

	@Override
	public boolean eIsSet(int featureID) {
		return myInternalDelegate.eIsSet(featureID);
	}

	@Override
	public boolean eNotificationRequired() {
		return myInternalDelegate.eNotificationRequired();
	}

	@Override
	public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
		return myInternalDelegate.eObjectForURIFragmentSegment(uriFragmentSegment);
	}

	@Override
	public URI eProxyURI() {
		return myInternalDelegate.eProxyURI();
	}

	@Override
	public EObject eResolveProxy(InternalEObject proxy) {
		return myInternalDelegate.eResolveProxy(proxy);
	}

	@Override
	public void eSet(int featureID, Object newValue) {
		myInternalDelegate.eSet(featureID, newValue);
	}

	@Override
	public void eSetClass(EClass class1) {
		myInternalDelegate.eSetClass(class1);
	}

	@Override
	public void eSetProxyURI(URI uri) {
		myInternalDelegate.eSetProxyURI(uri);
	}

	@Override
	public NotificationChain eSetResource(Internal resource,
			NotificationChain notifications) {
		return myInternalDelegate.eSetResource(resource, notifications);
	}

	@Override
	public void eSetStore(EStore store) {
		myInternalDelegate.eSetStore(store);
	}

	@Override
	public Setting eSetting(EStructuralFeature feature) {
		return myInternalDelegate.eSetting(feature);
	}

	@Override
	public EStore eStore() {
		return myInternalDelegate.eStore();
	}

	@Override
	public String eURIFragmentSegment(EStructuralFeature feature, EObject object) {
		return myInternalDelegate.eURIFragmentSegment(feature, object);
	}

	@Override
	public void eUnset(int featureID) {
		myInternalDelegate.eUnset(featureID);
	}

}
