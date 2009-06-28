package org.abreslav.grammatic.atf.types.unification.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ExplicitType;
import org.abreslav.grammatic.atf.types.unification.IClosedConstraint;
import org.abreslav.grammatic.atf.types.unification.ITypeValue;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.DFSHandlerAdapter;
import org.abreslav.grammatic.atf.types.unification.graph.impl.DepthFirstSearcher;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ISuccessorPolicy;
import org.abreslav.grammatic.atf.types.unification.graph.impl.StandardPolicies;

public class ClosedConstraintsBuilder {

	public static <T> Map<TypeVariable, IClosedConstraint<T>> buildClosedConstraints(
			IGraph<ITypeValue> graph, Collection<ExplicitType<T>> explicitTypes) {
		final Map<TypeVariable, IClosedConstraint<T>> constraints = new HashMap<TypeVariable, IClosedConstraint<T>>();
		
		Set<ITypeValue> started = new HashSet<ITypeValue>();
		Set<ITypeValue> finished = new HashSet<ITypeValue>();
		
		for (final ExplicitType<T> explicitType : explicitTypes) {
		
			Set<ITypeValue> successors = graph.getSuccessors(explicitType);
			final StandardPolicies policy = 
				successors.isEmpty() 
					? StandardPolicies.PREDECESSORS 
					: StandardPolicies.SUCCESSORS;
			
			started.clear();
			finished.clear();
			DepthFirstSearcher.dfsStep(graph, explicitType, started, finished, policy, new DFSHandlerAdapter<ITypeValue>() {
			
				@Override
				public void beforeAllChildren(IGraph<ITypeValue> graph,	ITypeValue vertex, Set<ITypeValue> started,	Set<ITypeValue> finished, ISuccessorPolicy successorPolicy) {
					if (vertex instanceof TypeVariable) {
						IClosedConstraint<T> constraint = getConstraint((TypeVariable) vertex, constraints);
						if (policy == StandardPolicies.SUCCESSORS) {
							constraint.addLowerBound(explicitType);
						} else {
							constraint.addUpperBound(explicitType);
						}
					}
				}
				
			});
			
		}
		
		return constraints;
	}
	
	private static <T> IClosedConstraint<T> getConstraint(TypeVariable variable, Map<TypeVariable, IClosedConstraint<T>> map) {
		IClosedConstraint<T> constraint = map.get(variable);
		if (constraint == null) {
			constraint = new ClosedConstraint<T>();
			map.put(variable, constraint);
		}
		return constraint;
	}
}
