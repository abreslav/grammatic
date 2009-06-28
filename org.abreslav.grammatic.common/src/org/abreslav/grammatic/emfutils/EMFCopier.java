package org.abreslav.grammatic.emfutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.utils.CustomHashMap;
import org.abreslav.grammatic.utils.IHashingStrategy;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;

// Completely stolen from EcoreUtil.Copier
public class EMFCopier {
	
	public static interface ICopyHandler {
		void handleCopy(EObject original, EObject copy);
	}
	
	public static final ICopyHandler DEFAULT_COPY_HANDLER = new ICopyHandler() {

		@Override
		public void handleCopy(EObject original, EObject copy) {
			
		}
		
	};
	
	private static final long serialVersionUID = 1L;

	private final ICopyHandler myCopyHandler;
	
	private final boolean myResolveProxies;

	private final boolean myUseOriginalReferences;

	private final Map<EObject, EObject> myMap;

	public EMFCopier() {
		this(true, true, new HashMap<EObject, EObject>(), DEFAULT_COPY_HANDLER);
	}
	
	public EMFCopier(IHashingStrategy hashingStrategy) {
		this(true, true, hashingStrategy);
	}

	public EMFCopier(IHashingStrategy hashingStrategy, ICopyHandler copyHandler) {
		this(true, true, new CustomHashMap<EObject, EObject>(hashingStrategy), copyHandler);
	}
	
	public EMFCopier(boolean resolveProxies, boolean useOriginalReferences, IHashingStrategy hashingStrategy) {
		this(resolveProxies, useOriginalReferences, new CustomHashMap<EObject, EObject>(hashingStrategy), DEFAULT_COPY_HANDLER);
	}

	public EMFCopier(boolean resolveProxies, boolean useOriginalReferences, Map<EObject, EObject> map, ICopyHandler copyHandler) {
		this.myResolveProxies = resolveProxies;
		this.myUseOriginalReferences = useOriginalReferences;
		this.myMap = map;
		this.myCopyHandler = copyHandler;
	}
	
	public <T> Collection<T> copyAll(Collection<? extends T> eObjects) {
		Collection<T> result = new ArrayList<T>(eObjects.size());
		for (Object object : eObjects) {
			@SuppressWarnings("unchecked")
			T t = (T) copy((EObject) object);
			result.add(t);
		}
		return result;
	}

	public EObject copy(EObject eObject) {
		EObject copyEObject = createCopy(eObject);
		myMap.put(eObject, copyEObject);
		EClass eClass = eObject.eClass();
		for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
			EStructuralFeature eStructuralFeature = eClass
					.getEStructuralFeature(i);
			if (eStructuralFeature.isChangeable()
					&& !eStructuralFeature.isDerived()) {
				if (eStructuralFeature instanceof EAttribute) {
					copyAttribute((EAttribute) eStructuralFeature, eObject,
							copyEObject);
				} else {
					EReference eReference = (EReference) eStructuralFeature;
					if (eReference.isContainment()) {
						copyContainment(eReference, eObject, copyEObject);
					}
				}
			}
		}

		copyProxyURI(eObject, copyEObject);

