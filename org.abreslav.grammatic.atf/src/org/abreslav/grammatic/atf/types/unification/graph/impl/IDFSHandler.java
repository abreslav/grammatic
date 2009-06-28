package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public interface IDFSHandler<V> extends ICycleHandler<V> {
	void beforeSubgraph(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);

	void afterSubgraph(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);

	void beforeAllChildren(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);

	void beforeChild(IGraph<V> graph, V parent, V child, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);

	void afterChild(IGraph<V> graph, V parent, V child, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);

	void afterAllChildren(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy);
}
