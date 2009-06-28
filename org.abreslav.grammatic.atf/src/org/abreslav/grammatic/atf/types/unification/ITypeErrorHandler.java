package org.abreslav.grammatic.atf.types.unification;

public interface ITypeErrorHandler<E extends Throwable> {
	public static final ITypeErrorHandler<RuntimeException> ERROR_HANDLER = new ITypeErrorHandler<RuntimeException>() {
		
		@Override
		public void reportError(ErrorType errorType,
				Object... typeValues) throws RuntimeException {
			throw new IllegalArgumentException(errorType.getErrorMessage(typeValues));
		}
		
		@Override
		public void reportWarning(WarningType errorType,
				Object... typeValues) {
			System.err.println(errorType.getErrorMessage(typeValues));
		}
		
	};
	void reportError(ErrorType errorType, Object... typeValues) throws E;
	void reportWarning(WarningType warningType, Object... typeValues);
}
