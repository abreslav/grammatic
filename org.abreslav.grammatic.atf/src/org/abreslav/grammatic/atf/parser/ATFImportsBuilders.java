package org.abreslav.grammatic.atf.parser;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.grammar.template.parser.AbstractImportsBuilders;
import org.abreslav.grammatic.utils.IErrorHandler;

public class ATFImportsBuilders extends AbstractImportsBuilders {

	private final IATFModuleLoader myParser;
	private final ISemanticModuleHandler mySemanticModuleHandler;
	private final List<SemanticModule> myImportedModules = new ArrayList<SemanticModule>();
	
	public ATFImportsBuilders(
			IErrorHandler<? extends RuntimeException> errorHandler,
			IATFModuleLoader parser, ISemanticModuleHandler semanticModuleHandler) {
		super(errorHandler);
		myParser = parser;
		mySemanticModuleHandler = semanticModuleHandler;
	}

	public List<SemanticModule> getImportedModules() {
		return myImportedModules;
	}
	
	@Override
	public IImportSpecBuilder getImportSpecBuilder() {
		return new IImportSpecBuilder() {


			@Override
			public void init() {
			}
			
			@Override
			public void release() {
			}

			@Override
			public void moduleName(String moduleName) {
				if (moduleName.endsWith(".module")) {
					SemanticModule module = myParser.loadSemanticModule(moduleName);
					myImportedModules.add(module);
					mySemanticModuleHandler.registerModule(module);
				} else if (moduleName.endsWith(".typesystem")) {
					myParser.loadTypeSystemModule(moduleName);
				} else {
					myErrorHandler.reportError("Unsupported file type: %s", moduleName);
				}
			}
			
		};
	}

}
