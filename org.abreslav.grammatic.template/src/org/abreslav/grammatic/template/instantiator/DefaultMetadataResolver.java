/**
 * 
 */
package org.abreslav.grammatic.template.instantiator;

import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.template.TemplateBody;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

public class DefaultMetadataResolver implements IMetadataResolver {
	private final Map<EClass, ObjectInstantiator> myClassToInstantiator = new HashMap<EClass, ObjectInstantiator>(); 

	@Override
	public <T> ObjectInstantiator getObjectInstantiator(TemplateBody<T> body) {
		return myClassToInstantiator.get(body.eClass());
	}

	@Override
	public EReference resolveReference(EClass eClass, EReference reference) {
		return reference;
	}

	@Override
	public EAttribute resolveAttribute(EClass eClass, EAttribute attribute) {
		return attribute;
	}

	@Override
	public void addInstantiatorModel(InstantiatorModel model) {
		for (ObjectInstantiator objectInstantiator : model.getObjectInstantiators()) {
			myClassToInstantiator.put(objectInstantiator.getSource(), objectInstantiator);
		}
	}

	@Override
	public EClass resolveClass(EClass eClass) {
		return eClass;
	}

	@Override
	public void addTargetPackage(EPackage package1) {
		
	}
}