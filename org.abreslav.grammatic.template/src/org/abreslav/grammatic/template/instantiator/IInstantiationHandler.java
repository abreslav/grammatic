package org.abreslav.grammatic.template.instantiator;

import org.abreslav.grammatic.template.TemplateBody;
import org.eclipse.emf.ecore.EObject;

public interface IInstantiationHandler {
	
	IInstantiationHandler DEFAULT = new IInstantiationHandler() {

		@Override
		public <T extends EObject> void handleCreatedObject(TemplateBody<? extends T> template, T object) {
			
		}
		
	};

	public <T extends EObject> void handleCreatedObject(TemplateBody<? extends T> template, T object);
}
