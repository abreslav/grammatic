/**
 * 
 */
package org.abreslav.grammatic.template.instantiator;

import org.abreslav.grammatic.template.TemplateBody;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

public interface IMetadataResolver {
	public <T> ObjectInstantiator getObjectInstantiator(TemplateBody<T> body);
	
	public EClass resolveClass(EClass eClass);

	public EReference resolveReference(EClass eClass, EReference reference);
	
	public EAttribute resolveAttribute(EClass eClass, EAttribute attribute);
	
	public void addInstantiatorModel(InstantiatorModel model);
	public void addTargetPackage(EPackage ePackage);
}