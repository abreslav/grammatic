package org.abreslav.grammatic.grammar.aspects;

import java.util.List;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Sequence;
import org.eclipse.emf.common.util.EList;

public class SequencePartAspectFunctions implements IGrammarAspectFunctions<List<Expression>, Expression> {

	public static final SequencePartAspectFunctions INSTANCE = new SequencePartAspectFunctions();
	
	private SequencePartAspectFunctions() {
	}

	@Override
	public void after(List<Expression> subject, Expression addition) {
		insert(subject, subject.size() - 1, addition, 1);
	}

	@Override
	public void before(List<Expression> subject, Expression addition) {
		insert(subject, 0, addition, 0);
	}

	@Override
	public void instead(List<Expression> subject, Expression replacement) {
		Expression expression = subject.get(0);
		Sequence sequence = (Sequence) expression.eContainer();
		EList<Expression> expressions = sequence.getExpressions();
		if (expressions.size() == subject.size()) {
			ExpressionAspectFunctions.INSTANCE.instead(sequence, replacement);
		} else {
			int index = expressions.indexOf(expression);
			expressions.removeAll(subject);
			insertIntoSequence(expressions, index, replacement);
		}
	}

	@Override
	public void remove(List<Expression> subject) {
		Sequence sequence = getSequence(subject);
		EList<Expression> expressions = sequence.getExpressions();
		if (expressions.size() == subject.size()) {
			ExpressionAspectFunctions.INSTANCE.remove(sequence);
		} else {
			expressions.removeAll(subject);
			if (expressions.size() == 1) {
				ExpressionAspectFunctions.INSTANCE.instead(sequence, expressions.get(0));
			}
		}
	}
	
	private Sequence getSequence(List<Expression> subject) {
		Expression expression = subject.get(0);
		return (Sequence) expression.eContainer();
	}

	/*package*/ static void insertIntoSequence(List<Expression> expressions,
			int index, Expression addition) {
		if (addition instanceof Sequence) {
			List<Expression> addedExpressions = ((Sequence) addition).getExpressions();
			expressions.addAll(index, addedExpressions);
		} else {
			expressions.add(index, addition);
		}
	}

	private void insert(List<Expression> subject, int subjectIndex,
			Expression addition, int targetShift) {
		Expression last = subject.get(subjectIndex);
		List<Expression> expressions = getSequence(subject).getExpressions();
		int index = expressions.indexOf(last);
		insertIntoSequence(expressions, index + targetShift, addition);
	}

}
