package org.abreslav.grammatic.atf.types.unification;

public enum WarningType {
	NO_EXPLICIT_BOUNDS_TOP_USED(2, "No explicit bounds for %s, the top type %s is used"),
	SUBTYPING_CYCLE_FOUND(2, "A subtyping cycle is found: %s is a subtype of %s and vice versa"),
	;
	
	private final int myNumberOfParameters;
	private final String myMessage;

	private WarningType(int numberOfParameters, String message) {
		myMessage = message;
		myNumberOfParameters = numberOfParameters;
	}
	
	public String getErrorMessage(Object... typeValues) {
		if (typeValues.length != myNumberOfParameters) {
			throw new IllegalArgumentException("Argument numbers do not match");
		}
		return String.format(myMessage, typeValues);
	}
}
