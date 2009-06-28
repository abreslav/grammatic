package org.abreslav.grammatic.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.cfgraph.ControlFlowEdge;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.eclipse.emf.common.util.EList;

public class TopologicalOrder {

	private final Map<ControlFlowVertex, List<ControlFlowVertex>> myVertices = new HashMap<ControlFlowVertex, List<ControlFlowVertex>>();
	
	public TopologicalOrder(ControlFlowGraph graph, boolean ignoreBackwardEdges) {
		EList<StartVertex> startVertices = graph.getStartVertices();
		Set<ControlFlowVertex> visited = new HashSet<ControlFlowVertex>();
		for (StartVertex startVertex : startVertices) {
			visited.clear();
			LinkedList<ControlFlowVertex> list = new LinkedList<ControlFlowVertex>();
			myVertices.put(startVertex, list);
			dfs(startVertex, list, visited, ignoreBackwardEdges);
		}
	}

	private void dfs(ControlFlowVertex vertex, LinkedList<ControlFlowVertex> list, Set<ControlFlowVertex> visited, boolean ignoreBackwardEdges) {
		if (visited.contains(vertex)) {
			return;
		}
		visited.add(vertex);
		Collection<ControlFlowEdge> outgoingEdges = VertexUtil.getOutgoingEdges(vertex);
		for (ControlFlowEdge edge : outgoingEdges) {
			if (!ignoreBackwardEdges || !edge.isBackward()) {
				dfs(edge.getEnd(), list, visited, ignoreBackwardEdges);
			}
		}
		if (vertex instanceof EndVertex) {
			myVertices.put(vertex, list);
			list.add(vertex);
		} else {
			list.add(0, vertex);
		}
	}
	
	public Iterator<ControlFlowVertex> iterator(StartVertex vertex) {
		return Collections.unmodifiableCollection(myVertices.get(vertex)).iterator();
	}
	
	public Iterator<ControlFlowVertex> iterator(EndVertex vertex) {
		List<ControlFlowVertex> list = myVertices.get(vertex);
		final ListIterator<ControlFlowVertex> iterator = list.listIterator(list.size() - 1);
		iterator.next();
		return new Iterator<ControlFlowVertex>() {

			@Override
			public boolean hasNext() {
				return iterator.hasPrevious();
			}

			@Override
			public ControlFlowVertex next() {
				return iterator.previous();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
}
