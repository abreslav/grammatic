package org.abreslav.grammatic.query.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.interpreter.graphmatching.AlternatingPathGraphMatcher;
import org.abreslav.grammatic.query.interpreter.graphmatching.Edge;
import org.abreslav.grammatic.query.interpreter.graphmatching.IEdge;
import org.abreslav.grammatic.query.interpreter.graphmatching.IGraphMatcher;
import org.abreslav.grammatic.query.interpreter.graphmatching.IMatchingHandler;
import org.eclipse.emf.ecore.EObject;

public class CommutativeOperationMatcher<Q extends Query, E> {
	
	private final IMatcher<Q, E> myNoVarsMatcher;
	private final IMatcher<Q, E> myMatcher;
	private final IWildcardHandler<E> myWildcardHandler;
	
	public CommutativeOperationMatcher(IMatcher<Q, E> noVarsMatcher,
			IMatcher<Q, E> matcher, IWildcardHandler<E> wildcardHandler) {
		super();
		myNoVarsMatcher = noVarsMatcher;
		myMatcher = matcher;
		myWildcardHandler = wildcardHandler;
	}

	public boolean match(final List<E> queries,
			final List<E> expressions) {
		int leftSize = queries.size();
		int rightSize = expressions.size();

		if (leftSize != rightSize) {
			return false;
		}
		
		Collection<IEdge>[] left = arrayOfLists(leftSize);
		Collection<IEdge>[] right = arrayOfLists(rightSize);

		boolean buildGraphResult = buildGraph(left, right, new IIndexMatcher() {

			@Override
			public boolean match(int i, int j) {
				return EMFProxyUtil.equals((EObject) queries.get(i), (EObject) expressions.get(j));
			}
			
		});
		
		if (!buildGraphResult) {
			return false;
		}
		
		AlternatingPathGraphMatcher alternatingPathGraphMatcher = new AlternatingPathGraphMatcher();
		Collection<IEdge> maximumMatching = alternatingPathGraphMatcher.findMaximumMatching(left, right);
		return maximumMatching.size() == leftSize;
	}
	
	public boolean match(final List<Q> queries, final CommutativeOperationQuery<Q> query,
			final List<E> expressions, 
			final IVariableValues variables) {
		
		int leftSize = queries.size();
		int rightSize = expressions.size();
		if (leftSize > rightSize) {
			return false;
		}
		
		if (!query.isOpen() && (rightSize > leftSize)) {
			return false;
		}
		
		Collection<IEdge>[] left = arrayOfLists(leftSize);
		Collection<IEdge>[] right = arrayOfLists(rightSize);

		boolean buildGraphResult = buildGraph(left, right, new IIndexMatcher() {

			@Override
			public boolean match(int i, int j) {
				VariableValues variablesCopy = new VariableValues();
				variablesCopy.putAllVariables(variables);
				return myNoVarsMatcher.match(queries.get(i), expressions.get(j), variablesCopy);
			}
			
		});
		if (!buildGraphResult) {
			return false;
		}
		
		IGraphMatcher alternatingPathGraphMatcher = new AlternatingPathGraphMatcher();
		final boolean[] result = new boolean[1];
		final IVariableValues tempVars = new VariableValues();
		alternatingPathGraphMatcher.enumMaximumMatchings(left, right, left.length, new IMatchingHandler<IEdge>() {
		
			@Override
			public boolean handleMatching(Collection<IEdge> edges) {
				tempVars.clear();
				tempVars.putAllVariables(variables);
				result[0] = true;
				Set<E> unmatched = new LinkedHashSet<E>(expressions);
				for (IEdge edge : edges) {
					E matched = expressions.get(edge.getRight());
					if (!myMatcher.match(queries.get(edge.getLeft()), matched, tempVars)) {
						result[0] = false;
						break;
					}
					unmatched.remove(matched);
				}
				if (result[0]) {
					variables.putAllVariables(tempVars);
					myWildcardHandler.processWildcardVariable(variables, unmatched, query.getWildcardVariable(), query);
				}
				return !result[0];
			}
		});
		return result[0];
	}
	
	private static boolean buildGraph(Collection<IEdge>[] left,	Collection<IEdge>[] right, IIndexMatcher matcher) {
		for (int i = 0; i < left.length; i++) {
			boolean hasMatch = false;
			for (int j = 0; j < right.length; j++) {
				if (matcher.match(i, j)) {
					Edge edge = new Edge(i, j);
					left[i].add(edge);
					right[j].add(edge);
					hasMatch = true;
				}
			}
			if (!hasMatch) {
				return false;
			}
		}		
		return true;
	}

	private static Collection<IEdge>[] arrayOfLists(int size) {
		@SuppressWarnings("unchecked")
		Collection<IEdge>[] left = new Collection[size];
		for (int i = 0; i < left.length; i++) {
			left[i] = new ArrayList<IEdge>();
		}
		return left;
	}
	
	private interface IIndexMatcher  {
		boolean match(int i, int j);
	}
	
}
