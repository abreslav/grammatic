package org.abreslav.grammatic.atf.dataflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.BranchingVertex;
import org.abreslav.grammatic.cfgraph.CallVertex;
import org.abreslav.grammatic.cfgraph.CfgraphFactory;
import org.abreslav.grammatic.cfgraph.ControlFlowEdge;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.SimpleStatementVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.cfgraph.StatementVertex;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;

public class GrammarControlFlowGraphBuilder {
	
	public static ControlFlowGraph buildControlFlowGraph(
			Collection<Grammar> grammars,
			IControlFlowVertexHandler vertexHandler
	) {
		return new GrammarControlFlowGraphBuilder(vertexHandler).buildControlFlowGraph(grammars);
	}
	
	private final ControlFlowGraph myControlFlowGraph = CfgraphFactory.eINSTANCE.createControlFlowGraph();
	private final IControlFlowVertexHandler myVertexHandler;
	private final Map<Symbol, StartVertex> mySymbolToStart = EMFProxyUtil.customHashMap();
	private final Map<Expression, FirstLastVertex> myFirstLastVertices = EMFProxyUtil.customHashMap();

	private GrammarControlFlowGraphBuilder(IControlFlowVertexHandler vertexHandler) {
		myVertexHandler = vertexHandler;
	}

	private ControlFlowGraph buildControlFlowGraph(Collection<Grammar> grammars) {
		for (Grammar grammar : grammars) {
			for (Symbol symbol : grammar.getSymbols()) {
				buildSymbolSubgraph(symbol);
			}
		}
		return myControlFlowGraph;
	}

	private StartVertex buildSymbolSubgraph(Symbol symbol) {
		StartVertex startVertex = mySymbolToStart.get(symbol);
		if (startVertex != null) {
			return startVertex;
		}
		
		startVertex = CfgraphFactory.eINSTANCE.createStartVertex();
		mySymbolToStart.put(symbol, startVertex);

		myControlFlowGraph.getStartVertices().add(startVertex);
		myVertexHandler.symbolStartVertex(symbol, startVertex);
		
		ControlFlowEdge edge = CfgraphFactory.eINSTANCE.createControlFlowEdge();
		startVertex.setNext(edge);
		
		EndVertex endVertex = CfgraphFactory.eINSTANCE.createEndVertex(); 
		List<Production> productions = symbol.getProductions();
		if (productions.size() == 1) {
			BodyVertex vertex = createProductionVertices(productions.get(0), endVertex);
			edge.setEnd(vertex);
		} else {
			BranchingVertex productionBranch = CfgraphFactory.eINSTANCE.createBranchingVertex();
			edge.setEnd(productionBranch);
			for (Production production : productions) {
				BodyVertex vertex = createProductionVertices(production, endVertex);
				productionBranch.getBranches().add(createEdge(vertex, false));
			}
		}
		
		myVertexHandler.symbolEndVertex(symbol, endVertex);
		return startVertex;
	}

	private BodyVertex createProductionVertices(Production production,
			EndVertex endVertex) {
		Expression expression = production.getExpression();
		FirstLastVertex firstLastVertex = getVertex(expression, endVertex);
		myVertexHandler.productionFirstVertex(production, firstLastVertex.ffFirst);
		myVertexHandler.productionLastVertex(production, firstLastVertex.ffLast);
		return firstLastVertex.ffFirst;
	}

	private FirstLastVertex getVertex(Expression expression, final BodyVertex next) {
		return getVertex(expression, next, false);
	}
	
