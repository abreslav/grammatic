package org.abreslav.grammatic.atf.types.unification.impl;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ErrorType;
import org.abreslav.grammatic.atf.types.unification.IClosedConstraint;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.ITypeValue;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class ConstraintSystemSolver {
	public static <T, E extends Throwable> Map<TypeVariable, T> solve(
			ConstraintSystem<T, E> constraintSystem,
			FiniteTypeSystemClosedConstraintSolver<T> solver,
			ITypeErrorHandler<E> errorHandler) throws E {
		Map<ITypeValue, ITypeValue> variableToRepresentative = new HashMap<ITypeValue, ITypeValue>();
		IGraph<ITypeValue> constraintGraph = constraintSystem.getConstraintGraph(variableToRepresentative);
		
		if (true) {
			try {
				ConstraintGraphPrinter.print(constraintGraph, new PrintStream("testData/atf/interpreter/constraintGraph.dot"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Map<TypeVariable, IClosedConstraint<T>> closedConstraints = ClosedConstraintsBuilder.buildClosedConstraints(constraintGraph, constraintSystem.getExplicitTypes());
		
		Map<TypeVariable, Set<T>> possibleValues = new HashMap<TypeVariable, Set<T>>();
		Set<TypeVariable> variables = closedConstraints.keySet();
		for (TypeVariable typeVariable : variables) {
			IClosedConstraint<T> constraint = closedConstraints.get(typeVariable);
			Set<T> solutions = solver.solve(constraint, errorHandler);
			possibleValues.put(typeVariable, solutions);
		}
		ImplicitConstraintPropagator.propagateImplicitConstraints(possibleValues, constraintGraph);
		Map<TypeVariable, T> result = new HashMap<TypeVariable, T>();
		for (TypeVariable typeVariable : constraintSystem.getVariables()) {
			ITypeValue representative = variableToRepresentative.get(typeVariable);
			Set<T> types = possibleValues.get(representative);
			if (types == null || types.isEmpty()) {
				errorHandler.reportError(ErrorType.NO_TYPE_FOR, typeVariable);
			} else if (types.size() > 1) {
				ArrayList<Object> arrayList = new ArrayList<Object>();
				arrayList.add(typeVariable);
				arrayList.addAll(types);
				errorHandler.reportError(ErrorType.MANY_TYPES_FOR, arrayList.toArray());
			} else {
				result.put(typeVariable, types.iterator().next());
			}
		}
		return result;
	}
	
}
