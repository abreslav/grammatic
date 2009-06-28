package org.abreslav.grammatic.atf.interpreter;

import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.atf.parser.IOptions;
import org.abreslav.grammatic.atf.parser.ITypeSystemBuilder;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystem;
import org.abreslav.grammatic.utils.IHashingStrategy;

class TestTypeSystemBuilder implements ITypeSystemBuilder<String> {

	private final Set<String> myTypes = new HashSet<String>();
	
	public TestTypeSystemBuilder() {
		myTypes.add("NULL");
	}
	
	@Override
	public void addType(Object type) {
		myTypes.add(type.toString());
	}

	@Override
	public void closeModule(String moduleName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadTypeSystemModule(String moduleName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openModule(String moduleName, IOptions options) {
		
	}

	@Override
	public <E extends Throwable> ITypeSystem<String> getTypeSystem(
			ITypeErrorHandler<E> errorHandler) throws E {
		return new FiniteTypeSystem<String>(
				myTypes,
				new ISubtypingRelation<String>() {
					
					@Override
					public boolean isSubtypeOf(String type, String supertype) {
						if ("NULL".equals(type)) {
							return true;
						}
						if ("TupleValue".equals(type) && "Value".equals(supertype)) {
							return true;
						}
						return type.equals(supertype);
					}
					
				},
				IHashingStrategy.DEFAULT,
				IStringRepresentationProvider.DEFAULT,
				errorHandler
		);
	}

	@Override
	public Object getStringType() {
		return "String";
	}
}