package org.abreslav.grammatic.atf.parser;

import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;

public interface ITypeSystemBuilder<T> {

	void addType(Object type);

	void openModule(String moduleName, IOptions options);

	void closeModule(String moduleName);

	void loadTypeSystemModule(String moduleName);
	
	public <E extends Throwable> ITypeSystem<T> getTypeSystem(ITypeErrorHandler<E> errorHandler) throws E;

	Object getStringType();
}
