package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;

import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.VariableDefinition;

public interface IWildcardHandler<E> {
	void processWildcardVariable(IVariableValues variables, Collection<E> unmatched, VariableDefinition variable, Query variableValue);
}
