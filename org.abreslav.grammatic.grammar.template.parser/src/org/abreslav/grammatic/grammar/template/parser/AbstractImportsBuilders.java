package org.abreslav.grammatic.grammar.template.parser;

import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.utils.IErrorHandler;

public abstract class AbstractImportsBuilders implements IImportsBuilders {

	private final Map<String, IKey> myRenamings = new HashMap<String, IKey>();
	protected final IErrorHandler<? extends RuntimeException> myErrorHandler;


	public AbstractImportsBuilders(
			IErrorHandler<? extends RuntimeException> errorHandler) {
		super();
		myErrorHandler = errorHandler;
	}

	public IKey resolveName(String name) {
		return myRenamings.get(name);
	}

	@Override
	public IRenamingBuilder getRenamingBuilder() {
		return new IRenamingBuilder() {
	
			private String myModule;
	
			@Override
			public void init(String fileName) {
				myModule = fileName;
			}
	
			@Override
			public void release() {
				myModule = null;
			}
	
			@Override
			public void renaming(String newName, String oldName) {
				if (newName == null) {
					newName = oldName;
				}
				if (myRenamings.containsKey(newName)) {
					myErrorHandler.reportError("Name used twice: %s", newName);
				}
				IKey key = DoubleKey.createKey(myModule, oldName);
				myRenamings.put(newName, key);
			}
			
		};
	}

	@Override
	public IModuleNameBuilder getModuleNameBuilder() {
		return new IModuleNameBuilder() {
	
			@Override
			public void init() {
			}
	
			@Override
			public void release() {
			}
			
			@Override
			public String moduleName(String string) {
				return string.substring(1, string.length() - 1);
			}
	
		};
	}

}