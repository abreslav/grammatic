/**
 * 
 */
package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public interface ISuccessorPolicy {
	<V> Set<V> getSuccessors(IGraph<V> graph, V vertex);
}