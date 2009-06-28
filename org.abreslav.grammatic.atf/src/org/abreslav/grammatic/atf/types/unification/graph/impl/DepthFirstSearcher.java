package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class DepthFirstSearcher {

	public static <V> void dfs(IGraph<V> graph, ISuccessorPolicy successorPolicy,
			IDFSHandler<V> dfsHandler) {
		dfsOnCollection(graph, graph.getAllVertices(), successorPolicy,
				dfsHandler);
	}

	public static <V> void dfsOnCollection(IGraph<V> graph, Collection<V> vertices,
			ISuccessorPolicy successorPolicy, IDFSHandler<V> dfsHandler) {
		Set<V> started = new HashSet<V>();
		Set<V> finished = new HashSet<V>();
		for (V v : vertices) {
			if (!finished.contains(v)) {
				if (started.contains(v)) {
					throw new IllegalStateException("A bug detected: a vertex cannot be started and not finished here");
				}
				dfsHandler.beforeSubgraph(graph, v, started, finished, successorPolicy);
				dfsStep(graph, v, started, finished, successorPolicy, dfsHandler);
				dfsHandler.afterSubgraph(graph, v, started, finished, successorPolicy);
			}
		}
	}

	public static <V> void dfsStep(IGraph<V> graph, V vertex, Set<V> started, Set<V> finished,
			ISuccessorPolicy successorPolicy, IDFSHandler<V> dfsHandler) {
		started.add(vertex);
		dfsHandler.beforeAllChildren(graph, vertex, started, finished, successorPolicy);
		Set<V> successors = successorPolicy.getSuccessors(graph, vertex);
		for (V v : successors) {
			if (started.contains(v)) {
				dfsHandler.cycleDetected(graph, vertex, v, started, finished, successorPolicy);
				continue;
			}
			if (!finished.contains(v)) {
				dfsHandler.beforeChild(graph, vertex, v, started, finished,
						successorPolicy);
				dfsStep(graph, v, started, finished, successorPolicy, dfsHandler);
			}
			// This is needed regardless to finished set
			// When we calculate reachability sets we need to process this child
			// The overall idea is that you always visit all the children
			dfsHandler.afterChild(graph, vertex, v, started, finished, 
					successorPolicy);
		}
		dfsHandler.afterAllChildren(graph, vertex, started, finished, successorPolicy);
		started.remove(vertex);
		finished.add(vertex);
	}

}
