package org.abreslav.grammatic.grammar.template.parser;


public class SymbolInstantiatorKeyConvertor implements IKeyConvertor {

	private final String myModuleName;
	
	public SymbolInstantiatorKeyConvertor(String moduleName) {
		super();
		myModuleName = moduleName;
	}

	@Override
	public IKey convert(IKey key) {
		if (key instanceof TemplateKey) {
			return DoubleKey.createKey(myModuleName, key.getSimpleName());
		}
		return key;
	}

}
