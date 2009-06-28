package org.abreslav.grammatic.metadata.aspectdef.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.AspectdefFactory;
import org.abreslav.grammatic.metadata.aspectdef.Assignment;
import org.abreslav.grammatic.metadata.aspectdef.AssignmentRule;
import org.abreslav.grammatic.metadata.aspectdef.GrammarAssignment;
import org.abreslav.grammatic.query.CommutativeOperationQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.parser.GrammaticQueryBuilders;

public class GrammaticMetadataAspectsBuilders implements IGrammaticMetadataAspectsBuilders {

	private final GrammaticQueryBuilders myGrammaticQueryBuilders;
	
	public GrammaticMetadataAspectsBuilders(
			GrammaticQueryBuilders grammaticQueryBuilders) {
		myGrammaticQueryBuilders = grammaticQueryBuilders;
	}

	@Override
	public IAspectBuilder getAspectBuilder() {
		return new IAspectBuilder() {
			private AspectDefinition myAspectDefinition;

			@Override
			public void init() {
				myAspectDefinition = AspectdefFactory.eINSTANCE.createAspectDefinition();
			}
			
			@Override
			public void release() {
				myAspectDefinition = null;
			}

			@Override
			public void assignmentRule(AssignmentRule assignmentRule) {
				myAspectDefinition.getAssignmentRules().add(assignmentRule);
				myGrammaticQueryBuilders.fillInQueryContainer(assignmentRule.getQueryContainer());
			}

			@Override
			public AspectDefinition getResult() {
				return myAspectDefinition;
			}

			@Override
			public void grammarAssignment(GrammarAssignment grammarAssignment) {
				myAspectDefinition.setGrammarAssignment(grammarAssignment);
			}
			
		};
	}

	@Override
	public IAssignmentBuilder getAssignmentBuilder() {
		return new IAssignmentBuilder() {
			private Assignment myAssignment;
			private final Map<String, VariableDefinition> myVarByName = new HashMap<String, VariableDefinition>();

			@Override
			public void init(QueryContainer<RuleQuery> queryContainer) {
				myVarByName.clear();
				Collection<VariableDefinition> variableDefinitions = new HashSet<VariableDefinition>(myGrammaticQueryBuilders.getDefinedVariables());
				variableDefinitions.addAll(myGrammaticQueryBuilders.getSymbolVariables());
				for (VariableDefinition variableDefinition : variableDefinitions) {
					myVarByName.put(variableDefinition.getName(), variableDefinition);
				}
				myAssignment = AspectdefFactory.eINSTANCE.createAssignment();
			}
			
			@Override
			public void release() {
				myVarByName.clear();
				myAssignment = null;
			}
			
			@Override
			public Assignment createResult() {
				myAssignment.setVariable(myVarByName.get("$"));
				return myAssignment;
			}

			@Override
			public void name(String name) {
				VariableDefinition variable = myVarByName.get(name);
				if (variable == null) {
					throw new IllegalArgumentException("Undefined variable: " + name);
				}
				myAssignment.setVariable(variable);
			}

			@Override
			public Assignment createResult1() {
				return createResult();
			}

			@Override
			public void name1(String name) {
				name(name);
			}
			
		};
	}

