package org.abreslav.grammatic.antlr.generator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRProduction;
import org.abreslav.grammatic.antlr.generator.antlr.Rule;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.utils.INull;

public class ANTLRGrammarTraverser {

	public Set<Rule> walk(Collection<Rule> startFrom) {
		Set<Rule> visited = new HashSet<Rule>();
		
		for (Rule rule : startFrom) {
			depthFirstSearch(rule, visited);
		}
		
		return visited;
	}

	private void depthFirstSearch(Rule rule, final Set<Rule> visited) {
		if (visited.contains(rule)) {
			return;
		}
		visited.add(rule);
		
		for (ANTLRProduction production : rule.getProductions()) {
			new ExpressionTraverser<INull, Void>(INull.NULL) {
				@Override
				public INull caseRuleCall(RuleCall object, Void data) {
					depthFirstSearch(object.getRule(), visited);
					return INull.NULL;
				}
			}.doSwitch(production.getExpression(), null);
		}
	}
}
