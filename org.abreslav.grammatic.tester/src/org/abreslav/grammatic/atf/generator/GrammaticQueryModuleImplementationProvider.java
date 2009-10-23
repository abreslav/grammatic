package org.abreslav.grammatic.atf.generator;

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
import org.abreslav.grammatic.grammar1.IGrammaticQueryModuleImplementationProvider;
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

public class GrammaticQueryModuleImplementationProvider implements IGrammaticQueryModuleImplementationProvider {

	public final void resolveVariableReferences() {
		for (Entry<VariableReference, String> entry : myUnresolvedReferences.entrySet()) {
			VariableDefinition variableDefinition = myVariableDefinitions.get(entry.getValue());
			if (variableDefinition == null) {
				throw new IllegalArgumentException("Undefined variable: " + entry.getValue());
			}
			entry.getKey().setVariable(variableDefinition);
		}
	}

	public void fillInQueryContainer(QueryContainer<?> queryContainer) {
		queryContainer.getVariableDefinitions().addAll(getVariables());
		myVariables.clear();
		myUnresolvedReferences.clear();
		clearVariableMaps();
	}

	
	@Override
	public IRuleQueryContainerFunctions getRuleQueryContainerFunctions() {
		return new IRuleQueryContainerFunctions() {
			
			@Override
			public RuleQuery createRuleQuery() {
				return QueryFactory.eINSTANCE.createRuleQuery();
			}
			
			@Override
			public QueryContainer<RuleQuery> createQueryContainer(RuleQuery query) {
				QueryContainer<RuleQuery> container = QueryFactory.eINSTANCE.createQueryContainer();
				container.setQuery(query);
				fillInQueryContainer(container);
				return container;
			}
		};
	}
	
	@Override
	public ISymbolQueryWithVarFunctions getSymbolQueryWithVarFunctions() {
		return new ISymbolQueryWithVarFunctions() {
			
			@Override
			public void namedLeftSide(RuleQuery query, SymbolQuery namedSymbolQuery) {
				query.setSymbolVariable(addSymbolQueryVariable(namedSymbolQuery.getName(), null));
				query.setSymbol(namedSymbolQuery);
			}
			
			@Override
			public void anonymousLeftSide(RuleQuery query, String varName,
					SymbolQuery anonymousSymbolQuery) {
				if (varName != null) {
					query.setSymbolVariable(
							createVariableDefinition(
									varName, 
								EMFProxyUtil.copy(anonymousSymbolQuery)));
				}
				query.setSymbol(anonymousSymbolQuery);
			}
		};
	}
	
	@Override
	public ISymbolReferenceQueryFunctions getSymbolReferenceQueryFunctions() {
		return new ISymbolReferenceQueryFunctions() {
			
			@Override
			public ExpressionQuery createSymbolReferenceQuery(
					SymbolQuery anonymousSymbolQuery) {
				return GrammaticQueryModuleImplementationProvider.this.createSymbolReferenceQuery(anonymousSymbolQuery);
			}
			
			@Override
			public ExpressionQuery addSymbolQueryVariable(SymbolQuery namedSymbolQuery) {
				return addSymbolReferenceQueryVariable(namedSymbolQuery.getName(), namedSymbolQuery);
			}
		};
	}
	
	@Override
	public IAnonymousSymbolQueryFunctions getAnonymousSymbolQueryFunctions() {
		return new IAnonymousSymbolQueryFunctions() {
			
			@Override
			public SymbolQuery createSymbolQuery() {
				return QueryFactory.eINSTANCE.createSymbolQuery();
			}
		};
	}
	
	@Override
	public INamedSymbolQueryFunctions getNamedSymbolQueryFunctions() {
		return new INamedSymbolQueryFunctions() {
			
			@Override
			public SymbolQuery createNamedQuery(String name) {
				SymbolQuery symbolQuery = QueryFactory.eINSTANCE.createSymbolQuery();
				symbolQuery.setName(name);
				return symbolQuery;
			}
		};
	}
	
