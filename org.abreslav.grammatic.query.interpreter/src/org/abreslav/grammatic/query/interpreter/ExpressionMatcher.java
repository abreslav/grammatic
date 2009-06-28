package org.abreslav.grammatic.query.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.AlternativeWildcard;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.LexicalWildcardQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.SequenceQuery;
import org.abreslav.grammatic.query.SequenceWildcard;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;
import org.abreslav.grammatic.query.util.QuerySwitch;
import org.abreslav.grammatic.query.variables.AlternativePartValue;
import org.abreslav.grammatic.query.variables.ExpressionValue;
import org.abreslav.grammatic.query.variables.ListValue;
import org.abreslav.grammatic.query.variables.SequencePartValue;
import org.abreslav.grammatic.query.variables.SymbolValue;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.abreslav.grammatic.query.variables.VariablesFactory;
import org.abreslav.grammatic.query.variables.util.VariablesSwitch;
import org.eclipse.emf.ecore.EObject;

public class ExpressionMatcher {
	private final AttributeMatcher myAttributeMatcher;
	private final ExpressionQuerySwitch myMatcher = new ExpressionQuerySwitch();
	private final IMatcher<ExpressionQuery, Expression> myNoVarsMatcher = new ExpressionQuerySwitchWithNoVariables();
	private final CommutativeOperationMatcher<ExpressionQuery, Expression> myCommutativeOperationMatcher = new CommutativeOperationMatcher<ExpressionQuery, Expression>(myNoVarsMatcher, myMatcher, AlternativeWildcardHandler.INSTANCE);
	private final VariableValueMatcher myVariableValueMatcher = new VariableValueMatcher();

	/*package*/ ExpressionMatcher(AttributeMatcher attributeMatcher) {
		myAttributeMatcher = attributeMatcher;
	}
	
	public boolean matchExpression(ExpressionQuery query, Expression expression, IVariableValues variables) {
		return myMatcher.match(query, expression, variables);
	}
	
	public boolean matchIgnoringVariables(ExpressionQuery definition,
			Expression expression, IVariableValues variables) {
		return myNoVarsMatcher.match(definition, expression, variables);
	}

	public AttributeMatcher getAttributeMatcher() {
		return myAttributeMatcher;
	}
	
