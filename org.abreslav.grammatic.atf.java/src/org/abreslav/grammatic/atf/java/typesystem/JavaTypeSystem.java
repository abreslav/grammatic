package org.abreslav.grammatic.atf.java.typesystem;

import java.util.Set;

import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.eclipse.emf.ecore.EGenericType;

public abstract class JavaTypeSystem implements ITypeSystem<EGenericType> {

	@Override
	public Set<EGenericType> getAllTypes() {
		// Add java.lang
		// Add java.util
		// Add primitive types
		//-------------------------------------
		// Add types declared somewhere
		// Yes, we add only some instantiations for generic classes, not all
		//  this damages inference, but we postpone this problem
		// Where to declare types? We just collect types which are referred to in the 
		// particular definition
		// TODO: How to reuse declarations of classes (not types)
		// TODO: How to refer to declarations of classes
		return null;
	}

	@Override
	public EGenericType getCollectionElementType(EGenericType type) {
		// type must refer to a classifier derived from java.util.Collection
		// result is a value of type parameter of Collection as a superclass
		// For raw types return java.lang.Object (TODO: check this)
		return null;
	}
	
	@Override
	public EGenericType getStringType() {
		// Return java.lang.String
		return null;
	}

	@Override
	public ISubtypingRelation<EGenericType> getSubtypingRelation() {
		// Obtained from declarations
		// Special rules for Void and Object
		// Careful with wildcards
		// TODO: Primitive types inherit from wrappers and vice versa 
		// (it's easier to always use wrappers, although we'd prefer 
		// to preserve primitives when possible)
		// TODO: Consider EClass vs EDataType...
		return null;
	}

	@Override
	public EGenericType getTopType() {
		// return java.lang.Object
		return null;
	}

	@Override
	public boolean isCollection(EGenericType type) {
		// look for java.lang.Collection if getEAllSuperTypes()
		return false;
	}
	
	@Override
	public String getStringRepresentation(EGenericType type) {
		// implemented somewhere (see model template project)
		return null;
	}

}
