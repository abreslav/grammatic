package org.abreslav.grammatic.query.interpreter.graphmatching;

import java.util.Collection;

public interface IGraphMatcher {

	<T extends IEdge> Collection<T> findMaximumMatching(Collection<T>[] left, Collection<T>[] right);

	public <T extends IEdge> void enumMaximumMatchings(Collection<T>[] left,
			Collection<T>[] right, int matchingSize, IMatchingHandler<T> handler); 
}
