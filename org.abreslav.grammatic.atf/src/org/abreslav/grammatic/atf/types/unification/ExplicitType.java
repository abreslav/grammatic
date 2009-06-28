package org.abreslav.grammatic.atf.types.unification;

public class ExplicitType<T> implements ITypeValue {

	private final T myType;

	public ExplicitType(T type) {
		myType = type;
	}
	
	public T getType() {
		return myType;
	}
	
	@Override
	public String toString() {
		return "[" + myType.toString() + "]::" + System.identityHashCode(this);
	}

}
