package org.abreslav.grammatic.grammar.template.parser;


public final class DoubleKey implements IKey {

	public static IKey createKey(String moduleName, String symbolName) {
		return new DoubleKey(moduleName, symbolName);
	}
	
	private final String myModuleName;
	private final String mySymbolName;
	
	private DoubleKey(String moduleName, String symbolName) {
		if (moduleName == null || symbolName == null) {
			throw new IllegalArgumentException("Module or symbol name cannot be null");
		}
		myModuleName = moduleName;
		mySymbolName = symbolName;
	}
	
	public String getModuleName() {
		return myModuleName;
	}
	
	public String getSimpleName() {
		return mySymbolName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((myModuleName == null) ? 0 : myModuleName.hashCode());
		result = prime * result
				+ ((mySymbolName == null) ? 0 : mySymbolName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoubleKey other = (DoubleKey) obj;
		if (myModuleName == null) {
			if (other.myModuleName != null)
				return false;
		} else if (!myModuleName.equals(other.myModuleName))
			return false;
		if (mySymbolName == null) {
			if (other.mySymbolName != null)
				return false;
		} else if (!mySymbolName.equals(other.mySymbolName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return myModuleName + "::" + mySymbolName;
	}
	
}
