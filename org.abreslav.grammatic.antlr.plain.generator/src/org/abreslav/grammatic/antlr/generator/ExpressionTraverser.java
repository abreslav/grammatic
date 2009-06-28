package org.abreslav.grammatic.antlr.generator;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRExpression;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRIteration;
import org.abreslav.grammatic.antlr.generator.antlr.Combination;
import org.abreslav.grammatic.antlr.generator.antlr.util.AntlrSwitch;

public class ExpressionTraverser<T, D> extends AntlrSwitch<T, D> {
	private final T myDefaultReturn;
	
	public ExpressionTraverser(T defaultReturn) {
		myDefaultReturn = defaultReturn;
	}

	@Override
	public T caseANTLRIteration(ANTLRIteration object, D data) {
		return doSwitch(object.getExpression(), data);
	}
	
	@Override
	public T caseCombination(Combination object, D data) {
		for (ANTLRExpression expression : object.getExpressions()) {
			T result = doSwitch(expression, data);
			if (isReturnNeeded(result)) {
				return result;
			}
		}
		return myDefaultReturn;
	}
	
	protected boolean isReturnNeeded(T result) {
		return false;
	}
	
}
