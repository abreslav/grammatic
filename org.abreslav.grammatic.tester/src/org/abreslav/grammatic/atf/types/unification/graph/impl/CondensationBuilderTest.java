package org.abreslav.grammatic.atf.types.unification.graph.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystem;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class CondensationBuilderTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
				{
					"acyclic",
					new String[][] {
							{"A", "B"},	
							{"A", "C"},	
							{"B", "C"},	
					},
					false,
					null
				},	
				{
					"cyclic_simple",
					new String[][] {
						{"A", "B"},	
						{"B", "A"},	
					},
					true,
					new String[][] {
							{"A"},	
					},
				},	
				{
					"cyclic_rho",
					new String[][] {
							{"A", "B"},	
							{"B", "A"},	
							{"E", "A"},	
					},
					true,
					new String[][] {
							{"E", "A"},	
					},
				},	
				{
					"cyclic_rho_2",
					new String[][] {
							{"A", "B"},	
							{"B", "A"},	
							{"E", "A"},	
							{"B", "E"},	
					},
					true,
					new String[][] {
							{"A"},	
					},
				},	
				{
					"edge_dir",
					new String[][] {
							{"A", "B"},	
							{"B", "A"},	
							{"D", "A"},	
							{"B", "C"},	
					},
					true,
					new String[][] {
							{"A", "C"},	
							{"D", "A"},	
					},
				},	
		});
	}

	private final String[][] myGraph;
	private final String[][] myExpectedGraph;
	private final boolean myHasCycles;
	
	public CondensationBuilderTest(String[][] graph, boolean hasCycles, String[][] expectedGraph) {
		super();
		this.myHasCycles = hasCycles;
		myGraph = graph;
		myExpectedGraph = expectedGraph;
	}

	@Test
	public void test() throws Exception {
		IGraph<String> graph = buildGraph(myGraph);
		IGraph<String> condensation = new Graph<String>();
		Map<String, String> map = new HashMap<String, String>();
		final Flag f = new Flag(false);
		boolean nonTrivial = DFSTools.buildCondensation(graph, condensation, map, new ICycleHandler<String>() {

			@Override
			public void cycleDetected(IGraph<String> graph, String a, String b,
					Set<String> started, Set<String> finished,
					ISuccessorPolicy successorPolicy) {
				f.set(true);
			}
			
		}, FiniteTypeSystem.getRepresentativeChooser(new IStringRepresentationProvider<String>() {

			@Override
			public String getStringRepresentation(String type) {
				return type;
			}
			
		}));
		assertEquals(myHasCycles, f.isTrue());
		assertEquals(myHasCycles, nonTrivial);
		if (myHasCycles) {
			IGraph<String> expectedGraph = buildGraph(myExpectedGraph);
			Set<String> expectedVertices = expectedGraph.getAllVertices();
			assertEquals(expectedVertices, condensation.getAllVertices());
			for (String string : expectedVertices) {
				assertEquals("Successors differ : " + string, expectedGraph.getSuccessors(string), condensation.getSuccessors(string));
			}
		}
	}

	private IGraph<String> buildGraph(String[][] o) {
		IGraph<String> graph = new Graph<String>();
		for (String[] strings : o) {
			graph.addVertex(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				graph.addVertex(strings[i]);
				graph.addEdge(strings[0], strings[i]);
			}
		}
		return graph;
	}
}
