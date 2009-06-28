package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;

import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.variables.ProductionValue;
import org.abreslav.grammatic.query.variables.RulePartValue;
import org.abreslav.grammatic.query.variables.SymbolValue;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.abreslav.grammatic.query.variables.VariablesFactory;

public class RuleMatcher {

	private final ExpressionMatcher myExpressionMatcher;
	
	/*package*/ RuleMatcher(ExpressionMatcher expressionMatcher) {
		myExpressionMatcher = expressionMatcher;
	}

	public ExpressionMatcher getExpressionMatcher() {
		return myExpressionMatcher;
	}
	
	private final CommutativeOperationMatcher<ProductionQuery, Production> ourMatcher = new CommutativeOperationMatcher<ProductionQuery, Production>(
			new IMatcher<ProductionQuery, Production>() {
				
				@Override
				public boolean match(ProductionQuery query,
						Production element,
						IVariableValues variables) {
					if (!myExpressionMatcher.getAttributeMatcher().matchAttributes(query.getAttributes(), element)) {
						return false;
					}
					
					return myExpressionMatcher.matchIgnoringVariables(query.getDefinition(), element.getExpression(), variables);
				}
				
				
			},
			new IMatcher<ProductionQuery, Production>() {
				
				@Override
				public boolean match(ProductionQuery query,
						Production element,
						IVariableValues variables) {
					if (!myExpressionMatcher.getAttributeMatcher().matchAttributes(query.getAttributes(), element)) {
						return false;
					}
					
					boolean matches = myExpressionMatcher.matchExpression(query.getDefinition(), element.getExpression(), variables);
					VariableDefinition variable = query.getVariable();
					if (matches && variable != null) {
						ProductionValue value = VariablesFactory.eINSTANCE.createProductionValue();
						value.setItem(element);
						variables.setVariableValue(variable, value);
					}
					return matches;
				}
				
				
			},
			new IWildcardHandler<Production>() {
				
				@Override
				public void processWildcardVariable(
						IVariableValues variables,
						Collection<Production> unmatched,
						VariableDefinition variable, Query variableValue) {
					if (variable == null) {
						return;
					}
					
					VariableValue value;
					ProductionValue prodValue = null;
					
					switch (unmatched.size()) {
					case 0:
						prodValue = VariablesFactory.eINSTANCE.createProductionValue();
						value = prodValue;
						prodValue.setItem(null);
						break;
					case 1:
						prodValue = VariablesFactory.eINSTANCE.createProductionValue();
						value = prodValue;
						prodValue.setItem(unmatched.iterator().next());
						break;
					default:
						RulePartValue partValue = VariablesFactory.eINSTANCE.createRulePartValue();
						partValue.getItems().addAll(unmatched);
						value = partValue;
						break;
					}
					variables.setVariableValue(variable, value);
					variable.setValue(null);
				}
				
				
			}
	);
	
	public boolean matchRule(RuleQuery query, Symbol symbol, IVariableValues variables) {
		SymbolQuery symbolQuery = query.getSymbol();
		if (!myExpressionMatcher.getAttributeMatcher().matchAttributes(symbolQuery.getAttributes(), symbol)) {
			return false;
		}
		if (!SymbolMatcher.matchSymbol(symbolQuery, symbol)) {
			return false;
		}
		VariableDefinition variable = query.getSymbolVariable();
		if (variable != null) {
			SymbolValue value = VariablesFactory.eINSTANCE.createSymbolValue();
			value.setItem(symbol);
			variables.setVariableValue(variable, value);
		}
		return ourMatcher.match(query.getDefinitions(), query, symbol.getProductions(), variables);
	}

	public boolean matchProduction(ProductionQuery query, Production production, IVariableValues variables) {
		return myExpressionMatcher.getAttributeMatcher().matchAttributes(query.getAttributes(), production)
			&& myExpressionMatcher.matchExpression(query.getDefinition(), production.getExpression(), variables);
	}
}
