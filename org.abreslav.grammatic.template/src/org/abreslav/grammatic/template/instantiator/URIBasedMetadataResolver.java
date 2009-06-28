/**
 * 
 */
package org.abreslav.grammatic.template.instantiator;

import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.template.TemplateBody;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This metadata resolver can pass by a problem with same EClasses loaded
 * in different ways
 *  
 * @author abreslav
 *
 */
public class URIBasedMetadataResolver implements IMetadataResolver {
	private final Map<URI, ObjectInstantiator> myClassURIToInstantiator = new HashMap<URI, ObjectInstantiator>(); 
	private final Map<URI, EClass> myClassURIToClass = new HashMap<URI, EClass>();
	
	public void addInstantiatorModel(InstantiatorModel model) {
		for (ObjectInstantiator objectInstantiator : model.getObjectInstantiators()) {
			EClass eClass = objectInstantiator.getSource();
			myClassURIToInstantiator.put(getEClassURI(eClass), objectInstantiator);
		}
	}
	
	public <T> ObjectInstantiator getObjectInstantiator(TemplateBody<T> body) {
		return myClassURIToInstantiator.get(getEClassURI(body.eClass()));
	}

	public EReference resolveReference(EClass eClass, EReference reference) {
		return resolveFeature(eClass, reference);
	}

	public EAttribute resolveAttribute(EClass eClass, EAttribute attribute) {
		return resolveFeature(eClass, attribute);
	}

	@SuppressWarnings("unchecked")
	private <T extends EStructuralFeature> T resolveFeature(EClass eClass,
			T feature) {
		return (T) eClass.getEStructuralFeature(feature.getFeatureID());
	}
	
	private URI getEClassURI(EClassifier eClass) {
		return URI.createURI(eClass.getEPackage().getNsURI()).appendFragment(eClass.getName());
	}

	@Override
	public EClass resolveClass(EClass eClass) {
		EClass result = myClassURIToClass.get(getEClassURI(eClass));
		if (result == null) {
			result = eClass;
		}
		return result;
	}

	@Override
	public void addTargetPackage(EPackage ePackage) {
		EList<EClassifier> classifiers = ePackage.getEClassifiers();
		for (EClassifier classifier : classifiers) {
			if (classifier instanceof EClass) {
				myClassURIToClass.put(getEClassURI(classifier), (EClass) classifier);
			}
		}
		
	}

}