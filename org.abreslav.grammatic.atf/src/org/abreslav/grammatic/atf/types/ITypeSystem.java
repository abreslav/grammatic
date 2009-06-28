package org.abreslav.grammatic.atf.types;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ISuccessorPolicy;

public interface ITypeSystem<T> extends IStringRepresentationProvider<T> {

	boolean isCollection(T type);
	T getCollectionElementType(T type);
	ISubtypingRelation<T> getSubtypingRelation();
	T getTopType();
	Set<T> getAllTypes();
	int getTopologicalIndex(T type);
	T getTypeByTopologicalIndex(int index);
	int size();
	Set<T> getSupertypes(T type);
	Set<T> getSubtypes(T type);
	Set<T> getNeighbours(T type, ISuccessorPolicy policy);
	Set<T> getCommonSubtypes(Set<T> superTypes);
	Set<T> getCommonSupertypes(Set<T> subTypes);
	T getStringType();
}
