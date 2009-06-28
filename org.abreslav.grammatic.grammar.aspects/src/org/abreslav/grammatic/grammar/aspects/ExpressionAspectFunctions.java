package org.abreslav.grammatic.grammar.aspects;

import java.util.List;

import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ExpressionAspectFunctions implements IGrammarAspectFunctions<Expression, Expression> {

	public static final Empty PLACEHOLDER = GrammarFactory.eINSTANCE.createEmpty();
	public static final ExpressionAspectFunctions INSTANCE = new ExpressionAspectFunctions();
	
	private ExpressionAspectFunctions() {
	}
	
	public void after(Expression exp, Expression after) {
		insert(exp, after, 1, exp, after);
	}

	public void before(Expression exp, Expression before) {
		insert(exp, before, 0, before, exp);
	}
	
	public void instead(Expression exp, Expression instead) {
		EObject container = exp.eContainer();
		if (container instanceof Sequence) {
			inteadInsideCombination(exp, instead, container, 
					instead instanceof Sequence);
		} else if (container instanceof Alternative) {
			inteadInsideCombination(exp, instead, container, 
					instead instanceof Alternative);
		} else {
			EStructuralFeature containingFeature = exp.eContainingFeature();
			container.eSet(containingFeature, instead);
		}
	}

	private void inteadInsideCombination(Expression exp, Expression instead,
			EObject container, boolean unwrap) {
		Combination targetCombination = (Combination) container;
		EList<Expression> expressions = targetCombination.getExpressions();
		int index = expressions.indexOf(exp);
		// Not using insetIntoSequence to avoid unnecessary removal
		// in case instead is not a sequence
		if (unwrap) {
			Combination insertedCombination = (Combination) instead;
			expressions.remove(index);
			expressions.addAll(index, insertedCombination.getExpressions());
		} else {
			expressions.set(index, instead);
		}
	}

	public void remove(Expression expr) {
		EObject container = expr.eContainer();
		EStructuralFeature containingFeature = expr.eContainingFeature();
		
		if (containingFeature.isMany()) {
			@SuppressWarnings("unchecked")
			final List<Expression> list = (List<Expression>) container.eGet(containingFeature);
			
			list.remove(expr);
			if (list.isEmpty()) {
				onLastItemDeleted(container, list);
			} else if (list.size() == 1) {
				instead((Expression) container, list.get(0));
			}
		} else {
			onLastItemDeleted(container, null);
		}
	}

	private void onLastItemDeleted(EObject container,
			final List<Expression> list) {
		new GrammarSwitch<INull>() {
			@Override
			public INull caseCombination(Combination object) {
				remove(object);
				return INull.NULL;
			}
			
			@Override
			public INull caseIteration(Iteration object) {
				remove(object);
				return INull.NULL;
			}
			
			@Override
			public INull caseLexicalExpression(LexicalExpression object) {
				remove(object);
				return INull.NULL;
			}
			
			@Override
			public INull caseProduction(Production object) {
				object.setExpression(GrammarFactory.eINSTANCE.createEmpty());
				return INull.NULL;
			}
			
			@Override
			public INull defaultCase(EObject object) {
				throw new IllegalStateException("Expression removed from an unexpected place");
			}
			
		}.doSwitch(container);
	}
	
	private void insert(Expression main, Expression added, int elementShift, Expression first, Expression second) {
		EObject container = main.eContainer();
		if (container instanceof Sequence) {
			// By "induction" we know that exp is not a sequence,
			// since it is directly contained in a sequence
			Sequence targetSequence = (Sequence) container;
			List<Expression> expressions = targetSequence.getExpressions();
			int index = expressions.indexOf(main);
			SequencePartAspectFunctions.insertIntoSequence(
					expressions, 
					// index
					index + elementShift, 
					added);
		} else if (container instanceof Alternative) {
			List<Expression> expressions = ((Alternative) container).getExpressions();
			int index = expressions.indexOf(main);
			// mergeOrWrapIntoSequence may reparent exp:
			expressions.set(index, PLACEHOLDER);
			Expression expressionToAdd = mergeOrWrapIntoSequence(first, second);
			expressions.set(index, expressionToAdd);
		} else {
			EStructuralFeature containingFeature = main.eContainingFeature();
			Expression expressionToAdd = mergeOrWrapIntoSequence(first, second);
			container.eSet(containingFeature, expressionToAdd);
		}
	}

	private Expression mergeOrWrapIntoSequence(Expression exp, Expression after) {
		// NOTE: This is _guaranteed_ to reparent exp or after!
		if (exp instanceof Sequence) {
			Sequence seq = (Sequence) exp;
			List<Expression> expressions = seq.getExpressions();
			SequencePartAspectFunctions.insertIntoSequence(
					expressions, 
					expressions.size(), 
					after);
			return seq;
		} else if (after instanceof Sequence) {
			Sequence seq = (Sequence) after;
			seq.getExpressions().add(0, exp);
			return seq;
		} else {
			return wrapIntoSequence(exp, after);
		}
	}

	private static Sequence wrapIntoSequence(Expression exp, Expression after) {
		Sequence sequence = GrammarFactory.eINSTANCE.createSequence();
		sequence.getExpressions().add(exp);
		sequence.getExpressions().add(after);
		return sequence;
	}
	
}
