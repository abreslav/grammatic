package org.abreslav.grammatic.atf.types.unification;

public interface ISubtypingRelation<T> {
	boolean isSubtypeOf(T type, T supertype);
}
