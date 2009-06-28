package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class DFSHandlerAdapter<V> implements IDFSHandler<V> {

	@Override
	public void afterAllChildren(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void afterChild(IGraph<V> graph, V parent, V child, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void afterSubgraph(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void beforeAllChildren(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void beforeChild(IGraph<V> graph, V parent, V child, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void beforeSubgraph(IGraph<V> graph, V vertex, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}

	@Override
	public void cycleDetected(IGraph<V> graph, V a, V b, Set<V> started,
			Set<V> finished, ISuccessorPolicy successorPolicy) {
		
	}
}