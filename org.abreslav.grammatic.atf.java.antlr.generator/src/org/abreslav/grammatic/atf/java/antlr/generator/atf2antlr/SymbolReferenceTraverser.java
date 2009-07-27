package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.List;

import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

// Traverses all symbol references
/*package*/ class SymbolReferenceTraverser extends GrammarSwitch<INull> {

	public INull caseSymbol(Symbol object) {
		List<Production> productions = object.getProductions();
		for (Production production : productions) {
			doSwitch(production);
		}
		return INull.NULL;
	}

	public INull caseProduction(Production object) {
		return doSwitch(object.getExpression());
	}

	@Override
	public INull caseCombination(Combination object) {
		for (Expression expr : object.getExpressions()) {
			doSwitch(expr);
		}
		return INull.NULL;
	}

	@Override
	public INull caseIteration(Iteration object) {
		doSwitch(object.getExpression());
		return INull.NULL;
	}

	@Override
	public INull caseLexicalExpression(LexicalExpression object) {
		doSwitch(object.getExpression());
		return INull.NULL;
	}

	@Override
	public INull caseSymbolReference(SymbolReference object) {
		// To be overridden
		return INull.NULL;
	}

	@Override
	public INull caseExpression(Expression object) {
		return INull.NULL;
	}

	public INull defaultCase(EObject object) {
		throw new IllegalStateException();
	}
}