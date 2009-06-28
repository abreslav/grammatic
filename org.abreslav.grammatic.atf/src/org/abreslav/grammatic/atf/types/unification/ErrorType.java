package org.abreslav.grammatic.atf.types.unification;

public enum ErrorType {
	NO_TYPE_FOR(1, "No type for variable %s"),
	MANY_TYPES_FOR(-1, "There are many possible values for %s : %s") {
		@Override
		public String getErrorMessage(Object... typeValues) {
			return String.format(myMessage, typeValues[0], commaSeparatedList(1, typeValues.length, typeValues));
		}
	},
	EXPLICIT_TYPES_DO_NOT_MATCH(2, "Types do not match: %s and %s"),
	NO_EXPLICIT_BOUNDS_NO_TOP_TYPE(2, "No explicit bounds for %s, there's no top type to assign"),
	NO_COMMON_SUPERTYPE(-1, "Types %s have no common supertype") {
		@Override
		public String getErrorMessage(Object... typeValues) {
			return String.format(myMessage, commaSeparatedList(0, typeValues.length, typeValues));
		}

	},
	NOT_A_COMMON_SUBTYPE(-1, "A complicated closed constraint for types %s is unsatisfiable") {
		@Override
		public String getErrorMessage(Object... typeValues) {
			return String.format(myMessage, commaSeparatedList(0, typeValues.length, typeValues));
		}
	},
	;
	
	private final int myNumberOfParameters;
	protected final String myMessage;

	private ErrorType(int numberOfParameters, String message) {
		myMessage = message;
		myNumberOfParameters = numberOfParameters;
	}
	
	public String getErrorMessage(Object... typeValues) {
		if (typeValues.length != myNumberOfParameters) {
			throw new IllegalArgumentException("Argument numbers do not match");
		}
		return String.format(myMessage, typeValues);
	}

	private static String commaSeparatedList(int from, int upTo, Object[] typeValues) {
		StringBuilder builder = new StringBuilder();
		builder.append(typeValues[0]);
		for (int i = from; i < upTo; i++) {
			builder.append(typeValues[i]);
		}
		return builder.toString();
	}
}
