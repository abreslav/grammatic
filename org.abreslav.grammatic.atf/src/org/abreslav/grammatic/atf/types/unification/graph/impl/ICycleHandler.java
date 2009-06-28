package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public interface ICycleHandler<V> {
	void cycleDetected(IGraph<V> graph, V a, V b, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);
}
