package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;

import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.RuleQuery;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class GrammarTraverser implements IGrammarTraverser {

	private final RuleMatcher myRuleMatcher;
	
	/*package*/ GrammarTraverser(RuleMatcher ruleMatcher) {
		myRuleMatcher = ruleMatcher;
	}

	@Override
	public void traverseGrammar(Grammar grammar,
			Collection<QueryContainer<? extends Query>> queries,
			final IMatchHandler matchHandler) {
		EList<Symbol> symbols = grammar.getSymbols();
		final VariableValues variableValues = new VariableValues();
		for (final QueryContainer<? extends Query> queryContainer : queries) {
			variableValues.clear();
			if (queryContainer.getQuery() instanceof RuleQuery) {
				RuleQuery query = (RuleQuery) queryContainer.getQuery();
				for (Symbol symbol : symbols) {
					if (myRuleMatcher.matchRule(query, symbol, variableValues)) {
						if (!matchHandler.queryMatched(queryContainer, variableValues)) {
							return;
						}
					}
				}
			} else if (queryContainer.getQuery() instanceof ExpressionQuery) {
				final ExpressionQuery query = (ExpressionQuery) queryContainer.getQuery();
				IExpressionHandler expressionHandler = new IExpressionHandler() {
					
					@Override
					public boolean expressionFound(Expression expression) {
						if (myRuleMatcher.getExpressionMatcher().matchExpression(query, expression, variableValues)) {
							if (!matchHandler.queryMatched(queryContainer, variableValues)) {
								return false;
							}
						}
						return true;
					}
					
				};
				MyGrammarSwitch.INSTANCE.setExpressionHandler(expressionHandler);
				for (Symbol symbol : symbols) {
					if (!MyGrammarSwitch.INSTANCE.doSwitch(symbol)) {
						return;
					}
				}
			} else {
				throw new IllegalArgumentException("This type of queries is not supported: " + queryContainer.getQuery());
			}
		}
	}

	private interface IExpressionHandler {
		boolean expressionFound(Expression expression);
	}
	
	private static class MyGrammarSwitch extends GrammarSwitch<Boolean> {
		public static final MyGrammarSwitch INSTANCE = new MyGrammarSwitch();
		
		private IExpressionHandler myExpressionHandler;
		
		public void setExpressionHandler(IExpressionHandler expressionHandler) {
			myExpressionHandler = expressionHandler;
		}

		@Override
		public Boolean caseSymbol(Symbol object) {
			return traverseExpressions(object.getProductions());
		}
		
		@Override
		public Boolean caseAlternative(Alternative object) {
			myExpressionHandler.expressionFound(object);
			return traverseExpressions(object.getExpressions());
		}
		
		@Override
		public Boolean caseIteration(Iteration object) {
			myExpressionHandler.expressionFound(object);
			return doSwitch(object.getExpression());
		}
		
		@Override
		public Boolean caseLexicalExpression(LexicalExpression object) {
			myExpressionHandler.expressionFound(object);
			return doSwitch(object.getExpression());
		}
		
		@Override
		public Boolean caseSequence(Sequence object) {
			myExpressionHandler.expressionFound(object);
			return traverseExpressions(object.getExpressions());
		}

		@Override
		public Boolean caseExpression(Expression object) {
			return myExpressionHandler.expressionFound(object);
		}

		@Override
		public Boolean defaultCase(EObject object) {
			throw new IllegalStateException();
		}
		
		private Boolean traverseExpressions(EList<? extends EObject> expressions) {
			for (EObject expression : expressions) {
				if (!doSwitch(expression)) {
					return false;
				}
			}
			return true;
		}
	}
}
