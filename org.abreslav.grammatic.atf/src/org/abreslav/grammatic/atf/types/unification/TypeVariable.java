package org.abreslav.grammatic.atf.types.unification;

public class TypeVariable implements ITypeValue {

	private final String myName;

	public TypeVariable(String name) {
		myName = name;
	}
	
	@Override
	public String toString() {
		return "<" + myName + ">";
	}
}
