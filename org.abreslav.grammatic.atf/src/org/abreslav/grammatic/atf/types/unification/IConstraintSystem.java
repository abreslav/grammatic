package org.abreslav.grammatic.atf.types.unification;

import java.util.Map;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public interface IConstraintSystem<T, E extends Throwable> {
	void addConstraint(TypeVariable less, TypeVariable greater) throws E;
	void addConstraint(TypeVariable less, T greater) throws E;
	void addConstraint(T less, TypeVariable greater) throws E;
	void addConstraint(T less, T greater) throws E;
	
	IGraph<ITypeValue> getConstraintGraph(Map<ITypeValue, ITypeValue> variableToRepresentative);
}
