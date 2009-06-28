package org.abreslav.grammatic.query.interpreter.graphmatching;

import java.util.Collection;

public interface IMatchingHandler<T extends IEdge> {

	boolean handleMatching(Collection<T> edges);
}
