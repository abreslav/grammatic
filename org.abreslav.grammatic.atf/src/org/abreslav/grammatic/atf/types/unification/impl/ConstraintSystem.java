package org.abreslav.grammatic.atf.types.unification.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ErrorType;
import org.abreslav.grammatic.atf.types.unification.ExplicitType;
import org.abreslav.grammatic.atf.types.unification.IConstraintSystem;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.ITypeValue;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.DFSTools;
import org.abreslav.grammatic.atf.types.unification.graph.impl.Graph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ICycleHandler;
import org.abreslav.grammatic.atf.types.unification.graph.impl.ISuccessorPolicy;

public class ConstraintSystem<T, E extends Throwable> implements IConstraintSystem<T, E> {

	private final IGraph<ITypeValue> myGraph = new Graph<ITypeValue>();
	private final Collection<ExplicitType<T>> myExplicitTypes = new ArrayList<ExplicitType<T>>();
	private final Collection<TypeVariable> myVariables = new ArrayList<TypeVariable>();
	private final ITypeErrorHandler<E> myErrorHandler;
	private final ISubtypingRelation<T> mySubtypingRelation;
	
	public ConstraintSystem(ISubtypingRelation<T> subtypingRelation,
			ITypeErrorHandler<E> errorHandler) {
		myErrorHandler = errorHandler;
		mySubtypingRelation = subtypingRelation;
	}

	@Override
	public void addConstraint(TypeVariable less, TypeVariable greater) {
		myVariables.add(less);
		myVariables.add(greater);
		addEdge(less, greater);
	}

	@Override
	public void addConstraint(TypeVariable less, T greater) {
		myVariables.add(less);
		addEdge(less, createExplicit(greater));
	}


	@Override
	public void addConstraint(T less, TypeVariable greater) {
		myVariables.add(greater);
		addEdge(createExplicit(less), greater);
	}


	@Override
	public void addConstraint(T less, T greater) throws E {
		if (!mySubtypingRelation.isSubtypeOf(less, greater)) {
			mySubtypingRelation.isSubtypeOf(less, greater);
			myErrorHandler.reportError(ErrorType.EXPLICIT_TYPES_DO_NOT_MATCH, less, greater);
		}
	}

	private void addEdge(ITypeValue less, ITypeValue greater) {
		myGraph.addVertex(less);
		myGraph.addVertex(greater);
		myGraph.addEdge(less, greater);
	}

	private ExplicitType<T> createExplicit(T type) {
		ExplicitType<T> explicitType = new ExplicitType<T>(type);
		myExplicitTypes.add(explicitType);
		return explicitType;
	}
	
	@Override
	public IGraph<ITypeValue> getConstraintGraph(Map<ITypeValue, ITypeValue> variableToRepresentative) {
		IGraph<ITypeValue> condensation = new Graph<ITypeValue>();
		boolean nontrivialCondensation = DFSTools.buildCondensation(myGraph, condensation, variableToRepresentative, new ICycleHandler<ITypeValue>() {

			@Override
			public void cycleDetected(IGraph<ITypeValue> graph, ITypeValue a,
					ITypeValue b, Set<ITypeValue> started,
					Set<ITypeValue> finished, ISuccessorPolicy successorPolicy) {
				
			}
		});
		if (nontrivialCondensation) {
			return condensation;
		} else {
			// Build identity map
			for (ITypeValue typeValue : myGraph.getAllVertices()) {
				variableToRepresentative.put(typeValue, typeValue);
			}
			return myGraph;
		}
	}
	
	public Collection<TypeVariable> getVariables() {
		return myVariables;
	}
	
	public Collection<ExplicitType<T>> getExplicitTypes() {
		return myExplicitTypes;
	}

}
