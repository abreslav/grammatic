package org.abreslav.grammatic.atf.generator;

import java.io.IOException;

import org.abreslav.grammatic.grammar.template.parser.IModuleLoader;
import org.abreslav.grammatic.grammar1.IImportsModuleImplementationProvider;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;

public class ImportsModuleImplementationProvider implements
		IImportsModuleImplementationProvider {
	
	private final IModuleLoader myModuleLoader;
	private final IErrorHandler<? extends RuntimeException> myErrorHandler;
	
	public ImportsModuleImplementationProvider(IModuleLoader moduleLoader,
			IErrorHandler<? extends RuntimeException> errorHandler) {
		myErrorHandler = errorHandler;
		myModuleLoader = moduleLoader;
	}

	@Override
	public IRenamingFunctions getRenamingFunctions() {
		return new IRenamingFunctions() {
			
			@Override
			public String null1() {
				return null;
			}
			
			@Override
			public void addRenaming(IRenamingManager manager, String moduleName,
					String old, String new1) {
				manager.addRenaming(moduleName, new1, old);
			}
		};
	}

	@Override
	public IImportSpecFunctions getImportSpecFunctions() {
		return new IImportSpecFunctions() {
			
			@Override
			public void loadModule(String moduleName) {
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