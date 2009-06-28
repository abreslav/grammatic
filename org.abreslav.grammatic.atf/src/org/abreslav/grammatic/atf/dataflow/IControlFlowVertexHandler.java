/**
 * 
 */
package org.abreslav.grammatic.atf.dataflow;

import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;

public interface IControlFlowVertexHandler {
	void symbolStartVertex(Symbol symbol, StartVertex vertex);
	void symbolEndVertex(Symbol symbol, EndVertex vertex);
	void productionFirstVertex(Production production, BodyVertex vertex);
	void productionLastVertex(Production production, BodyVertex vertex);
	void expressionFirstVertex(Expression expression, BodyVertex vertex);
	void expressionLastVertex(Expression expression, BodyVertex vertex);
	void fakeVertex(BodyVertex vertex);
}