	private class ExpressionQuerySwitch extends QuerySwitch<Boolean, Expression, IVariableValues>
												implements IMatcher<ExpressionQuery, Expression> {

		@Override
		public boolean match(ExpressionQuery query, Expression expression, IVariableValues variables) {
			if (!myAttributeMatcher.matchAttributes(query.getAttributes(), expression)) {
				return false;
			}
			
			return doSwitch(query, expression, variables);
		}

		@Override
		public Boolean caseSymbolReferenceQuery(SymbolReferenceQuery query, Expression expression, IVariableValues variables) {
			if (false == expression instanceof SymbolReference) {
				return false;
			}

			return SymbolMatcher.matchSymbol(query.getSymbol(), ((SymbolReference) expression).getSymbol());
		}
		
		@Override
		public Boolean caseSequenceQuery(SequenceQuery query, Expression expression, IVariableValues variables) {
			List<Expression> expressions;
			if (false == expression instanceof Sequence) {
				expressions = Collections.singletonList(expression);
			} else {
				Sequence sequence = (Sequence) expression;
				expressions = sequence.getExpressions();
			}
			
			Iterator<ExpressionQuery> subqueries = query.getDefinitions().iterator();
			ListIterator<Expression> subexpressions = expressions.listIterator();
			
			while (subqueries.hasNext()) {
				ExpressionQuery subquery = subqueries.next();
				if (subquery instanceof SequenceWildcard) {
					if (!processSequenceWildcard(variables, subqueries,
							subexpressions, subquery)) {
						return false;
					}
				} else if (isWildcardVariableReference(subquery)) {
					VariableDefinition variable = ((VariableReference) subquery).getVariable();
					VariableValue value = variables.getVariableValue(variable);
					List<Expression> items;
					if (value instanceof SequencePartValue) {
						items = ((SequencePartValue) value).getItems();
					} else {
						items = Collections.singletonList(((ExpressionValue) value).getItem());
					}
					int firstMatched = subexpressions.nextIndex();
					for (Expression item : items) {
						if (!subexpressions.hasNext()) {
							return false;
						}
						if (!EMFProxyUtil.equals(item, subexpressions.next())) {
							return false;
						}
					}
					int lastMatched = subexpressions.nextIndex();
					List<Expression> subList = expressions.subList(firstMatched, lastMatched);
					handleMatchedSequenceWildcardReference(variables, variable, subList);
				} else {
					if (!subexpressions.hasNext()) {
						return false;
					}
					Expression subexpression = subexpressions.next();
					if (!match(subquery, subexpression, variables)) {
						return false;
					}
				}
			}
			
			return !subexpressions.hasNext();
		}

		protected void handleMatchedSequenceWildcardReference(IVariableValues variables,
				VariableDefinition variable, List<Expression> subList) {
			VariableValue valueToAdd = SequenceWildcardHandler.INSTANCE.createValue(subList);
			variables.addVariableValue(variable, valueToAdd);
		}

		private boolean isWildcardVariableReference(ExpressionQuery subquery) {
			return subquery instanceof VariableReference 
				&& ((VariableReference) subquery).getVariable().getValue() instanceof SequenceWildcard;
		}

		private boolean processSequenceWildcard(
				IVariableValues variables,
				Iterator<ExpressionQuery> subqueries,
				Iterator<Expression> subexpressions, ExpressionQuery subquery) {
			SequenceWildcard wildcard = (SequenceWildcard) subquery;
			List<Expression> matched = new ArrayList<Expression>();
			if (subqueries.hasNext()) {
				ExpressionQuery nextSubquery = subqueries.next();
				IVariableValues tempVars = new VariableValues();
				
				boolean nextSubqueryMatched = false;
				while (subexpressions.hasNext()) {
					Expression subexpression = subexpressions.next();

					tempVars.clear();
					tempVars.putAllVariables(variables);
					SequenceWildcardHandler.INSTANCE.processWildcardVariable(tempVars, matched, wildcard.getVariable(), wildcard);
					if (match(nextSubquery, subexpression, tempVars)) {
						variables.putAllVariables(tempVars);
						nextSubqueryMatched = true;
						break;
					}
					
					matched.add(subexpression);
				}
				if (!nextSubqueryMatched) {
					return false;
				}
			} else {
				while (subexpressions.hasNext()) {
					matched.add(subexpressions.next());
				}
				SequenceWildcardHandler.INSTANCE.processWildcardVariable(variables, matched, wildcard.getVariable(), wildcard);
			}
			return true;
		}
		
		@Override
		public Boolean caseIterationQuery(IterationQuery query, Expression expression, IVariableValues variables) {
			if (false == expression instanceof Iteration) {
				return false;
			}
			Iteration iteration = (Iteration) expression;
			
			if (iteration.getLowerBound() != query.getLowerBound() 
					|| iteration.getUpperBound() != query.getUpperBound()) {
				return false;
			}
			
			return match(query.getDefinition(), iteration.getExpression(), variables);
		}
		
		@Override
		public Boolean caseSequenceWildcard(SequenceWildcard object, Expression expression, IVariableValues variables) {
			SequenceWildcardHandler.INSTANCE.processWildcardVariable(
					variables, 
					Collections.singleton(expression), 
					object.getVariable(), 
					object);			
			return true;
		}
		
		@Override
		public Boolean caseEmptyQuery(EmptyQuery object, Expression expression, IVariableValues variables) {
			return expression instanceof Empty;
		}
		
		@Override
		public Boolean caseAlternativeQuery(final AlternativeQuery query, Expression expression, final IVariableValues variables) {
			final List<Expression> expressions;

			if (expression instanceof Alternative) {
				expressions = ((Alternative) expression).getExpressions();
			} else {
				expressions = Collections.singletonList(expression);
			}			
			
			List<ExpressionQuery> definitions = embedParts(query.getDefinitions(), AlternativePartValue.class, new IConverter<Expression, ExpressionQuery>() {

				@Override
				public ExpressionQuery convert(Expression object) {
					ExactExpressionQuery query = QueryFactory.eINSTANCE.createExactExpressionQuery();
					query.setExpression(object);
					return query;
				}
			}, variables);
			return myCommutativeOperationMatcher.match(definitions, query, expressions, variables);
		}

		@Override
		public Boolean caseVariableReference(VariableReference reference, Expression expression, IVariableValues variables) {
			VariableDefinition variable = reference.getVariable();
			VariableValue value = variables.getVariableValue(variable);
			if (value == null) {
				Query query = variable.getValue();
				if (false == query instanceof ExpressionQuery) {
					return false;
				}
				ExpressionQuery expressionQuery = (ExpressionQuery) query;
				return defineVariable(variable, expressionQuery, expression, variables);
			}
			
			Boolean result = myVariableValueMatcher.doSwitch(value, expression);
			if (result) {
				handleMatchedVariableReference(reference, expression, variables);
			}
			return result;
		}

		protected void handleMatchedVariableReference(
				VariableReference reference, Expression expression,
				IVariableValues variables) {
			variables.addVariableValue(reference.getVariable(), createExpressionValue(expression));
		}

		protected Boolean defineVariable(VariableDefinition variable, ExpressionQuery expressionQuery, 
				Expression expression, IVariableValues variables) {

			if (match(expressionQuery, expression, variables)) {
				ExpressionValue value = createExpressionValue(expression);
				variables.setVariableValue(variable, value);
				return true;
			} 
			return false;
		}

		private ExpressionValue createExpressionValue(Expression expression) {
			ExpressionValue value = VariablesFactory.eINSTANCE.createExpressionValue();
			value.setItem(expression);
			return value;
		}

		@Override
		public Boolean caseExactExpressionQuery(ExactExpressionQuery object,
				Expression data,
				IVariableValues additionalData) {
			return EMFProxyUtil.equals(object.getExpression(), data);
		}		
		
		@Override
		public Boolean caseLexicalWildcardQuery(LexicalWildcardQuery object,
				Expression data,
				IVariableValues additionalData) {
			return data instanceof LexicalDefinition;
		}
		
		@Override
		public Boolean defaultCase(EObject object, Expression data,
				IVariableValues additionalData) {
			return false;
		}

	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	UTILITIES
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	private class ExpressionQuerySwitchWithNoVariables extends
			ExpressionQuerySwitch {
		
		@Override
		protected Boolean defineVariable(VariableDefinition variable, ExpressionQuery expressionQuery, 
				Expression expression, IVariableValues variables) {
			return this.match(expressionQuery, expression, variables);
		}
		
		@Override
		protected void handleMatchedVariableReference(VariableReference arg0,
				Expression arg1, IVariableValues arg2) {
		}
		
		@Override
		protected void handleMatchedSequenceWildcardReference(IVariableValues arg0,
				VariableDefinition arg1, List<Expression> arg2) {
		}
	}
	
	private static abstract class AbstractWildcardHandler implements IWildcardHandler<Expression> {
		public void processWildcardVariable(
				IVariableValues variables,
				Collection<Expression> matchedExpressions, VariableDefinition variable, Query variableValue) {
			if (variable != null) {
				VariableValue value = createValue(matchedExpressions);
				variables.setVariableValue(variable, value);
				variable.setValue(getVariableValue(variableValue));
			}
		}

		public VariableValue createValue(
				Collection<Expression> matchedExpressions) {
			VariableValue value;
			ExpressionValue expValue;
			switch (matchedExpressions.size()) {
			case 0:
				expValue = VariablesFactory.eINSTANCE.createExpressionValue();
				value = expValue;
				expValue.setItem(GrammarFactory.eINSTANCE.createEmpty());
				break;
			case 1:
				expValue = VariablesFactory.eINSTANCE.createExpressionValue();
				value = expValue;
				expValue.setItem(matchedExpressions.iterator().next());
				break;
			default:
				value = createExpressionFragment(matchedExpressions);
				break;
			}
			return value;
		}

		protected abstract Query getVariableValue(Query variableValue);
		
		protected abstract VariableValue createExpressionFragment(
				Collection<Expression> matchedExpressions);
	}	
	
	private static class SequenceWildcardHandler extends AbstractWildcardHandler {

		public static final SequenceWildcardHandler INSTANCE = new SequenceWildcardHandler();
		
		private SequenceWildcardHandler() {
		}
		
		@Override
		protected VariableValue createExpressionFragment(
				Collection<Expression> matchedExpressions)  {
			SequencePartValue value = VariablesFactory.eINSTANCE.createSequencePartValue();
			value.getItems().addAll(matchedExpressions);
			return value;
		}

		@Override
		protected Query getVariableValue(Query variableValue) {
			return EMFProxyUtil.copy(variableValue);
		}
	}
	
	private static class AlternativeWildcardHandler extends AbstractWildcardHandler {
		public static final AlternativeWildcardHandler INSTANCE = new AlternativeWildcardHandler();
		
		private AlternativeWildcardHandler() {
		}			

		@Override
		protected VariableValue createExpressionFragment(
				Collection<Expression> matchedExpressions)  {
			AlternativePartValue value = VariablesFactory.eINSTANCE.createAlternativePartValue();
			value.getItems().addAll(matchedExpressions);
			return value;
		}

		@Override
		protected Query getVariableValue(Query variableValue) {
			return QueryFactory.eINSTANCE.createAlternativeWildcard();
		}
	}
	
	private class VariableValueMatcher extends VariablesSwitch<Boolean, Expression> {
		
		@Override
		public Boolean caseExpressionValue(ExpressionValue object,
				Expression data) {
			return EMFProxyUtil.equals(object.getItem(), data);
		}
		
		@Override
		public Boolean caseSymbolValue(SymbolValue object, Expression data) {
			if (false == data instanceof SymbolReference) {
				return false;
			}
			return object.getItem() == ((SymbolReference) data).getSymbol();
		}
		
		@Override
		public Boolean caseAlternativePartValue(AlternativePartValue object,
				Expression data) {
			if (false == data instanceof Alternative) {
				return false;
			} 
			Alternative alternative = (Alternative) data;
			return myCommutativeOperationMatcher.match(object.getItems(), alternative.getExpressions());
		}
		
		@Override
		public Boolean caseSequencePartValue(SequencePartValue object,
				Expression data) {
			List<Expression> expressions;
			if (data instanceof Sequence) {
				Sequence sequence = (Sequence) data;
				expressions = sequence.getExpressions();
			} else {
				expressions = Collections.singletonList(data);
			}
			
			if (expressions.size() != object.getItems().size()) {
				return false;
			}
			Iterator<Expression> iterator = expressions.iterator();
			for (Expression item : object.getItems()) {
				if (!EMFProxyUtil.equals(item, iterator.next())) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public Boolean defaultCase(EObject object, Expression data) {
			return false;
		}
	}
	
	private interface IConverter<F, T> {
		T convert(F object);
	}
	
	private static <Q, E> List<Q> embedParts(List<Q> subqueries,
					Class<? extends ListValue<E>> clazz,
					IConverter<E, Q> factory,
					IVariableValues variables) {
		
		List<Q> result = new ArrayList<Q>();
		
		for (Q subquery : subqueries) {
			if (subquery instanceof VariableReference) {
				// If the variable references another variable, etc.
				VariableReference ref = (VariableReference) subquery;
				while (ref.getVariable().getValue() instanceof VariableReference) {
					ref = (VariableReference) ref.getVariable().getValue();
				}
				// assert: ref.getVariable() does not reference a variable directly
				VariableValue value = variables.getVariableValue(ref.getVariable());
				if (ref.getVariable().getValue() instanceof AlternativeWildcard 
						&& value == null) {
					throw new IllegalStateException("Embedding of uninitialized variable");
				}
				if (clazz.isInstance(value)) {
					@SuppressWarnings("unchecked")
					ListValue<E> lv = (ListValue<E>) value;
					List<E> items = lv.getItems();
					for (E item : items) {
						result.add(factory.convert(item));
					}
					continue;
				}
			}
			result.add(subquery);
		}
		return result;
	}

}
