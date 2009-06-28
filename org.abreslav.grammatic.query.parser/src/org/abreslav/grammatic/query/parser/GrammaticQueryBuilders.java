package org.abreslav.grammatic.query.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.system.ISystemMetadata;
import org.abreslav.grammatic.parser.util.ListBuilder;
import org.abreslav.grammatic.parser.util.ListBuilder.IListOperations;
import org.abreslav.grammatic.query.AlternativeQuery;
import org.abreslav.grammatic.query.AttributePresence;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.AttributeType;
import org.abreslav.grammatic.query.AttributeTypeOptions;
import org.abreslav.grammatic.query.AttributeValueQuery;
import org.abreslav.grammatic.query.ExactExpressionQuery;
import org.abreslav.grammatic.query.ExactValue;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.IterationQuery;
import org.abreslav.grammatic.query.MetadataQuery;
import org.abreslav.grammatic.query.ProductionQuery;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.SequenceQuery;
import org.abreslav.grammatic.query.SequenceWildcard;
import org.abreslav.grammatic.query.SymbolQuery;
import org.abreslav.grammatic.query.SymbolReferenceQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.VariableReference;

public class GrammaticQueryBuilders implements IGrammaticQueryBuilders {

	public void fillInQueryContainer(QueryContainer<?> queryContainer) {
		queryContainer.getVariableDefinitions().addAll(getVariables());
		myVariables.clear();
		myUnresolvedReferences.clear();
		clearVariableMaps();
	}

	@Override
	public IRuleQueryContainerBuilder getRuleQueryContainerBuilder() {
		return new IRuleQueryContainerBuilder() {
			@Override
			public void init() {
				// Nothing
			}

			@Override
			public void release() {
				// Nothing
			}

			@Override
			public QueryContainer<RuleQuery> ruleQueryContainer(RuleQuery ruleQuery) {
				QueryContainer<RuleQuery> queryContainer = QueryFactory.eINSTANCE.createQueryContainer();
				queryContainer.setQuery(ruleQuery);
				fillInQueryContainer(queryContainer);
				return queryContainer;
			}

		};
	}

	@Override
	public IRuleQueryBuilder getRuleQueryBuilder() {
		return new IRuleQueryBuilder() {
			private RuleQuery myRuleQuery;

			@Override
			public void init() {
				myRuleQuery = QueryFactory.eINSTANCE.createRuleQuery();
			}

			@Override
			public void release() {
				myRuleQuery = null;
			}

			@Override
			public RuleQuery createResult() {
				return myRuleQuery;
			}

			@Override
			public void symbolQueryWithVar(SymbolQuery symbolQueryWithVar) {
				myRuleQuery.setSymbol(symbolQueryWithVar);
			}
			
		};
	}

	@Override
	public IRuleOperationBuilder getRuleOperationBuilder() {
		return new IRuleOperationBuilder() {

			@Override
			public void init() {
				// Nothing
			}

			@Override
			public void release() {
				// Nothing
			}

			@Override
			public boolean ruleOperation(String token) {
				return token.charAt(0) == '|';
			}
			
		};
	}

	@Override
	public IDefinitionBuilder getDefinitionBuilder() {
		return new IDefinitionBuilder() {
			private RuleQuery myRuleQuery;

			@Override
			public void init(RuleQuery ruleQuery) {
				myRuleQuery = ruleQuery;
			}

			@Override
			public void release() {
				myRuleQuery = null;
			}

			@Override
			public void productionQuery(ProductionQuery productionQuery) {
				if (productionQuery != null) {
					myRuleQuery.getDefinitions().add(productionQuery);
				}
			};
		
		};
	}

	@Override
	public IProductionsWildcardBuilder getProductionsWildcardBuilder() {
		return new IProductionsWildcardBuilder() {

			private RuleQuery myRuleQuery;

			@Override
			public void init(RuleQuery rQuery) {
				myRuleQuery = rQuery;
			}

			@Override
			public void release() {
				myRuleQuery = null;
			}

			@Override
			public void productionsWildcard(String name) {
				myRuleQuery.setOpen(true);
				if (name != null) {
					myRuleQuery.setWildcardVariable(createVariableDefinition(name, null));
				}
			}

		};
	}

