package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.query.Query;

public interface IMatcher<Q extends Query, E> {
	boolean match(Q query, E element, IVariableValues variables);
}
