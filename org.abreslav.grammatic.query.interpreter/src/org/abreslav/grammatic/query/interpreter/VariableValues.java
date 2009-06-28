package org.abreslav.grammatic.query.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.variables.VariableValue;

public class VariableValues implements IVariableValues {

	private final Map<VariableDefinition, VariableValue> myValues = new LinkedHashMap<VariableDefinition, VariableValue>();
	private final Map<VariableDefinition, VariableValue> myValuesReadOnly = Collections.unmodifiableMap(myValues);
	private final Map<VariableDefinition, Collection<VariableValue>> myAllValues = new LinkedHashMap<VariableDefinition, Collection<VariableValue>>();

	@Override
	public VariableValue getVariableValue(VariableDefinition variable) {
		return myValues.get(variable);
	}

	@Override
	public void setVariableValue(VariableDefinition variable, VariableValue value) {
		myValues.put(variable, value);
		addVariableValue(variable, value);
	}

	@Override
	public void addVariableValue(VariableDefinition variable,
			VariableValue value) {
		if (!myValues.containsKey(variable)) {
			throw new IllegalStateException("Value added before it is set: " + variable.getName());
		}
		Collection<VariableValue> values = getAllValues(variable);
		values.add(value);
	}

	private Collection<VariableValue> getAllValues(VariableDefinition variable) {
		Collection<VariableValue> values = myAllValues.get(variable);
		if (values == null) {
			values = new ArrayList<VariableValue>();
			myAllValues.put(variable, values);
		}
		return values;
	}

	@Override
	public void clear() {
		myValues.clear();
		myAllValues.clear();
	}

	@Override
	public void putAllVariables(IVariableValues variables) {
		for (Iterator<VariableDefinition> it = variables.variableIterator(); it.hasNext(); ) {
			VariableDefinition var = it.next();
			
			setVariableValue(var, variables.getVariableValue(var));
			
			Collection<VariableValue> allValues = getAllValues(var);
			allValues.clear();
			allValues.addAll(variables.getAllVariableValues(var));
		}
	}

	@Override
	public Iterator<VariableDefinition> variableIterator() {
		return myValuesReadOnly.keySet().iterator();
	}

	@Override
	public Collection<VariableValue> getAllVariableValues(VariableDefinition variable) {
		return Collections.unmodifiableCollection(myAllValues.get(variable));
	}

	@Override
	public int getValuesCount(VariableDefinition variable) {
		return myAllValues.get(variable).size();
	}

}