	@Override
	public ISymbolQueryWithVarBuilder getSymbolQueryWithVarBuilder() {
		return new ISymbolQueryWithVarBuilder() {

			private RuleQuery myRuleQuery;

			@Override
			public void init(RuleQuery rQuery) {
				myRuleQuery = rQuery;
			}

			@Override
			public void release() {
				myRuleQuery = null;
			}
			
			@Override
			public SymbolQuery symbolQueryWithVar(String variablePrefix,
					SymbolQuery anonymousSymbolQuery) {
				if (variablePrefix != null) {
					myRuleQuery.setSymbolVariable(
							createVariableDefinition(
									variablePrefix, 
								EMFProxyUtil.copy(anonymousSymbolQuery)));
				}
				return anonymousSymbolQuery;
			}

			@Override
			public SymbolQuery namedSymbolQuery(SymbolQuery namedSymbolQuery) {
				myRuleQuery.setSymbolVariable(addSymbolQueryVariable(namedSymbolQuery.getName(), null));
				return namedSymbolQuery;
			}

		};
	}

	@Override
	public IAnonymousSymbolQueryBuilder getAnonymousSymbolQueryBuilder() {
		return new IAnonymousSymbolQueryBuilder() {
			private SymbolQuery mySymbolQuery;
			
			@Override
			public void init() {
				mySymbolQuery = QueryFactory.eINSTANCE.createSymbolQuery();
			}
			
			@Override
			public void release() {
				mySymbolQuery = null;
			}
			
			@Override
			public SymbolQuery createResult() {
				return mySymbolQuery;
			}

			@Override
			public SymbolQuery hash() {
				return mySymbolQuery;
			}
			
		};
	}
	
