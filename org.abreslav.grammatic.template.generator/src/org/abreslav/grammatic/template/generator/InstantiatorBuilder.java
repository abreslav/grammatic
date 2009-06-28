package org.abreslav.grammatic.template.generator;

import org.abreslav.grammatic.template.generator.EcoreToTemplateModels.ITraceBuilder;
import org.abreslav.grammatic.template.instantiator.AttributeInstantiator;
import org.abreslav.grammatic.template.instantiator.InstantiatorFactory;
import org.abreslav.grammatic.template.instantiator.InstantiatorModel;
import org.abreslav.grammatic.template.instantiator.ObjectInstantiator;
import org.abreslav.grammatic.template.instantiator.ReferenceInstantiator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

public class InstantiatorBuilder implements ITraceBuilder {

	private final InstantiatorModel myModel = InstantiatorFactory.eINSTANCE.createInstantiatorModel();
	private ObjectInstantiator myCurrentInstantiator;
	
	public InstantiatorModel getModel() {
		return myModel;
	}
	
	@Override
	public void beforeClassTemplate(EClass self, EClass result) {
		myCurrentInstantiator = InstantiatorFactory.eINSTANCE.createObjectInstantiator();
		myCurrentInstantiator.setSource(result);
		myCurrentInstantiator.setTarget(self);
		myModel.getObjectInstantiators().add(myCurrentInstantiator);
	}
	
	@Override
	public void afterClassTemplate(EClass self, EClass template) {
	}

	@Override
	public void onAttributeTemplate(EClass selfClass, EClass resultClass,
			EAttribute self, EAttribute template) {
		AttributeInstantiator attInst = InstantiatorFactory.eINSTANCE.createAttributeInstantiator();
		attInst.setSource(template);
		attInst.setTarget(self);
		myCurrentInstantiator.getAttributeInstantiators().add(attInst);
	}

	@Override
	public void onReferenceTemplate(EClass selfClass, EClass resultClass,
			EReference self, EReference template) {
		ReferenceInstantiator refInst = InstantiatorFactory.eINSTANCE.createReferenceInstantiator();
		refInst.setSource(template);
		refInst.setTarget(self);
		myCurrentInstantiator.getReferenceInstantiators().add(refInst);
	}
	
}
