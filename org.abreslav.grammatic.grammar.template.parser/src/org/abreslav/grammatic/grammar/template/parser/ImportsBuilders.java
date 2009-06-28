package org.abreslav.grammatic.grammar.template.parser;

import java.io.IOException;

import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;

public class ImportsBuilders extends AbstractImportsBuilders implements IImportsBuilders, INameResolver {

	private final IModuleLoader myModuleLoader;
	
	public ImportsBuilders(IModuleLoader moduleLoader,
			IErrorHandler<? extends RuntimeException> errorHandler) {
		super(errorHandler);
		myModuleLoader = moduleLoader;
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
				try {
					if (moduleName.endsWith(".grammar")) {
						myModuleLoader.loadGrammar(moduleName);
					} else if (moduleName.endsWith(".templates")) {
						myModuleLoader.loadTemplates(moduleName);
					} else {
						myErrorHandler.reportError("Error while loading %s: unknown extension", moduleName);
					}
				} catch (IOException e) {
					myErrorHandler.reportError("Error while loading %s: %s", moduleName, e.getMessage());
				} catch (RecognitionException e) {
					myErrorHandler.reportError("Error while loading %s: %s", moduleName, e.getMessage());
				}
			}

		};
	}
	
}