	private FirstLastVertex getVertex(Expression expression, final BodyVertex next, final boolean backward) {
		FirstLastVertex firstLastVertex = myFirstLastVertices.get(expression);
		if (firstLastVertex != null) {
			return firstLastVertex;
		}
		
		firstLastVertex = new GrammarSwitch<FirstLastVertex>() {
			@Override
			public FirstLastVertex caseAlternative(Alternative object) {
				BranchingVertex vertex = CfgraphFactory.eINSTANCE.createBranchingVertex();
				StatementVertex afterVertex = createAfterVertex(object, next, backward);
				
				for (Expression branch : object.getExpressions()) {
					BodyVertex branchVertex = getVertex(branch, afterVertex).ffFirst;
					vertex.getBranches().add(createEdge(branchVertex, false));
				}
				return new FirstLastVertex(vertex, afterVertex);
			}
			
			@Override
			public FirstLastVertex caseIteration(Iteration object) {
				int lowerBound = object.getLowerBound();
				int upperBound = object.getUpperBound();
				String upperBoundError = "Upper bounds other than 1 and -1 are not supported: ";
				String lowerBoundError = "Lower bounds other than 0 and 1 are not supported: ";
				if (lowerBound == 0) {
					BranchingVertex condition = CfgraphFactory.eINSTANCE.createBranchingVertex();
					FirstLastVertex firstLastVertex;
					BodyVertex loopBody;
					switch (upperBound) {
					case -1:
						loopBody = getVertex(object.getExpression(), condition, true).ffFirst;
						condition.getBranches().add(createEdge(next, backward));
						firstLastVertex = new FirstLastVertex(condition, condition);
						break;
					case 1:
						StatementVertex afterVertex = createAfterVertex(object, next, backward);
						condition.getBranches().add(createEdge(afterVertex, false));
						loopBody = getVertex(object.getExpression(), afterVertex).ffFirst;
						firstLastVertex = new FirstLastVertex(condition, afterVertex);
						break;
					default:
						throw new IllegalArgumentException(upperBoundError + upperBound);
					}
					condition.getBranches().add(createEdge(loopBody, false));
					return firstLastVertex;
				} else if (lowerBound == 1) {
					switch (upperBound) {
					case -1:
						BranchingVertex condition = CfgraphFactory.eINSTANCE.createBranchingVertex();
						BodyVertex body = getVertex(object.getExpression(), condition).ffFirst;
						condition.getBranches().add(createEdge(next, backward));
						condition.getBranches().add(createBackwardEdge(body));
						return new FirstLastVertex(body, condition);
					default:
						throw new IllegalArgumentException(upperBoundError + upperBound);
					}
				} else {
					throw new IllegalArgumentException(lowerBoundError + lowerBound);
				}
			}
			
			@Override
			public FirstLastVertex caseSequence(Sequence object) {
				List<Expression> expressions = new ArrayList<Expression>(object.getExpressions());
				Collections.reverse(expressions);
				BodyVertex result = next;
				BodyVertex last = null;
				for (Expression expression : expressions) {
					FirstLastVertex vertex = getVertex(expression, result);
					result = vertex.ffFirst;
					if (last == null) {
						last = vertex.ffLast;
					}
				}
				return new FirstLastVertex(result, last);
			}
			
			@Override
			public FirstLastVertex caseSymbolReference(SymbolReference object) {
				CallVertex vertex = CfgraphFactory.eINSTANCE.createCallVertex();
				vertex.setCalled(buildSymbolSubgraph(object.getSymbol()));
				vertex.setNext(createEdge(next, backward));
				return new FirstLastVertex(vertex, vertex);
			}

			@Override
			public FirstLastVertex caseLexicalDefinition(LexicalDefinition object) {
				FirstLastVertex statement = createSimpleStatement(object, next);
				return statement;
			}

			@Override
			public FirstLastVertex caseEmpty(Empty object) {
				FirstLastVertex result = createSimpleStatement(object, next);
				myVertexHandler.fakeVertex(result.ffFirst);
				return result;
			}

			private FirstLastVertex createSimpleStatement(Expression object,
					final BodyVertex next) {
				SimpleStatementVertex vertex = CfgraphFactory.eINSTANCE.createSimpleStatementVertex();
				vertex.setNext(createEdge(next, backward));
				return new FirstLastVertex(vertex, vertex);
			}

		}.doSwitch(expression);
		myFirstLastVertices.put(expression, firstLastVertex);
		myVertexHandler.expressionFirstVertex(expression, firstLastVertex.ffFirst);
		myVertexHandler.expressionLastVertex(expression, firstLastVertex.ffLast);
		return firstLastVertex;
	}

	private StatementVertex createAfterVertex(Expression expression,
			final BodyVertex next, boolean backward) {
		final StatementVertex afterVertex = CfgraphFactory.eINSTANCE.createSimpleStatementVertex();
		afterVertex.setNext(createEdge(next, backward));
		myVertexHandler.fakeVertex(afterVertex);
		return afterVertex;
	}
	
	private ControlFlowEdge createBackwardEdge(final BodyVertex next) {
		ControlFlowEdge edge = createEdge(next, true);
		return edge;
	}

	private ControlFlowEdge createEdge(final BodyVertex next, boolean backward) {
		ControlFlowEdge edge = CfgraphFactory.eINSTANCE.createControlFlowEdge();
		edge.setBackward(backward);
		edge.setEnd(next);
		return edge;
	}
	
	private final class FirstLastVertex {
		public final BodyVertex ffFirst;
		public final BodyVertex ffLast;
		
		public FirstLastVertex(BodyVertex first, BodyVertex last) {
			this.ffFirst = first;
			this.ffLast = last;
		}
	}
}

