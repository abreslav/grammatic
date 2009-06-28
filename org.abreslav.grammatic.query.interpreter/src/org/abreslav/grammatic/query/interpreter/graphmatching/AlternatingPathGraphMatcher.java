package org.abreslav.grammatic.query.interpreter.graphmatching;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AlternatingPathGraphMatcher implements IGraphMatcher {

	@Override
	public <T extends IEdge> Collection<T> findMaximumMatching(Collection<T>[] left, Collection<T>[] right) {
		boolean[] leftVisited = new boolean[left.length]; 
		boolean[] rightVisited = new boolean[right.length]; 
		@SuppressWarnings("unchecked")
		T[] leftMatchingEdges = (T[]) new IEdge[left.length];
		@SuppressWarnings("unchecked")
		T[] rightMatchingEdges = (T[]) new IEdge[right.length];
		for (int i = 0; i < left.length; i++) {
			if (leftMatchingEdges[i] == null) {
				Arrays.fill(leftVisited, false);
				Arrays.fill(rightVisited, false);
				if (false == leftToRight(i, leftVisited, rightVisited, leftMatchingEdges, rightMatchingEdges, left, right)) {
					return Collections.emptyList();
				}
			}
		}
		return Arrays.asList(leftMatchingEdges);
	}
	
	public <T extends IEdge> void enumMaximumMatchings(Collection<T>[] left, Collection<T>[] right, int matchingSize, IMatchingHandler<T> handler) {
		Collection<T> maximumMatching = findMaximumMatching(left, right);
		if (maximumMatching.size() < matchingSize) {
			return;
		}
		if (!handler.handleMatching(maximumMatching)) {
			return;
		}
		for (T edge : maximumMatching) {
			left[edge.getLeft()].remove(edge);
			right[edge.getRight()].remove(edge);
			enumMaximumMatchings(left, right, matchingSize, handler);
			left[edge.getLeft()].add(edge);
			right[edge.getRight()].add(edge);			
		}
	}
	
	private final boolean leftToRight(
			int current, 
			boolean[] leftVisited, 
			boolean[] rightVisited, 
			IEdge[] leftMatchingEdges, 
			IEdge[] rightMatchingEdges, 
			Collection<? extends IEdge>[] left, 
			Collection<? extends IEdge>[] right
	) {
		if (leftVisited[current]) {
			return false;
		}
		leftVisited[current] = true;
				
		Collection<? extends IEdge> outgoingEdges = left[current];
		for (IEdge edge : outgoingEdges) {
			if (leftMatchingEdges[current] != edge) {
				if (rightToLeft(edge.getRight(), leftVisited, rightVisited, leftMatchingEdges, rightMatchingEdges, left, right)) {
					leftMatchingEdges[current] = edge;
					rightMatchingEdges[edge.getRight()] = edge;
					return true;
				}
			}
		}
		return false;
	}

	private final boolean rightToLeft(
			int current, 
			boolean[] leftVisited, 
			boolean[] rightVisited, 
			IEdge[] leftMatchingEdges, 
			IEdge[] rightMatchingEdges, 
			Collection<? extends IEdge>[] left, 
			Collection<? extends IEdge>[] right
	) {
		if (rightVisited[current]) {
			return false;
		}
		rightVisited[current] = true;
				
		IEdge edge = rightMatchingEdges[current];
		if (edge == null) {
			return true;
		}
		
		return leftToRight(edge.getLeft(), leftVisited, rightVisited, leftMatchingEdges, rightMatchingEdges, left, right);
	}
}
