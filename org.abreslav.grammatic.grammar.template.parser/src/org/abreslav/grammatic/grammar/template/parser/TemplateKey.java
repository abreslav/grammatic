package org.abreslav.grammatic.grammar.template.parser;


public class TemplateKey implements IKey {

	public static final IKey createKey(String debugInfo, String name) {
		return new TemplateKey(debugInfo, name);
	}
	
	private final String myDebugInfo;
	private final String mySimpleName;
	
	private TemplateKey(String debugInfo, String simpleName) {
		myDebugInfo = debugInfo;
		mySimpleName = simpleName;
	}

	@Override
	public String toString() {
		return myDebugInfo + "::" + mySimpleName;
	}
	
	@Override
	public String getSimpleName() {
		return mySimpleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mySimpleName == null) ? 0 : mySimpleName.hashCode());
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
		TemplateKey other = (TemplateKey) obj;
		if (mySimpleName == null) {
			if (other.mySimpleName != null)
				return false;
		} else if (!mySimpleName.equals(other.mySimpleName))
			return false;
		return true;
	}

}
