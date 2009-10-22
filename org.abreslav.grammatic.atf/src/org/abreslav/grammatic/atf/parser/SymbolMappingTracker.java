package org.abreslav.grammatic.atf.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.abreslav.grammatic.utils.IErrorHandler;

public class SymbolMappingTracker {
	
	// a -> a means that a is mapped to somewhere
	// a -> b means that a cannot be mapped anywhere because some b which references a is mapped somewhere
	// a -> null means that a can be mapped (and all the referenced variables must be marked with a
	private final Map<VariableDefinition, VariableDefinition> myForbiddings = new HashMap<VariableDefinition, VariableDefinition>();
	private final Set<VariableDefinition> myDefaultAttributes = new HashSet<VariableDefinition>();
	private final IErrorHandler<RuntimeException> myErrorHandler;
	
	public SymbolMappingTracker(IErrorHandler<RuntimeException> errorHandler) {
		myErrorHandler = errorHandler;
	}

	private void writeForbiddings(VariableDefinition variable) {
		writeForbiddings(variable, variable);
	}
	
	private void writeForbiddings(VariableDefinition currentVar, VariableDefinition cause) {
		VariableDefinition currentCause = myForbiddings.get(currentVar);
		if (currentCause == currentVar) {
			// we have met a variable which is mapped somewhere
			myErrorHandler.reportError("%s (alias: %s) is already mapped to a function call", 
					currentVar.getName(), cause.getName()); 
			return;
		}
		if (currentCause != null) {
			// we have met a variable which is forbidden to map, thus all the following variables are also forbidden,
			// we can safely stop here
			return;
		}
		myForbiddings.put(currentVar, cause);
		Query query = currentVar.getValue();
		if (query instanceof VariableReference) {
			VariableReference ref = (VariableReference) query;
			writeForbiddings(ref.getVariable(), cause);
		}
	}
	
	public void checkAssignToAttributes(VariableDefinition variable) {
		writeForbiddings(variable);
	}
	
	public void checkAddDefaultAttribute(VariableDefinition variable) {
		VariableDefinition forbidding = myForbiddings.get(variable);
		if (forbidding != null) {
			if (forbidding != variable || !myDefaultAttributes.contains(variable)) {
				myErrorHandler.reportError("Cannot add a default attribute to %s (alias: %s), it is already mapped to a function call", 
						forbidding.getName(), variable.getName()); 
			}
		}
	}

	public void clear() {
		myDefaultAttributes.clear();
		myForbiddings.clear();
	}
}
