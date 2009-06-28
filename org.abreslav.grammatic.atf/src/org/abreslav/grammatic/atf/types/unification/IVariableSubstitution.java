package org.abreslav.grammatic.atf.types.unification;

public interface IVariableSubstitution<T> {
	T substitute(ITypeValue typeValue);
}
