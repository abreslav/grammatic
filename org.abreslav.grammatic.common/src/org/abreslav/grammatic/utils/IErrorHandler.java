package org.abreslav.grammatic.utils;

public interface IErrorHandler<E extends Throwable> {

	IErrorHandler<RuntimeException> EXCEPTION = new IErrorHandler<RuntimeException>() {

		@Override
		public void reportError(String string, Object... objects) {
			throw new IllegalArgumentException(String.format(string, objects));
		}
		
	};
	
	void reportError(String messageTemplate, Object... objects) throws E;
}