	@Override
	public INamedSymbolQueryBuilder getNamedSymbolQueryBuilder() {
		return new INamedSymbolQueryBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public SymbolQuery namedSymbolQuery(String name) {
				SymbolQuery symbolQuery = QueryFactory.eINSTANCE.createSymbolQuery();
				symbolQuery.setName(name);
				return symbolQuery;
			}
			
		};
	}

	@Override
	public ISymbolReferenceQueryBuilder getSymbolReferenceQueryBuilder() {
		return new ISymbolReferenceQueryBuilder() {
			
			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public ExpressionQuery anonymousSymbolQuery(
					SymbolQuery anonymousSymbolQuery) {
				return createSymbolReferenceQuery(anonymousSymbolQuery);
			}

			@Override
			public ExpressionQuery namedSymbolQuery(SymbolQuery namedSymbolQuery) {
				return addSymbolReferenceQueryVariable(
							namedSymbolQuery.getName(), 
							namedSymbolQuery);
			}

		};
	}

	@Override
	public IProductionQueryBuilder getProductionQueryBuilder() {
		return new IProductionQueryBuilder() {
			
			private RuleQuery myRuleQuery;

			@Override
			public void init(RuleQuery ruleQuery) {
				myRuleQuery = ruleQuery;
			}
			
			@Override
			public void release() {
				myRuleQuery = null;
			}

			@Override
			public ProductionQuery productionQuery(String variablePrefix,
					boolean ruleOperation, ExpressionQuery alternativeQuery) {
				myRuleQuery.setOpen(ruleOperation || myRuleQuery.isOpen());
				ProductionQuery query = QueryFactory.eINSTANCE.createProductionQuery();
				query.setDefinition(alternativeQuery);
				if (variablePrefix != null) {
					query.setVariable(createVariableDefinition(variablePrefix, query));
				}
				return query;
			}

			@Override
			public ProductionQuery productionQuery() {
				return null;
			}
			
		};
	}

	@Override
	public IExpressionQueryBuilder getExpressionQueryBuilder() {
		return new IExpressionQueryBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public ExpressionQuery expressionQuery(
					ExpressionQuery alternativeQuery) {
				return alternativeQuery;
			}
			
		};
	}

	@Override
	public IAlternativeQueryBuilder getAlternativeQueryBuilder() {
		return new IAlternativeQueryBuilder() {
			private final ListBuilder<ExpressionQuery, AlternativeQuery> myListBuilder = new ListBuilder<ExpressionQuery, AlternativeQuery>(new ListBuilder.IListOperations<ExpressionQuery, AlternativeQuery>() {
				
				@Override
				public void addItemToList(AlternativeQuery list,
						ExpressionQuery item) {
					list.getDefinitions().add(item);
				}
				
				@Override
				public AlternativeQuery createList() {
					return QueryFactory.eINSTANCE.createAlternativeQuery();
				}
			});
			
			@Override
			public void init() {
				myListBuilder.init();
			}
			
			@Override
			public void release() {
				myListBuilder.init();
			}

			@Override
			public void sequenceQuery(ExpressionQuery sequenceQuery) {
				myListBuilder.item(sequenceQuery);
			}

			@Override
			public void alternativeWildcard(String alternativeWildcard) {
				AlternativeQuery query = myListBuilder.createList();
				query.setOpen(true);
				if (alternativeWildcard.length() > 0) {
					query.setWildcardVariable(
							createVariableDefinition(alternativeWildcard, query));
				}
			}

			@Override
			public ExpressionQuery getResult() {
				AlternativeQuery query = myListBuilder.getList();
				if (query != null
						&& !query.isOpen()
						&& query.getAttributes().isEmpty()
						&& query.getDefinitions().size() == 1
						) {
					return query.getDefinitions().get(0);
				}
				return myListBuilder.getResult();
			}
			
		};
	}

	@Override
	public IAlternativeWildcardBuilder getAlternativeWildcardBuilder() {
		return new IAlternativeWildcardBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public String alternativeWildcard(String name) {
				if (name == null) {
					return ""; 
				}
				return name;
			}
			
		};
	}

	@Override
	public ISequenceQueryBuilder getSequenceQueryBuilder() {
		return new ISequenceQueryBuilder() {
			private final ListBuilder<ExpressionQuery, SequenceQuery> myListBuilder = new ListBuilder<ExpressionQuery, SequenceQuery>(new IListOperations<ExpressionQuery, SequenceQuery>() {
				
				@Override
				public void addItemToList(SequenceQuery sequenceQuery, ExpressionQuery iterationQuery) {
					sequenceQuery.getDefinitions().add(iterationQuery);				
				}
				
				@Override
				public SequenceQuery createList() {
					return QueryFactory.eINSTANCE.createSequenceQuery();
				}
			});
			
			@Override
			public void init() {
				myListBuilder.init();
			}
			
			@Override
			public void release() {
				myListBuilder.init();
			}
			
			@Override
			public ExpressionQuery getResult() {
				return myListBuilder.getResult();
			}

			@Override
			public void iterationQuery(ExpressionQuery iterationQuery) {
				myListBuilder.item(iterationQuery);
			}
			
		};
	}

	@Override
	public IIterationQueryBuilder getIterationQueryBuilder() {
		return new IIterationQueryBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public ExpressionQuery iterationQuery(ExpressionQuery voa, IterationQuery operation) {
				if (operation == null) {
					return voa;
				}
				operation.setDefinition(voa);
				return operation;
			}

			@Override
			public ExpressionQuery variablePrefix(String name) {
				SequenceWildcard sequenceWildcard = QueryFactory.eINSTANCE.createSequenceWildcard();
				if (name != null) {
					sequenceWildcard.setVariable(createVariableDefinition(name, sequenceWildcard));
				}
				return sequenceWildcard;
			}
			
		};
	}

	@Override
	public IOperation1Builder getOperation1Builder() {
		return new IOperation1Builder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public IterationQuery empty() {
				return null;
			}

			@Override
			public IterationQuery token(String token) {
				IterationQuery query = QueryFactory.eINSTANCE.createIterationQuery();
				switch (token.charAt(0)) {
				case '*':
					query.setLowerBound(0);
					query.setUpperBound(-1);
					break;
				case '?':
					query.setLowerBound(0);
					query.setUpperBound(1);
					break;
				case '+':
					query.setLowerBound(1);
					query.setUpperBound(-1);
					break;
				}
				return query;
			}
			
		};
	}

	@Override
	public IVarOrAtomBuilder getVarOrAtomBuilder() {
		return new IVarOrAtomBuilder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public ExpressionQuery atom1(ExpressionQuery atom) {
				return atom;
			}

			@Override
			public ExpressionQuery varOrAtom(String name, ExpressionQuery atom) {
				createVariableDefinition(name, atom);
				return getVariableReference(name);
			}

		};
	}

	@Override
	public IAtom1Builder getAtom1Builder() {
		return new IAtom1Builder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public ExpressionQuery alternativeQuery(
					ExpressionQuery alternativeQuery) {
				return alternativeQuery;
			}

			@Override
			public ExpressionQuery empty() {
				return QueryFactory.eINSTANCE.createEmptyQuery();
			}

			@Override
			public ExpressionQuery lex() {
				return QueryFactory.eINSTANCE.createLexicalWildcardQuery();
			}

			@Override
			public ExpressionQuery lexicalAtom1(Expression lexicalAtom) {
				ExactExpressionQuery ldq = QueryFactory.eINSTANCE.createExactExpressionQuery();
				ldq.setExpression(wrapIntoLexical(lexicalAtom));		
				return ldq;
			}

			@Override
			public ExpressionQuery name(String name) {
				return getVariableReference(name);
			}

			@Override
			public ExpressionQuery symbolReferenceQuery(
					ExpressionQuery symbolReferenceQuery) {
				return symbolReferenceQuery;
			}
			
		};
	}

	@Override
	public ILexicalAlternativeBuilder getLexicalAlternativeBuilder() {
		return new ILexicalAlternativeBuilder() {
			private final ListBuilder<Expression, Alternative> myListBuilder = createAlternativeListBuilder();

			@Override
			public void init() {
				myListBuilder.init();
			}
			
			@Override
			public void release() {
				myListBuilder.init();
			}
			
			@Override
			public Expression getResult() {
				return myListBuilder.getResult();
			}

			@Override
			public void lexicalSequence(Expression lexicalSequence) {
				myListBuilder.item(lexicalSequence);
			}
			
		};
	}

	@Override
	public ILexicalSequenceBuilder getLexicalSequenceBuilder() {
		return new ILexicalSequenceBuilder() {
			private final ListBuilder<Expression, Sequence> myListBuilder = createSequenceListBuilder();

			@Override
			public void init() {
				myListBuilder.init();
			}
			
			@Override
			public void release() {
				myListBuilder.init();
			}
			
			@Override
			public Expression getResult() {
				return myListBuilder.getResult();
			}

			@Override
			public void lexicalIteration(Expression lexicalIteration) {
				myListBuilder.item(lexicalIteration);
			}
			
		};
	}

	@Override
	public ILexicalIterationBuilder getLexicalIterationBuilder() {
		return new ILexicalIterationBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public Expression lexicalIteration(Expression lexicalAtom, Iteration lexicalOperation) {
				if (lexicalOperation == null) {
					return lexicalAtom;
				}
				lexicalOperation.setExpression(lexicalAtom);
				return lexicalOperation;
			}
			
		};
	}

	@Override
	public ILexicalOperationBuilder getLexicalOperationBuilder() {
		return new ILexicalOperationBuilder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public Iteration empty() {
				return null;
			}

			@Override
			public Iteration token(String token) {
				Iteration iteration = GrammarFactory.eINSTANCE.createIteration();
				switch (token.charAt(0)) {
				case '*':
					iteration.setLowerBound(0);
					iteration.setUpperBound(-1);
					break;
				case '?':
					iteration.setLowerBound(0);
					iteration.setUpperBound(1);
					break;
				case '+':
					iteration.setLowerBound(1);
					iteration.setUpperBound(-1);
					break;
				}
				return iteration;
			}
			
		};
	}

	@Override
	public ILexicalAtom1Builder getLexicalAtom1Builder() {
		return new ILexicalAtom1Builder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public Expression lexicalAtomIndependent(Expression lexicalAtom) {
				return lexicalAtom;
			}

			@Override
			public Expression lexicalAlternative(Expression lexicalAlternative) {
				return wrapIntoLexical(lexicalAlternative);
			}

		};
	}

	@Override
	public IQueryMetadataBuilder getQueryMetadataBuilder() {
		return new IQueryMetadataBuilder() {
			
			private MetadataQuery myQuery;

			@Override
			public void init(MetadataQuery query) {
				myQuery = query;
			}
			
			@Override
			public void release() {
				myQuery = null;
			}

			@Override
			public void attributeQuery(AttributeQuery attributeQuery) {
				myQuery.getAttributes().add(attributeQuery);
			}
			
		};
	}

	@Override
	public IAttributeQueryBuilder getAttributeQueryBuilder() {
		return new IAttributeQueryBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public AttributeQuery attributeQuery(String namespace,
					String name) {
				AttributePresence presence = QueryFactory.eINSTANCE.createAttributePresence();
				presence.setPresent(false);
				return createAttributeQuery(namespace, name, presence);
			}

			@Override
			public AttributeQuery attributeQuery(String namespace,
					String name, AttributeTypeOptions attributeType) {
				AttributeType type = QueryFactory.eINSTANCE.createAttributeType();
				type.setType(attributeType);
				return createAttributeQuery(namespace, name, type);
			}

			@Override
			public AttributeQuery attributeWithValueQuery(
					AttributeQuery attributeWithValueQuery) {
				return attributeWithValueQuery;
			}
			
		};
	}

	@Override
	public IAttributeWithValueQueryBuilder getAttributeWithValueQueryBuilder() {
		return new IAttributeWithValueQueryBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public AttributeQuery attributeWithValueQuery(String namespace,
					String name, Value attributeValue) {
				AttributeValueQuery value;
				if (attributeValue == null) {
					AttributePresence presence = QueryFactory.eINSTANCE.createAttributePresence();
					presence.setPresent(true);
					value = presence;
				} else {
					ExactValue exactValue = QueryFactory.eINSTANCE.createExactValue();
					exactValue.setValue(attributeValue);
					value = exactValue;
				}
				return createAttributeQuery(namespace, name, value);
			}
			
		};
	}

	@Override
	public IAttributeTypeBuilder getAttributeTypeBuilder() {
		return new IAttributeTypeBuilder() {

			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}
			
			@Override
			public AttributeTypeOptions attributeType(String name) {
				return AttributeTypeOptions.valueOf(name);
			}
			
		};
	}

	private AttributeQuery createAttributeQuery(String namespace,
			String name, AttributeValueQuery value) {
		AttributeQuery attr = QueryFactory.eINSTANCE.createAttributeQuery();
		attr.setAttributeName(name);
		attr.setNamespaceUri(getNamespaceUri(namespace));
		attr.setAttributeValue(value);
		return attr;
	}

	@Override
	public INamespaceUriBuilder getNamespaceUriBuilder() {
		return new INamespaceUriBuilder() {
			
			@Override
			public void init() {
				// Nothing				
			}
			
			@Override
			public void release() {
				// Nothing				
			}

			@Override
			public String namespaceUri(String name) {
				return name;
			}
			
		};
	}
	
	@Override
	public IVariablePrefixBuilder getVariablePrefixBuilder() {
		return new IVariablePrefixBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public String variablePrefix(String name) {
				return name == null ? "$" : name;
			}
			
		};
	}

	private final Map<String, VariableDefinition> myVariableDefinitions = new LinkedHashMap<String, VariableDefinition>();
	private final Map<String, VariableDefinition> mySymbolVariableDefinitions = new LinkedHashMap<String, VariableDefinition>();
	private final Map<VariableReference, String> myUnresolvedReferences = new HashMap<VariableReference, String>();
	private final Set<VariableDefinition> myVariables = new LinkedHashSet<VariableDefinition>();
	
	private final VariableDefinition createVariableDefinition(String name, Query query) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		VariableDefinition binding = myVariableDefinitions.get(name);
		if (binding != null && !"$".equals(name)) {
			throw new IllegalArgumentException("Variable rebinding: " + name);
		}
  	    binding = createVariable(name, query);
	    myVariableDefinitions.put(name, binding);
	    return binding;
	}

	private final ExpressionQuery getVariableReference(String name) {
		VariableDefinition definition = myVariableDefinitions.get(name);
		VariableReference reference = QueryFactory.eINSTANCE.createVariableReference();
		if (definition == null) {
			myUnresolvedReferences.put(reference, name);
		} else {
			reference.setVariable(definition);
		}
		return reference;
	}
	
	public final void resolveVariableReferences() {
		for (Entry<VariableReference, String> entry : myUnresolvedReferences.entrySet()) {
			VariableDefinition variableDefinition = myVariableDefinitions.get(entry.getValue());
			if (variableDefinition == null) {
				throw new IllegalArgumentException("Undefined variable: " + entry.getValue());
			}
			entry.getKey().setVariable(variableDefinition);
		}
	}
	
	private static LexicalDefinition wrapIntoLexical(Expression e) {
		if (e instanceof LexicalDefinition) {
			return (LexicalDefinition) e;
		}
		LexicalExpression le = GrammarFactory.eINSTANCE.createLexicalExpression();
		le.setExpression(e);
		return le;
	}
	
	public Collection<VariableDefinition> getDefinedVariables() {
		return myVariableDefinitions.values();
	}
	
	private String getNamespaceUri(String s) {
		if (s == null) {
			return null;
		}
		if ("system".equals(s)) {
			return ISystemMetadata.SYSTEM_NAMESPACE_URI;
		}
		return s;
	}

	private VariableDefinition addSymbolQueryVariable(String name,
			SymbolQuery symbolQuery) {
		VariableDefinition variableDefinition = getSymbolVariable(name);
		if (variableDefinition != null) {
			return variableDefinition;
		}
		return createSymbolVariable(name, symbolQuery);
	}

	private VariableDefinition createSymbolVariable(String name, Query query) {
		VariableDefinition var = createVariable(name, query);
		mySymbolVariableDefinitions.put(name, var);
		return var;
	}

	private VariableDefinition createVariable(String name, Query query) {
		VariableDefinition var = QueryFactory.eINSTANCE.createVariableDefinition();
		var.setName(name);
		var.setValue(query);
		myVariables.add(var);
		return var;
	}
	
	private ExpressionQuery addSymbolReferenceQueryVariable(String name,
			SymbolQuery symbolQuery) {
		VariableDefinition variableDefinition = getSymbolVariable(name);
		if (variableDefinition != null) {
			if (variableDefinition.getValue() instanceof SymbolReferenceQuery) {
				return getVariableReference(variableDefinition);
			} else {
				return createSymbolReferenceQuery(symbolQuery);
			}
		}
		return getVariableReference(createSymbolVariable(name, createSymbolReferenceQuery(symbolQuery)));
	}
	
	private VariableDefinition getSymbolVariable(String name) {
		return mySymbolVariableDefinitions.get(name);
	}

	private ExpressionQuery getVariableReference(
			VariableDefinition symbolVariable) {
		VariableReference ref = QueryFactory.eINSTANCE.createVariableReference();
		ref.setVariable(symbolVariable);
		return ref;
	}
	
	public Collection<VariableDefinition> getSymbolVariables() {
		return mySymbolVariableDefinitions.values();
	}

	private ExpressionQuery createSymbolReferenceQuery(
			SymbolQuery symbolQuery) {
		SymbolReferenceQuery ref = QueryFactory.eINSTANCE.createSymbolReferenceQuery();
		ref.setSymbol(symbolQuery);
		return ref;
	}

	public Set<VariableDefinition> getVariables() {
		return myVariables;
	}

	public void clearVariableMaps() {
		mySymbolVariableDefinitions.clear();
		myVariableDefinitions.clear();
	}

	public static final ListBuilder<Expression, Alternative> createAlternativeListBuilder() {
		return new ListBuilder<Expression, Alternative>(new IListOperations<Expression, Alternative>() {
		
			@Override
			public void addItemToList(Alternative list,
					Expression item) {
				list.getExpressions().add(item);
			}
			
			@Override
			public Alternative createList() {
				return GrammarFactory.eINSTANCE.createAlternative();
			}
		});
	}

	public static final ListBuilder<Expression, Sequence> createSequenceListBuilder() {
		return new ListBuilder<Expression, Sequence>(new IListOperations<Expression, Sequence>() {

			@Override
			public void addItemToList(Sequence list,
					Expression item) {
				list.getExpressions().add(item);
			}
	
			@Override
			public Sequence createList() {
				return GrammarFactory.eINSTANCE.createSequence();
			}
		});
	}
	
}
