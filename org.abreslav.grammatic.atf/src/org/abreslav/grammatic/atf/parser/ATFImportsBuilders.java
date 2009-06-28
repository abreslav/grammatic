package org.abreslav.grammatic.atf.parser;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.grammar.template.parser.AbstractImportsBuilders;
import org.abreslav.grammatic.utils.IErrorHandler;

public class ATFImportsBuilders extends AbstractImportsBuilders {

	private final IATFParser myParser;
	private final ISemanticModuleHandler mySemanticModuleHandler;

	public ATFImportsBuilders(
			IErrorHandler<? extends RuntimeException> errorHandler,
			IATFParser parser, ISemanticModuleHandler semanticModuleHandler) {
		super(errorHandler);
		myParser = parser;
		mySemanticModuleHandler = semanticModuleHandler;
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
