package org.abreslav.grammatic.atf.types.unification.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.ErrorType;
import org.abreslav.grammatic.atf.types.unification.IClosedConstraint;
import org.abreslav.grammatic.atf.types.unification.IClosedConstraintSolver;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.graph.impl.StandardPolicies;

public class FiniteTypeSystemClosedConstraintSolver<T> implements IClosedConstraintSolver<T> {

	private final ITypeSystem<T> myTypeSystem;

	public FiniteTypeSystemClosedConstraintSolver(ITypeSystem<T> typeSystem) {
		myTypeSystem = typeSystem;
	}

	@Override
	public <E extends Throwable> Set<T> solve(
			IClosedConstraint<T> closedConstraint, ITypeErrorHandler<E> errorHandler)
			throws E {
		Set<T> lowerBounds = closedConstraint.getLowerBounds();
		Set<T> upperBounds = closedConstraint.getUpperBounds();
		
		int maxIndex = myTypeSystem.size() - 1;
		for (T upperBound : upperBounds) {
			maxIndex = Math.min(maxIndex, myTypeSystem.getTopologicalIndex(upperBound));
		}
		
		if (lowerBounds.isEmpty()) {
			return greatestCommonSubtypes(upperBounds, maxIndex);
		}
		
		Set<T> commonSupertypes = myTypeSystem.getCommonSupertypes(lowerBounds);
		
		if (commonSupertypes.isEmpty()) {
			return Collections.emptySet();
		}
		
		int minIndex = 0;
		for (T lowerBound : lowerBounds) {
			minIndex = Math.max(minIndex, myTypeSystem.getTopologicalIndex(lowerBound));
		}
		
		if (minIndex > maxIndex) {
			return Collections.emptySet();
		}
		
		T greatestLowerBound = myTypeSystem.getTypeByTopologicalIndex(minIndex);
		if (commonSupertypes.contains(greatestLowerBound)) {
			return Collections.singleton(greatestLowerBound);
		}
		
		Set<T> solutions = findNearestNeighboursFrom(
				commonSupertypes, 
				minIndex + 1, 
				maxIndex,
				1);
		
		return solutions;
	}

	private Set<T> findNearestNeighboursFrom(Set<T> commonSupertypes, int first,
			int last, int inc) {
		StandardPolicies direction = inc > 0 ? StandardPolicies.SUCCESSORS : StandardPolicies.PREDECESSORS;
		Set<T> solutions = new HashSet<T>();
		Set<T> forbidden = new HashSet<T>();
		for (int i = first; i <= last; i += inc) {
			T candidate = myTypeSystem.getTypeByTopologicalIndex(i);
			if (forbidden.contains(candidate)) {
				continue;
			}
			if (commonSupertypes.contains(candidate)) {
				solutions.add(candidate);
				forbidden.addAll(myTypeSystem.getNeighbours(candidate, direction));
			}
		}
		return solutions;
	}
	
	private Set<T> greatestCommonSubtypes(Set<T> upperBounds, int maxIndex) {
		Set<T> commonSubtypes = myTypeSystem.getCommonSubtypes(upperBounds);
		T leastUpperBound = myTypeSystem.getTypeByTopologicalIndex(maxIndex);
		if (commonSubtypes.contains(leastUpperBound)) {
			return Collections.singleton(leastUpperBound);
		}
		
		return findNearestNeighboursFrom(commonSubtypes, maxIndex, 0, -1);
	}

	@Deprecated
	public <E extends Throwable> Set<T> solve_old(
			IClosedConstraint<T> closedConstraint, ITypeErrorHandler<E> errorHandler)
			throws E {
		Set<T> lowerBounds = closedConstraint.getLowerBounds();
		Set<T> solutions;
		if (lowerBounds.isEmpty()) {
			solutions = new HashSet<T>(myTypeSystem.getAllTypes());
		} else {
			solutions = null;
			for (T lowerBound : lowerBounds) {
				Set<T> reachable = myTypeSystem.getSupertypes(lowerBound);
				if (solutions == null) {
					solutions = new HashSet<T>(reachable);
				} else {
					solutions.retainAll(reachable);
				}			
				if (solutions.isEmpty()) {
					errorHandler.reportError(ErrorType.NO_COMMON_SUPERTYPE, lowerBounds.toArray());
					return solutions;
				}
			}
		}
		Set<T> upperBounds = closedConstraint.getUpperBounds();
		for (Iterator<T> iterator = solutions.iterator(); iterator.hasNext();) {
			T candidate = iterator.next();
			Set<T> reachable = myTypeSystem.getSupertypes(candidate);
			if (!reachable.containsAll(upperBounds)) {
				iterator.remove();
			}
		}
		return solutions;
	}
	
}