		return copyEObject;
	}

	protected void copyProxyURI(EObject eObject, EObject copyEObject) {
		if (eObject.eIsProxy()) {
			((InternalEObject) copyEObject)
					.eSetProxyURI(((InternalEObject) eObject).eProxyURI());
		}
	}

	protected EObject createCopy(EObject eObject) {
		EObject copy = EcoreUtil.create(getTarget(eObject.eClass()));
		myCopyHandler.handleCopy(eObject, copy);
		return copy;
	}

	protected EClass getTarget(EClass eClass) {
		return eClass;
	}

	protected EStructuralFeature getTarget(EStructuralFeature eStructuralFeature) {
		return eStructuralFeature;
	}

	protected void copyContainment(EReference eReference, EObject eObject,
			EObject copyEObject) {
		if (eObject.eIsSet(eReference)) {
			if (eReference.isMany()) {
				@SuppressWarnings("unchecked")
				List<EObject> source = (List<EObject>) eObject.eGet(eReference);
				@SuppressWarnings("unchecked")
				List<EObject> target = (List<EObject>) copyEObject
						.eGet(getTarget(eReference));
				if (source.isEmpty()) {
					target.clear();
				} else {
					target.addAll(copyAll(source));
				}
			} else {
				EObject childEObject = (EObject) eObject.eGet(eReference);
				copyEObject.eSet(getTarget(eReference),
						childEObject == null ? null : copy(childEObject));
			}
		}
	}

	protected void copyAttribute(EAttribute eAttribute, EObject eObject,
			EObject copyEObject) {
		if (eObject.eIsSet(eAttribute)) {
			if (FeatureMapUtil.isFeatureMap(eAttribute)) {
				FeatureMap featureMap = (FeatureMap) eObject.eGet(eAttribute);
				for (int i = 0, size = featureMap.size(); i < size; ++i) {
					EStructuralFeature feature = featureMap
							.getEStructuralFeature(i);
					if (feature instanceof EReference
							&& ((EReference) feature).isContainment()) {
						Object value = featureMap.getValue(i);
						if (value != null) {
							copy((EObject) value);
						}
					}
				}
			} else if (eAttribute.isMany()) {
				List<?> source = (List<?>) eObject.eGet(eAttribute);
				@SuppressWarnings("unchecked")
				List<Object> target = (List<Object>) copyEObject
						.eGet(getTarget(eAttribute));
				if (source.isEmpty()) {
					target.clear();
				} else {
					target.addAll(source);
				}
			} else {
				copyEObject.eSet(getTarget(eAttribute), eObject
						.eGet(eAttribute));
			}
		}
	}

	public void copyReferences() {
		for (Map.Entry<EObject, EObject> entry : myMap.entrySet()) {
			EObject eObject = entry.getKey();
			EObject copyEObject = entry.getValue();
			EClass eClass = eObject.eClass();
			for (int j = 0, size = eClass.getFeatureCount(); j < size; ++j) {
				EStructuralFeature eStructuralFeature = eClass
						.getEStructuralFeature(j);
				if (eStructuralFeature.isChangeable()
						&& !eStructuralFeature.isDerived()) {
					if (eStructuralFeature instanceof EReference) {
						EReference eReference = (EReference) eStructuralFeature;
						if (!eReference.isContainment()
								&& !eReference.isContainer()) {
							copyReference(eReference, eObject, copyEObject);
						}
					} else if (FeatureMapUtil.isFeatureMap(eStructuralFeature)) {
						FeatureMap featureMap = (FeatureMap) eObject
								.eGet(eStructuralFeature);
						FeatureMap copyFeatureMap = (FeatureMap) copyEObject
								.eGet(getTarget(eStructuralFeature));
						int copyFeatureMapSize = copyFeatureMap.size();
						for (int k = 0, featureMapSize = featureMap.size(); k < featureMapSize; ++k) {
							EStructuralFeature feature = featureMap
									.getEStructuralFeature(k);
							if (feature instanceof EReference) {
								Object referencedEObject = featureMap
										.getValue(k);
								Object copyReferencedEObject = myMap
										.get(referencedEObject);
								if (copyReferencedEObject == null
										&& referencedEObject != null) {
									EReference reference = (EReference) feature;
									if (!myUseOriginalReferences
											|| reference.isContainment()
											|| reference.getEOpposite() != null) {
										continue;
									}
									copyReferencedEObject = referencedEObject;
								}
								// If we can't add it, it must already be in the
								// list so find it and move it to the end.
								//
								if (!copyFeatureMap.add(feature,
										copyReferencedEObject)) {
									for (int l = 0; l < copyFeatureMapSize; ++l) {
										if (copyFeatureMap
												.getEStructuralFeature(l) == feature
												&& copyFeatureMap.getValue(l) == copyReferencedEObject) {
											copyFeatureMap.move(copyFeatureMap
													.size() - 1, l);
											--copyFeatureMapSize;
											break;
										}
									}
								}
							} else {
								copyFeatureMap.add(featureMap.get(k));
							}
						}
					}
				}
			}
		}
	}

	protected void copyReference(EReference eReference, EObject eObject,
			EObject copyEObject) {
		if (eObject.eIsSet(eReference)) {
			if (eReference.isMany()) {
				@SuppressWarnings("unchecked")
				InternalEList<EObject> source = (InternalEList<EObject>) eObject
						.eGet(eReference);
				@SuppressWarnings("unchecked")
				InternalEList<EObject> target = (InternalEList<EObject>) copyEObject
						.eGet(getTarget(eReference));
				if (source.isEmpty()) {
					target.clear();
				} else {
					boolean isBidirectional = eReference.getEOpposite() != null;
					int index = 0;
					for (Iterator<EObject> k = myResolveProxies ? source
							.iterator() : source.basicIterator(); k.hasNext();) {
						EObject referencedEObject = k.next();
						EObject copyReferencedEObject = myMap
								.get(referencedEObject);
						if (copyReferencedEObject == null) {
							if (myUseOriginalReferences && !isBidirectional) {
								target.addUnique(index, referencedEObject);
								++index;
							}
						} else {
							if (isBidirectional) {
								int position = target
										.indexOf(copyReferencedEObject);
								if (position == -1) {
									target.addUnique(index,
											copyReferencedEObject);
								} else if (index != position) {
									target.move(index, copyReferencedEObject);
								}
							} else {
								target.addUnique(index, copyReferencedEObject);
							}
							++index;
						}
					}
				}
			} else {
				Object referencedEObject = eObject.eGet(eReference,
						myResolveProxies);
				if (referencedEObject == null) {
					copyEObject.eSet(getTarget(eReference), null);
				} else {
					Object copyReferencedEObject = myMap.get(referencedEObject);
					if (copyReferencedEObject == null) {
						if (myUseOriginalReferences
								&& eReference.getEOpposite() == null) {
							copyEObject.eSet(getTarget(eReference),
									referencedEObject);
						}
					} else {
						copyEObject.eSet(getTarget(eReference),
								copyReferencedEObject);
					}
				}
			}
		}
	}
}
