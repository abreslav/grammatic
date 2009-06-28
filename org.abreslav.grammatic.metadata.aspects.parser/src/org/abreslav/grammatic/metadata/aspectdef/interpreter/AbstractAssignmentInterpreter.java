package org.abreslav.grammatic.metadata.aspectdef.interpreter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.Assignment;
import org.abreslav.grammatic.metadata.aspectdef.AssignmentRule;
import org.abreslav.grammatic.metadata.aspectdef.GrammarAssignment;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.interpreter.IMatchHandler;
import org.abreslav.grammatic.query.interpreter.Matchers;
import org.abreslav.grammatic.query.interpreter.VariableValues;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.abreslav.grammatic.utils.IErrorHandler;

public class AbstractAssignmentInterpreter {
	
	public static <E extends Throwable> void runDefinition(
			AspectDefinition definition,
			Grammar grammar,
			IMetadataProvider metadataProvider,
			final AssignmentHandler assignmentHandler,
			IErrorHandler<E> errorHandler
	) throws E {
		GrammarAssignment grammarAssignment = definition.getGrammarAssignment();
		if (grammarAssignment != null) {
			assignmentHandler.caseGrammar(grammar, grammarAssignment.getAttributes());
		}
		Matchers matchers = new Matchers(metadataProvider);
		final Map<QueryContainer<? extends Query>, AssignmentRule> queries = new LinkedHashMap<QueryContainer<? extends Query>, AssignmentRule>();
		List<AssignmentRule> assignmentRules = definition.getAssignmentRules();
		for (AssignmentRule assignmentRule : assignmentRules) {
			queries.put(assignmentRule.getQueryContainer(), assignmentRule);
		}
		final Map<Query, Integer> matchCounters = new HashMap<Query, Integer>();
		matchers.getGrammarTraverser().traverseGrammar(grammar, queries.keySet(), new IMatchHandler() {

			@Override
			public boolean queryMatched(QueryContainer<? extends Query> queryContainer,
					VariableValues variableValues) {
				incCounter(queryContainer);
				AssignmentRule assignmentRule = queries.get(queryContainer);
				List<Assignment> assignments = assignmentRule.getAssignments();
				for (Assignment assignment : assignments) {
					VariableDefinition variable = assignment.getVariable();
					if (variable == null) {
						continue;
					}
					Collection<VariableValue> allVariableValues = variableValues.getAllVariableValues(variable);
					for (VariableValue value : allVariableValues) {
						if (value == null) {
							continue;
						}
						assignmentHandler.doSwitch(value, assignment.getAttributes());
					}
					
				}
				return true;
			}

			private void incCounter(QueryContainer<? extends Query> queryContainer) {
				Query query = queryContainer.getQuery();
				Integer counter = getCounter(matchCounters, query);
				matchCounters.put(query, counter + 1);
			}

		});
		int matchCount = 0;
		for (AssignmentRule assignmentRule : assignmentRules) {
			Integer counter = getCounter(matchCounters, assignmentRule.getQueryContainer().getQuery());
			int upperBound = assignmentRule.getMatchesUpperBound();
			if (!(counter >= assignmentRule.getMatchesLowerBound()
					&& (upperBound == 0 || counter <= upperBound))) {
				RuleQuery query = (RuleQuery) assignmentRule.getQueryContainer().getQuery();
				VariableDefinition symbolVariable = query.getSymbolVariable();
				String name = symbolVariable == null ? "#" : symbolVariable.getName();
				errorHandler.reportError("Query '%s' matched wrong number of times: %d", name, counter);
			} else {
				matchCount++;
			}
		}
	}
	
	private static Integer getCounter(final Map<Query, Integer> matchCounters,
			Query query) {
		Integer counter = matchCounters.get(query);
		if (counter == null) {
			counter = 0;
		};
		return counter;
	}
	
}
