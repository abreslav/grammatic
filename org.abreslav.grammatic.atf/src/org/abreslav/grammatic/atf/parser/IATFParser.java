package org.abreslav.grammatic.atf.parser;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;

public interface IATFParser {

	SemanticModule loadSemanticModule(String moduleName);
	AspectDefinition loadATFModule(String moduleName);
	void loadTypeSystemModule(String moduleName);
	
}
