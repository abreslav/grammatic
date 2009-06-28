package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;
import java.util.Iterator;

import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.variables.VariableValue;

public interface IVariableValues {
	VariableValue getVariableValue(VariableDefinition variable);

	void setVariableValue(VariableDefinition variable, VariableValue value);

	int getValuesCount(VariableDefinition variable);
	Collection<VariableValue> getAllVariableValues(VariableDefinition variable);
	void addVariableValue(VariableDefinition variable, VariableValue value);

	void clear();

	void putAllVariables(IVariableValues variables);

	Iterator<VariableDefinition> variableIterator();
}
