package org.abreslav.grammatic.atf.types.unification.graph;

import java.util.Set;

public interface IGraph<V> {

	Set<V> getAllVertices();
	void addVertex(V vertex);
	void addEdge(V from, V to);
	
	Set<V> getSuccessors(V vertex);
	Set<V> getPredecessors(V vertex);
}