	@Override
	public IProductionQueryFunctions getProductionQueryFunctions() {
		return new IProductionQueryFunctions() {
			
			@Override
			public void setWildcardVariable(RuleQuery query, String name) {
				if (name != null) {
					query.setWildcardVariable(createVariableDefinition(name, null));
				}
			}
			
			@Override
			public void setOpen(RuleQuery query) {
				query.setOpen(true);
			}
			
			@Override
			public void addProduction(RuleQuery query, String productionVariable,
					ExpressionQuery alternativeQuery) {
				ProductionQuery productionQuery = QueryFactory.eINSTANCE.createProductionQuery();
				productionQuery.setDefinition(alternativeQuery);
				if (productionVariable != null) {
					productionQuery.setVariable(createVariableDefinition(productionVariable, query));
				}
				query.getDefinitions().add(productionQuery);
			}
		};
	}
	
	@Override
	public IAlternativeQueryFunctions getAlternativeQueryFunctions() {
		return new IAlternativeQueryFunctions() {
			
			@Override
			public ExpressionQuery getResult(
					ListBuilder<ExpressionQuery, AlternativeQuery> builder,
					boolean open, String variablePrefix) {
				// TODO: Suspicious code
				if (open) {
					AlternativeQuery query = builder.createList();
					query.setOpen(true);
					if (variablePrefix != null) {
						if (variablePrefix.length() > 0) {
							query.setWildcardVariable(
									createVariableDefinition(variablePrefix, query));
						}
					}
				}
				// && query.getAttributes().isEmpty()
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<ExpressionQuery, AlternativeQuery> createListBuilder() {
				return new ListBuilder<ExpressionQuery, AlternativeQuery>(new ListBuilder.IListOperations<ExpressionQuery, AlternativeQuery>() {
					
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
			}
			
			@Override
			public void addItem(ListBuilder<ExpressionQuery, AlternativeQuery> builder,
					ExpressionQuery sequenceQuery) {
				builder.item(sequenceQuery);
			}
		};
	}
	
	@Override
	public ISequenceQueryFunctions getSequenceQueryFunctions() {
		return new ISequenceQueryFunctions() {
			
			@Override
			public ExpressionQuery getResult(
					ListBuilder<ExpressionQuery, SequenceQuery> builder) {
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<ExpressionQuery, SequenceQuery> createListBuilder() {
				return new ListBuilder<ExpressionQuery, SequenceQuery>(new IListOperations<ExpressionQuery, SequenceQuery>() {
					
					@Override
					public void addItemToList(SequenceQuery sequenceQuery, ExpressionQuery iterationQuery) {
						sequenceQuery.getDefinitions().add(iterationQuery);				
					}
					
					@Override
					public SequenceQuery createList() {
						return QueryFactory.eINSTANCE.createSequenceQuery();
					}
				});
			}
			
			@Override
			public void addItem(ListBuilder<ExpressionQuery, SequenceQuery> builder,
					ExpressionQuery iterationQuery) {
				builder.item(iterationQuery);
			}
		};
	}
	
	@Override
	public IIterationQueryFunctions getIterationQueryFunctions() {
		return new IIterationQueryFunctions() {
			
			@Override
			public ExpressionQuery createSequeceWildcard(String variablePrefix) {
				SequenceWildcard sequenceWildcard = QueryFactory.eINSTANCE.createSequenceWildcard();
				if (variablePrefix != null) {
					sequenceWildcard.setVariable(createVariableDefinition(variablePrefix, sequenceWildcard));
				}
				return sequenceWildcard;
			}
			
			@Override
			public ExpressionQuery assembleIterationQuery(ExpressionQuery varOrAtom,
					IterationQuery op) {
				if (op == null) {
					return varOrAtom;
				}
				op.setDefinition(varOrAtom);
				return op;
			}
		};
	}

	@Override
	public IOperationFunctions getOperationFunctions() {
		return new IOperationFunctions() {
			
			@Override
			public IterationQuery null1() {
				return null;
			}
			
			@Override
			public IterationQuery createIteration(String token) {
				// TODO: tokens should be codes
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
	public ILexicalOperationFunctions getLexicalOperationFunctions() {
		return new ILexicalOperationFunctions() {
			
			@Override
			public Iteration null1() {
				return null;
			}
			
			@Override
			public Iteration createIteration(String token) {
				// TODO: tokens should be codes
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
	public IVarOrAtomFunctions getVarOrAtomFunctions() {
		return new IVarOrAtomFunctions() {
			
			@Override
			public ExpressionQuery createVariableQuery(String name,
					ExpressionQuery atom) {
				createVariableDefinition(name, atom);
				return getVariableReference(name);
			}
		};
	}
	
	@Override
	public IAtomFunctions getAtomFunctions() {
		return new IAtomFunctions() {
			
			@Override
			public ExpressionQuery createVariableReference(String name) {
				return getVariableReference(name);
			}
			
			@Override
			public ExpressionQuery createExactQuery(Expression lexicalAtom) {
				ExactExpressionQuery ldq = QueryFactory.eINSTANCE.createExactExpressionQuery();
				ldq.setExpression(wrapIntoLexical(lexicalAtom));		
				return ldq;
			}
			
			@Override
			public ExpressionQuery createEmptyQuery() {
				return QueryFactory.eINSTANCE.createEmptyQuery();
			}
			
			@Override
			public ExpressionQuery createAnyLexicalQuery() {
				return QueryFactory.eINSTANCE.createLexicalWildcardQuery();
			}
		};
	}
	
	@Override
	public IQueryMetadataFunctions getQueryMetadataFunctions() {
		return new IQueryMetadataFunctions() {
			
			@Override
			public void addAttribute(MetadataQuery query, AttributeQuery attributeQuery) {
				query.getAttributes().add(attributeQuery);
			}
		};
	}
	
	@Override
	public IAttributeQueryFunctions getAttributeQueryFunctions() {
		return new IAttributeQueryFunctions() {
			
			@Override
			public AttributeQuery createType(String namespaceUri, String name,
					AttributeTypeOptions attributeType) {
				AttributeType type = QueryFactory.eINSTANCE.createAttributeType();
				type.setType(attributeType);
				return createAttributeQuery(namespaceUri, name, type);
			}
			
			@Override
			public AttributeQuery createAbsence(String namespaceUri, String name) {
				AttributePresence presence = QueryFactory.eINSTANCE.createAttributePresence();
				presence.setPresent(false);
				return createAttributeQuery(namespaceUri, name, presence);
			}
		};
	}
	
	@Override
	public IAttributeWithValueQueryFunctions getAttributeWithValueQueryFunctions() {
		return new IAttributeWithValueQueryFunctions() {

			@Override
			public AttributeQuery createAttributeQuery() {
				return QueryFactory.eINSTANCE.createAttributeQuery();
			}

			@Override
			public void setName(AttributeQuery result, String name) {
				result.setAttributeName(name);
			}

			@Override
			public void setNamespace(AttributeQuery result, String namespaceUri) {
				result.setNamespaceUri(getNamespaceUri(namespaceUri));
			}

			@Override
			public void setValue(AttributeQuery result,
					Value attributeValue) {
				ExactValue exactValue = QueryFactory.eINSTANCE.createExactValue();
				exactValue.setValue(attributeValue);
				result.setAttributeValue(exactValue);
			}
			
			@Override
			public void setPresenceIfNeeded(AttributeQuery result) {
				if (result.getAttributeValue() == null) {
					AttributePresence presence = QueryFactory.eINSTANCE.createAttributePresence();
					presence.setPresent(true);
					result.setAttributeValue(presence);					
				}
			}
		};
	}
	
	@Override
	public IAttributeTypeFunctions getAttributeTypeFunctions() {
		return new IAttributeTypeFunctions() {
			
			@Override
			public AttributeTypeOptions getOptions(String name) {
				return AttributeTypeOptions.valueOf(name);
			}
		};
	}
	
	//------------------------
	
	@Override
	public ILexicalAlternativeFunctions getLexicalAlternativeFunctions() {
		return new ILexicalAlternativeFunctions() {
			
			@Override
			public Expression getResult(ListBuilder<Expression, Alternative> builder) {
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<Expression, Alternative> createListBuilder() {
				return createAlternativeListBuilder();
			}
			
			@Override
			public void addItem(ListBuilder<Expression, Alternative> builder,
					Expression lexicalSequence) {
				builder.item(lexicalSequence);
			}
		};
	}
	
	@Override
	public ILexicalSequenceFunctions getLexicalSequenceFunctions() {
		return new ILexicalSequenceFunctions() {
			
			@Override
			public Expression getResult(ListBuilder<Expression, Sequence> builder) {
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<Expression, Sequence> createListBuilder() {
				return createSequenceListBuilder();
			}
			
			@Override
			public void addItem(ListBuilder<Expression, Sequence> builder,
					Expression lexicalIteration) {
				builder.item(lexicalIteration);
			}
		};
	}
	
	@Override
	public ILexicalIterationFunctions getLexicalIterationFunctions() {
		return new ILexicalIterationFunctions() {
			
			@Override
			public Expression assembleIteration(Iteration lexOp, Expression lexicalAtom) {
				if (lexOp == null) {
					return lexicalAtom;
				}
				lexOp.setExpression(lexicalAtom);
				return lexOp;
			}
		};
	}
	
	/////////////////////////////////////////////////////////
	private final Map<String, VariableDefinition> myVariableDefinitions = new LinkedHashMap<String, VariableDefinition>();
	private final Set<VariableDefinition> myVariables = new LinkedHashSet<VariableDefinition>();
	private final Map<String, VariableDefinition> mySymbolVariableDefinitions = new LinkedHashMap<String, VariableDefinition>();
	private final Map<VariableReference, String> myUnresolvedReferences = new HashMap<VariableReference, String>();
	
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

	private VariableDefinition addSymbolQueryVariable(String name,
			SymbolQuery symbolQuery) {
		VariableDefinition variableDefinition = getSymbolVariable(name);
		if (variableDefinition != null) {
			return variableDefinition;
		}
		return createSymbolVariable(name, symbolQuery);
	}

	private VariableDefinition getSymbolVariable(String name) {
		return mySymbolVariableDefinitions.get(name);
	}

	private VariableDefinition createSymbolVariable(String name, Query query) {
		VariableDefinition var = createVariable(name, query);
		mySymbolVariableDefinitions.put(name, var);
		return var;
	}

	private ExpressionQuery createSymbolReferenceQuery(
			SymbolQuery symbolQuery) {
		SymbolReferenceQuery ref = QueryFactory.eINSTANCE.createSymbolReferenceQuery();
		ref.setSymbol(symbolQuery);
		return ref;
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

	private ExpressionQuery getVariableReference(
			VariableDefinition symbolVariable) {
		VariableReference ref = QueryFactory.eINSTANCE.createVariableReference();
		ref.setVariable(symbolVariable);
		return ref;
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
	
	private static LexicalDefinition wrapIntoLexical(Expression e) {
		if (e instanceof LexicalDefinition) {
			return (LexicalDefinition) e;
		}
		LexicalExpression le = GrammarFactory.eINSTANCE.createLexicalExpression();
		le.setExpression(e);
		return le;
	}
	
	private AttributeQuery createAttributeQuery(String namespace,
			String name, AttributeValueQuery value) {
		AttributeQuery attr = QueryFactory.eINSTANCE.createAttributeQuery();
		attr.setAttributeName(name);
		attr.setNamespaceUri(getNamespaceUri(namespace));
		attr.setAttributeValue(value);
		return attr;
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

	public Set<VariableDefinition> getVariables() {
		return myVariables;
	}

	public void clearVariableMaps() {
		mySymbolVariableDefinitions.clear();
		myVariableDefinitions.clear();
	}

	private VariableDefinition createVariable(String name, Query query) {
		VariableDefinition var = QueryFactory.eINSTANCE.createVariableDefinition();
		var.setName(name);
		var.setValue(query);
		myVariables.add(var);
		return var;
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
