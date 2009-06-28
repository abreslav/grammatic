package org.abreslav.grammatic.atf.types.unification.impl;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ITypeValue;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;

public class ConstraintGraphPrinter {

	public static void print(IGraph<ITypeValue> graph, PrintStream out) {
		out.println("digraph typeGraph {");
		Map<ITypeValue, String> ids = new HashMap<ITypeValue, String>();
		int i = 0;
		for (ITypeValue typeValue : graph.getAllVertices()) {
			String id = "v" + i;
			ids.put(typeValue, id);
			out.println(id + " [label=\"" + typeValue + "\"];");
			i++;
		}
		for (ITypeValue typeValue : graph.getAllVertices()) {
			Set<ITypeValue> successors = graph.getSuccessors(typeValue);
			for (ITypeValue successor : successors) {
				out.println(ids.get(typeValue) + " -> " + ids.get(successor) + ";");
			}
		}
		
		out.println("}");
	}

}
