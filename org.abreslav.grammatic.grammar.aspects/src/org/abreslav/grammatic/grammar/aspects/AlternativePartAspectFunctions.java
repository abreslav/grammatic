package org.abreslav.grammatic.grammar.aspects;

import java.util.List;

import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Expression;
import org.eclipse.emf.common.util.EList;

public class AlternativePartAspectFunctions implements IGrammarAspectFunctions<List<Expression>, Expression> {

	public static final AlternativePartAspectFunctions INSTANCE = new AlternativePartAspectFunctions();
	
	private AlternativePartAspectFunctions() {
	}

	@Override
	public void after(List<Expression> subject, Expression addition) {
		appendToAlternative(getExpressionList(subject), addition);
	}

	@Override
	public void before(List<Expression> subject, Expression addition) {
		appendToAlternative(getExpressionList(subject), addition);
	}

	@Override
	public void instead(List<Expression> subject, Expression replacement) {
		Alternative alternative = getAlternative(subject);
		EList<Expression> expressions = getExpressionList(alternative);
		if (expressions.size() == subject.size()) {
			ExpressionAspectFunctions.INSTANCE.instead(alternative, replacement);
		} else {
			expressions.removeAll(subject);
			appendToAlternative(expressions, replacement);
		}
	}

	@Override
	public void remove(List<Expression> subject) {
		Alternative alternative = getAlternative(subject);
		EList<Expression> expressions = getExpressionList(alternative);
		if (expressions.size() == subject.size()) {
			ExpressionAspectFunctions.INSTANCE.remove(alternative);
		} else {
			expressions.removeAll(subject);
			if (expressions.size() == 1) {
				ExpressionAspectFunctions.INSTANCE.instead(alternative, expressions.get(0));
			}
		}
	}	
	
	/*package*/ static void appendToAlternative(List<Expression> expressions,
			Expression addition) {
		if (addition instanceof Alternative) {
			expressions.addAll(((Alternative) addition).getExpressions());
		} else {
			expressions.add(addition);
		}
	}

	private EList<Expression> getExpressionList(Alternative alternative) {
		EList<Expression> expressions = alternative.getExpressions();
		return expressions;
	}

	private EList<Expression> getExpressionList(List<Expression> subject) {
		Alternative alternative = getAlternative(subject);
		return getExpressionList(alternative);
	}

	private Alternative getAlternative(List<Expression> subject) {
		Expression expression = subject.get(0);
		Alternative alternative = (Alternative) expression.eContainer();
		return alternative;
	}

}
