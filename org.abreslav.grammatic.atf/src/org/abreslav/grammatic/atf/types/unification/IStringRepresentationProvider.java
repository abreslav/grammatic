package org.abreslav.grammatic.atf.types.unification;

public interface IStringRepresentationProvider<T> {
	
	IStringRepresentationProvider<Object> DEFAULT = new IStringRepresentationProvider<Object>() {

		@Override
		public String getStringRepresentation(Object type) {
			return type.toString();
		}
		
	};
	
	String getStringRepresentation(T type);
}
