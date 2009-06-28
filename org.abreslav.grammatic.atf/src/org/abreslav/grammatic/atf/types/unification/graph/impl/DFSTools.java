package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class DFSTools {

	@SuppressWarnings("unchecked")
	private static final IRepresentativeChooser myDefaultRepresentativeChooser = new IRepresentativeChooser() {
		
		@Override
		public Object chooseRepresentative(Set component) {
			return component.iterator().next();
		}
		
	};
	
	@SuppressWarnings("unchecked")
	public static <V> IRepresentativeChooser<V> getDefaultRepresentativeChooser() {
		return myDefaultRepresentativeChooser;
	}

	public static <V> List<V> topologicalSort(IGraph<V> graph, final ICycleHandler<V> cycleHandler) {
		final List<V> result = new LinkedList<V>();
		DepthFirstSearcher.dfs(graph, StandardPolicies.SUCCESSORS, new DFSHandlerAdapter<V>() {
			@Override
			public void afterAllChildren(IGraph<V> graph, V vertex, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				result.add(0, vertex);
			};
			
			@Override
			public void cycleDetected(IGraph<V> graph, V a, V b, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				cycleHandler.cycleDetected(graph, a, b, started, finished, successorPolicy);
			};
		});
		return result;
	}

	public static <V> boolean findConnectedComponents(IGraph<V> graph, final List<Set<V>> components, List<V> topologicalOrder) {
		final Flag nontrivialComponent = new Flag(false);
		DepthFirstSearcher.dfsOnCollection(graph, topologicalOrder, StandardPolicies.PREDECESSORS, new DFSHandlerAdapter<V>() {
			private Set<V> myCurrentComponent = new HashSet<V>(1);
			
			@Override
			public void afterSubgraph(IGraph<V> graph, V vertex, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				components.add(myCurrentComponent);
				if (myCurrentComponent.size() > 1) {
					nontrivialComponent.set(true);
				}
				myCurrentComponent = new HashSet<V>(1);
			};
			
			@Override
			public void beforeAllChildren(IGraph<V> graph, V vertex, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				myCurrentComponent.add(vertex);
			};
		});
		return nontrivialComponent.isTrue(); 
	}
	
	public static <V> boolean buildCondensation(
			IGraph<V> graph, 
			IGraph<V> condensation,
			Map<V, V> vertexToRepresentative,
			ICycleHandler<V> cycleHandler) {
		List<V> topologicalOrder = topologicalSort(graph, cycleHandler);
		IRepresentativeChooser<V> defaultRepresentativeChooser = getDefaultRepresentativeChooser();
		return buildCondensationFromTopologicalOrder(graph, condensation,
				vertexToRepresentative, defaultRepresentativeChooser, topologicalOrder);
	}

	public static <V> boolean buildCondensation(
			IGraph<V> graph, 
			IGraph<V> condensation,
			Map<V, V> vertexToRepresentative,
			ICycleHandler<V> cycleHandler,
			IRepresentativeChooser<V> defaultRepresentativeChooser) {
		List<V> topologicalOrder = topologicalSort(graph, cycleHandler);
		return buildCondensationFromTopologicalOrder(graph, condensation,
				vertexToRepresentative, defaultRepresentativeChooser, topologicalOrder);
	}
	
	public static <V> boolean buildCondensationFromTopologicalOrder(
			IGraph<V> graph, IGraph<V> condensation,
			Map<V, V> vertexToRepresentative, 
			IRepresentativeChooser<V> representativeChooser, 
			List<V> topologicalOrder) {
		List<Set<V>> components = new ArrayList<Set<V>>();
		if (!findConnectedComponents(graph, components, topologicalOrder)) {
			return false;
		}
		buildCondensation(graph, condensation, components, vertexToRepresentative, representativeChooser);
		return true;
	}

	public static <V> List<V> buildCondensation(
			IGraph<V> graph,
			IGraph<V> condensation, 
			List<Set<V>> components, 
			Map<V, V> vertexToRepresentative, 
			IRepresentativeChooser<V> representativeChooser) {
		// NOTE: the list of components is in topological order
		// so the first component has no incoming edges
		// the second has edges only from the first, etc.
		List<V> topologicalOrder = new ArrayList<V>();
		for (Set<V> component : components) {
			V representative = representativeChooser.chooseRepresentative(component);
			topologicalOrder.add(representative);
			condensation.addVertex(representative);
			for (V v : component) {
				vertexToRepresentative.put(v, representative);
			}
			for (V vertex : component) {
				for (V predecessor : graph.getPredecessors(vertex)) {
					V predecessorRepresentative = vertexToRepresentative.get(predecessor);
					if (representative != predecessorRepresentative) {
						condensation.addEdge(predecessorRepresentative, representative);
					}
				}
			}
		}
		return topologicalOrder;
	}
	
	public static <V> void transitivelyClose(IGraph<V> graph) {
		DepthFirstSearcher.dfs(graph, StandardPolicies.SUCCESSORS, new DFSHandlerAdapter<V>() {
			@Override
			public void afterChild(IGraph<V> graph, V parent, V child, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				Set<V> reachableFromChild = graph.getSuccessors(child);
				for (V v : reachableFromChild) {
					graph.addEdge(parent, v);
				}
			};
			
			@Override
			public void afterAllChildren(IGraph<V> graph, V vertex, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				graph.addEdge(vertex, vertex);
			};
			
			@Override
			public void cycleDetected(IGraph<V> graph, V a, V b, Set<V> started, Set<V> finished, ISuccessorPolicy successorPolicy) {
				throw new IllegalStateException("This graph must be acyclic!");
			};
		});
	}
	
}
