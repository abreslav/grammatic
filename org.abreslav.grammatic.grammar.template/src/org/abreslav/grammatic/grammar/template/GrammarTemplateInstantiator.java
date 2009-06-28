package org.abreslav.grammatic.grammar.template;

import java.io.IOException;

import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.emfutils.EMFCopier.ICopyHandler;
import org.abreslav.grammatic.grammar.GrammarPackage;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplatePackage;
import org.abreslav.grammatic.template.instantiator.IInstantiationHandler;
import org.abreslav.grammatic.template.instantiator.ISpecialTemplateInstantiator;
import org.abreslav.grammatic.template.instantiator.InstantiatorModel;
import org.abreslav.grammatic.template.instantiator.InstantiatorPackage;
import org.abreslav.grammatic.template.instantiator.TemplateInstantiatorInterpreter;
import org.abreslav.grammatic.template.instantiator.URIBasedMetadataResolver;
import org.eclipse.emf.ecore.EcorePackage;

public class GrammarTemplateInstantiator {

	private static InstantiatorModel INSTANTIATOR_MODEL;
	static {
		try {
			// TODO: Specify resource location properly
			INSTANTIATOR_MODEL = (InstantiatorModel) new ResourceLoader(".").loadStaticModel(
				"../org.abreslav.grammatic.grammar.template/model/instantiators/grammarInstantiator.xmi", 
				EcorePackage.eINSTANCE,
				InstantiatorPackage.eINSTANCE,
				GrammarPackage.eINSTANCE,
				GrammarTemplatePackage.eINSTANCE
			);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static TemplateInstantiatorInterpreter getInstantiator(IInstantiationHandler handler, ISpecialTemplateInstantiator specialInstantiator, ICopyHandler copyHandler) {
		TemplateInstantiatorInterpreter instantiator = new TemplateInstantiatorInterpreter(handler, new URIBasedMetadataResolver(), specialInstantiator, copyHandler);
		instantiator.addInstantiatorModel(INSTANTIATOR_MODEL);
		instantiator.addTargetPackage(GrammarPackage.eINSTANCE);
		return instantiator;
	}

}
