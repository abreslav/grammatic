package org.abreslav.grammatic.atf.types.unification.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ITypeValue;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class ImplicitConstraintPropagator {
	public static <T> void propagateImplicitConstraints(Map<TypeVariable, Set<T>> possibleValues, IGraph<ITypeValue> graph) {
		Set<TypeVariable> constrainedVariables = possibleValues.keySet();
		Map<TypeVariable, Set<T>> implicitPossibleValues = new HashMap<TypeVariable, Set<T>>();
		for (TypeVariable typeVariable : constrainedVariables) {
			Set<T> values = possibleValues.get(typeVariable);
			HashSet<TypeVariable> closed = new HashSet<TypeVariable>(constrainedVariables);
			closed.remove(typeVariable);
			propagate(typeVariable, graph, closed, values, implicitPossibleValues);
			implicitPossibleValues.remove(typeVariable);
		}
		possibleValues.putAll(implicitPossibleValues);
	}
	
	public static <T> void propagate(
			TypeVariable typeVariable,
			IGraph<ITypeValue> graph,
			Set<TypeVariable> closed,
			Set<T> propagated, 
			Map<TypeVariable, Set<T>> possibleValues) {
		if (closed.contains(typeVariable)) {
			return;
		}
		closed.add(typeVariable);
		Set<T> values = possibleValues.get(typeVariable);
		if (values == null) {
			values = new HashSet<T>(propagated);
			possibleValues.put(typeVariable, values);
		} else {
			values.retainAll(propagated);
		}
		Set<ITypeValue> successors = graph.getSuccessors(typeVariable);
		for (ITypeValue typeValue : successors) {
			if (typeValue instanceof TypeVariable) {
				propagate((TypeVariable) typeValue, graph, closed, propagated, possibleValues);
			}
		}
		Set<ITypeValue> predecessors = graph.getPredecessors(typeVariable);
		for (ITypeValue typeValue : predecessors) {
			if (typeValue instanceof TypeVariable) {
				propagate((TypeVariable) typeValue, graph, closed, propagated, possibleValues);
			}
		}
	}
}
