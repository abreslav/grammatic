package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public enum StandardPolicies implements ISuccessorPolicy {

	SUCCESSORS {
		public <V> Set<V> getSuccessors(IGraph<V> graph, V vertex) {
			return graph.getSuccessors(vertex);
		};
		
	},
	PREDECESSORS  {
		public <V> Set<V> getSuccessors(IGraph<V> graph, V vertex) {
			return graph.getPredecessors(vertex);
		};
		
	};
}
