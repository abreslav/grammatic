package org.abreslav.grammatic.graph;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.ControlFlowEdge;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;

public class DotPrinter {

	public static void printDot(ControlFlowGraph graph, PrintStream out, IVertexNameProvider nameProvider) {
		printHeader(out);
		Map<ControlFlowVertex, String> vertexNames = new HashMap<ControlFlowVertex, String>();
		Set<ControlFlowVertex> visited = new HashSet<ControlFlowVertex>();
		List<StartVertex> startVertices = graph.getStartVertices();
		for (StartVertex startVertex : startVertices) {
			printVertex(out, startVertex, vertexNames, visited, nameProvider);
		}
		printFooter(out);
	}

	private static void printVertex(final PrintStream out, ControlFlowVertex vertex,
			final Map<ControlFlowVertex, String> vertexNames, final Set<ControlFlowVertex> visited, final IVertexNameProvider nameProvider) {
		if (visited.contains(vertex)) {
			return;
		}
		visited.add(vertex);
		final String name = nameProvider.getVertexName(vertex);
		Collection<ControlFlowEdge> outgoingEdges = VertexUtil.getOutgoingEdges(vertex);
		for (ControlFlowEdge controlFlowEdge : outgoingEdges) {
			BodyVertex end = controlFlowEdge.getEnd();
			printEdge(out, name, nameProvider.getVertexName(end));
			printVertex(out, end, vertexNames, visited, nameProvider);
		}
	}

	private static void printEdge(PrintStream out, String name, String otherName) {
		out.append("\t").append(name).append(" -> ").append(otherName).append("\n");
	}

	private static void printHeader(PrintStream out) {
		out.println("digraph cfgraph {");		
	}

	private static void printFooter(PrintStream out) {
		out.println("}");
	}
	
}
