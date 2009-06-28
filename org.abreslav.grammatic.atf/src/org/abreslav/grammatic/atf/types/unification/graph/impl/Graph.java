package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.utils.CustomLinkedHashMap;
import org.abreslav.grammatic.utils.CustomLinkedHashSet;
import org.abreslav.grammatic.utils.IHashingStrategy;

public class Graph<V> implements IGraph<V> {
	
	private static final class Vertex<V> {
		private final Set<V> mySuccessors;
		private final Set<V> myPredecessors;
		private final Set<V> mySuccessorsReadOnly;
		private final Set<V> myPredecessorsReadOnly;
		
		public Vertex(IHashingStrategy hashingStrategy) {
			mySuccessors = new CustomLinkedHashSet<V>(hashingStrategy);
			myPredecessors = new CustomLinkedHashSet<V>(hashingStrategy);
			mySuccessorsReadOnly = Collections.unmodifiableSet(mySuccessors);
			myPredecessorsReadOnly = Collections.unmodifiableSet(myPredecessors);
		}
		
		public Vertex() {
			mySuccessors = new LinkedHashSet<V>();
			myPredecessors = new LinkedHashSet<V>();
			mySuccessorsReadOnly = Collections.unmodifiableSet(mySuccessors);
			myPredecessorsReadOnly = Collections.unmodifiableSet(myPredecessors);
		}
	}

	private final Map<V, Vertex<V>> myVertices;
	private final Set<V> myVerticesReadOnly;
	private final IHashingStrategy myHashingStrategy;
	
	public Graph(IHashingStrategy hashingStrategy) {
		myHashingStrategy = hashingStrategy;
		myVertices = new CustomLinkedHashMap<V, Vertex<V>>(hashingStrategy);
		myVerticesReadOnly = Collections.unmodifiableSet(myVertices.keySet());
	}

	public Graph() {
		myHashingStrategy = null;
		myVertices = new LinkedHashMap<V, Vertex<V>>();
		myVerticesReadOnly = Collections.unmodifiableSet(myVertices.keySet());
	}
	
	private Vertex<V> assertMyVertex(V vertex) {
		Vertex<V> result = myVertices.get(vertex);
		if (result == null) {
			throw new IllegalArgumentException("Not my vertex: " + vertex);
		}
		return result;
	}
	
	@Override
	public void addEdge(V from, V to) {
		Vertex<V> fromVertex = assertMyVertex(from);
		Vertex<V> toVertex = assertMyVertex(to);
		
		fromVertex.mySuccessors.add(to);
		toVertex.myPredecessors.add(from);
	}

	@Override
	public void addVertex(V vertex) {
		if (myVertices.containsKey(vertex)) {
			return;
		}
		Vertex<V> vertexWrapper;
		if (myHashingStrategy == null) {
			vertexWrapper = new Vertex<V>();
		} else {
			vertexWrapper = new Vertex<V>(myHashingStrategy);
		}
		myVertices.put(vertex, vertexWrapper);
	}

	@Override
	public Set<V> getPredecessors(V vertex) {
		return assertMyVertex(vertex).myPredecessorsReadOnly;
	}

	@Override
	public Set<V> getSuccessors(V vertex) {
		return assertMyVertex(vertex).mySuccessorsReadOnly;
	}
	
	@Override
	public Set<V> getAllVertices() {
		return myVerticesReadOnly;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (V v : getAllVertices()) {
			builder.append(v.toString()).append(" -> ");
			for (V successor : getSuccessors(v)) {
				builder.append(successor.toString()).append(" ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
}
