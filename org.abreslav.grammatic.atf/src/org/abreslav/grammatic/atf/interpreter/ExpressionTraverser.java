package org.abreslav.grammatic.atf.interpreter;

import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class ExpressionTraverser extends GrammarSwitch<INull> {
	
	@Override
	public INull caseCombination(Combination object) {
		for (Expression expression : object.getExpressions()) {
			doSwitch(expression);
		}
		return INull.NULL;
	}
	
	@Override
	public INull caseIteration(Iteration object) {
		doSwitch(object.getExpression());
		return INull.NULL;
	}
	
	@Override
	public INull defaultCase(EObject object) {
		throw new IllegalArgumentException("Unsupported: " + object);
	}
}
