package org.abreslav.grammatic.query.tester;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.abreslav.grammatic.query.interpreter.graphmatching.AlternatingPathGraphMatcher;
import org.abreslav.grammatic.query.interpreter.graphmatching.Edge;
import org.abreslav.grammatic.query.interpreter.graphmatching.IEdge;
import org.junit.Test;


public class GraphMatcherTest {

	@Test
	public void testSimplePairs() throws Exception {
		int l = 3;
		int r = 3;
		int[][] edges = {
				{0, 0},
				{1, 1},
				{2, 2},
		};
		
		IEdge[] ansEdges = new IEdge[] {
				new Edge(0, 0),
				new Edge(1, 1),
				new Edge(2, 2),
				
		};

		doTest(l, r, edges, ansEdges);
	}

	@Test
	public void testCrossedPairs() throws Exception {
		int l = 3;
		int r = 3;
		int[][] edges = {
				{0, 2},
				{1, 0},
				{2, 1},
		};
		
		IEdge[] ansEdges = new IEdge[] {
				new Edge(0, 2),
				new Edge(1, 0),
				new Edge(2, 1),
				
		};

		doTest(l, r, edges, ansEdges);
	}

	@Test
	public void testNonTrivial() throws Exception {
		int l = 3;
		int r = 4;
		int[][] edges = {
				{0, 0},
				{0, 1},
				{1, 0},
				{1, 3},
				{1, 3},
				{2, 1},
		};
		
		IEdge[] ansEdges = new IEdge[] {
				new Edge(0, 0),
				new Edge(1, 3),
				new Edge(2, 1),
				
		};

		doTest(l, r, edges, ansEdges);
	}

	@Test
	public void testLessThanL() throws Exception {
		int l = 3;
		int r = 3;
		int[][] edges = {
				{0, 0},
				{0, 1},
				{1, 2},
				{2, 2},
		};
		
		IEdge[] ansEdges = new IEdge[] {
		};

		doTest(l, r, edges, ansEdges);
	}

	private void doTest(int l, int r, int[][] edges, IEdge[] ansEdges) {
		Collection<IEdge>[] left = arrayOfLists(l);
		Collection<IEdge>[] right = arrayOfLists(r);
		
		for (int i = 0; i < edges.length; i++) {
			Edge edge = new Edge(edges[i][0], edges[i][1]);
			left[edge.getLeft()].add(edge);
			right[edge.getRight()].add(edge);
		}
		
		Collection<IEdge> result = new AlternatingPathGraphMatcher().findMaximumMatching(left, right);
		Collection<IEdge> answer = Arrays.asList(ansEdges);
		assertTrue(result.toString(), new HashSet<IEdge>(result).equals(new HashSet<IEdge>(answer)));
	}

	private Collection<IEdge>[] arrayOfLists(int l) {
		@SuppressWarnings("unchecked")
		Collection<IEdge>[] left = new Collection[l];
		for (int i = 0; i < left.length; i++) {
			left[i] = new ArrayList<IEdge>();
		}
		return left;
	}
}
