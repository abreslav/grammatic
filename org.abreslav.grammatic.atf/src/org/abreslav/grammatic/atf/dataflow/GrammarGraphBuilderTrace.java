package org.abreslav.grammatic.atf.dataflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;

public class GrammarGraphBuilderTrace {

	private final Map<ControlFlowVertex, Symbol> myStartData = new HashMap<ControlFlowVertex, Symbol>();

	private final Map<ControlFlowVertex, List<Expression>> myBeforeExpressions = EMFProxyUtil.customHashMap();
	private final Map<ControlFlowVertex, List<Expression>> myAfterExpressions = EMFProxyUtil.customHashMap();
	private final Map<ControlFlowVertex, Production> myProductionByFirstVertex = EMFProxyUtil.customHashMap();
	private final Map<ControlFlowVertex, Production> myProductionByLastVertex = EMFProxyUtil.customHashMap();
	
	public Symbol getSymbol(ControlFlowVertex vertex) {
		return myStartData.get(vertex);
	}

	public void setStartVertex(StartVertex vertex, Symbol symbol) {
		myStartData.put(vertex, symbol);
	}
	
	public void setEndVertex(EndVertex vertex, Symbol symbol) {
		myStartData.put(vertex, symbol);
	}
	
	public List<Expression> getBeforeExpressions(ControlFlowVertex vertex) {
		return myBeforeExpressions.get(vertex);
	}
	
	public void prependBeforeExpression(ControlFlowVertex vertex, Expression expression) {
		List<Expression> list = myBeforeExpressions.get(vertex);
		if (list == null) {
			list = new LinkedList<Expression>();
			myBeforeExpressions.put(vertex, list);
		}
		list.add(0, expression);
	}
	
	public List<Expression> getAfterExpressions(ControlFlowVertex vertex) {
		return myAfterExpressions.get(vertex);
	}
	
	public void appendAfterExpression(ControlFlowVertex vertex, Expression expression) {
		List<Expression> list = myAfterExpressions.get(vertex);
		if (list == null) {
			list = new ArrayList<Expression>(1);
			myAfterExpressions.put(vertex, list);
		}
		list.add(expression);
	}

	public Production getProductionByFirstVertex(ControlFlowVertex vertex) {
		return myProductionByFirstVertex.get(vertex);
	}
	
	public Production getProductionByLastVertex(ControlFlowVertex vertex) {
		return myProductionByLastVertex.get(vertex);
	}
	
	public void setProductionFirstVertex(Production production, ControlFlowVertex first) {
		myProductionByFirstVertex.put(first, production);
	}

	public void setProductionLastVertex(Production production, ControlFlowVertex last) {
		myProductionByLastVertex.put(last, production);
	}
}