	@Override
	public IAssignmentRuleBuilder getAssignmentRuleBuilder() {
		return new IAssignmentRuleBuilder() {
			private AssignmentRule myAssignmentRule;
			private QueryContainer<RuleQuery> myQueryContainer;
			
			@Override
			public void init() {
				myAssignmentRule = AspectdefFactory.eINSTANCE.createAssignmentRule();
				myQueryContainer = null;
			}
			
			@Override
			public void release() {
				myAssignmentRule = null;
				myQueryContainer = null;
			}
			
			@Override
			public AssignmentRule createResult() {
				return myAssignmentRule;
			}

			@Override
			public void qC(QueryContainer<RuleQuery> qc) {
				myQueryContainer = qc;
				myAssignmentRule.setQueryContainer(qc);
			}

			@Override
			public void rQ(RuleQuery rq) {
				myQueryContainer.setQuery(rq);
			}

			@Override
			public void symbolQueryWithVar(SymbolQuery symbolQueryWithVar) {
				myQueryContainer.getQuery().setSymbol(symbolQueryWithVar);
			}

			@Override
			public void symbolMetadataAssignment(
					Assignment symbolMetadataAssignment) {
				VariableDefinition symbolVariable = myQueryContainer.getQuery().getSymbolVariable();
				if (symbolVariable == null) {
					symbolVariable = createSymbolVariable(myQueryContainer);
				}
				symbolMetadataAssignment.setVariable(symbolVariable);
				myAssignmentRule.getAssignments().add(symbolMetadataAssignment);
			}

		};
	}

	@Override
	public IAssignmentsBuilder getAssignmentsBuilder() {
		return new IAssignmentsBuilder() {

			private AssignmentRule myRule;

			@Override
			public void init(AssignmentRule rule,
					QueryContainer<RuleQuery> queryContainer) {
				myRule = rule;
			}
			
			@Override
			public void release() {
				myRule = null;
			}
			
			@Override
			public void assignment(Assignment assignment) {
				if (assignment.getVariable() == null) {
					throw new IllegalArgumentException("Variable not found (probably '$')");
				}
				myRule.getAssignments().add(assignment);
			}
			
		};
	}

	@Override
	public ICreateQueryContainerBuilder getCreateQueryContainerBuilder() {
		return new ICreateQueryContainerBuilder() {
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public QueryContainer<RuleQuery> createQueryContainer() {
				return QueryFactory.eINSTANCE.createQueryContainer();
			}
			
		};
	}

	@Override
	public ICreateRuleQueryBuilder getCreateRuleQueryBuilder() {
		return new ICreateRuleQueryBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public RuleQuery createRuleQuery() {
				return QueryFactory.eINSTANCE.createRuleQuery();
			}
			
		};
	}

	@Override
	public IProductionAssignmentBuilder getProductionAssignmentBuilder() {
		return new IProductionAssignmentBuilder() {
			
			private CommutativeOperationQuery<ProductionQuery> myRuleQuery;

			@Override
			public void init(AssignmentRule rule,
					QueryContainer<RuleQuery> queryContainer,
					RuleQuery theRuleQuery) {
				myRuleQuery = theRuleQuery;
			}
			
			@Override
			public void release() {
				myRuleQuery = null;
			}

			@Override
			public void productionAssignment(ProductionQuery productionQuery) {
				myGrammaticQueryBuilders.clearVariableMaps();
				if (productionQuery == null) {
					myRuleQuery.setOpen(true);
				} else {
					myRuleQuery.getDefinitions().add(productionQuery);
				}
			}
			
		};
	}

	@Override
	public ISymbolMetadataAssignmentBuilder getSymbolMetadataAssignmentBuilder() {
		return new ISymbolMetadataAssignmentBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public Assignment createResult() {
				return AspectdefFactory.eINSTANCE.createAssignment();
			}
			
		};
	}

	private VariableDefinition createSymbolVariable(QueryContainer<RuleQuery> ruleQueryContainer) {
		VariableDefinition variableDefinition = QueryFactory.eINSTANCE.createVariableDefinition();
		variableDefinition.setName("$$");
		ruleQueryContainer.getQuery().setSymbolVariable(variableDefinition);
		ruleQueryContainer.getVariableDefinitions().add(variableDefinition);
		return variableDefinition;
	}

	@Override
	public IGrammarAssignmentBuilder getGrammarAssignmentBuilder() {
		return new IGrammarAssignmentBuilder() {

			@Override
			public GrammarAssignment createResult() {
				return AspectdefFactory.eINSTANCE.createGrammarAssignment();
			}

			@Override
			public void init() {
				// Nothing
			}

			@Override
			public void release() {
				// Nothing
			}
			
		};
	}
}
