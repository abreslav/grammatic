package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;

public interface IMatchHandler {

	/**
	 * 
	 * @return {@code false} if no more traversing is needed, {@code true} otherwise
	 */
	boolean queryMatched(QueryContainer<? extends Query> queryContainer, VariableValues variableValues);
}
