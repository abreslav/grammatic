package org.abreslav.grammatic.atf.parser;

import java.util.Map;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;

public interface IATFModuleLoader {

	SemanticModule loadSemanticModule(String moduleName);
	AspectDefinition loadATFModule(String moduleName);
	void loadTypeSystemModule(String moduleName);
	Map<SemanticModule, SemanticModuleDescriptor> getSemanticModuleDescriptors();
	
}
