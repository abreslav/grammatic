package org.abreslav.grammatic.atf.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.GrammarPackage;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.grammarTemplate.AlternativeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.grammar.template.grammarTemplate.IterationTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.ProductionTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.SequenceTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.SymbolTemplate;
import org.abreslav.grammatic.grammar.template.parser.CombinationUtils;
import org.abreslav.grammatic.grammar.template.parser.IKey;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar.template.parser.LazySymbolReference;
import org.abreslav.grammatic.grammar.template.parser.TemplateKey;
import org.abreslav.grammatic.grammar1.IGrammaticGrammarTemplateModuleImplementationProvider;
import org.abreslav.grammatic.parser.util.ListBuilder;
import org.abreslav.grammatic.template.ObjectContainer;
import org.abreslav.grammatic.template.ParameterReference;
import org.abreslav.grammatic.template.ParameterUsagePolicy;
import org.abreslav.grammatic.template.Template;
import org.abreslav.grammatic.template.TemplateApplication;
import org.abreslav.grammatic.template.TemplateArgument;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.TemplateFactory;
import org.abreslav.grammatic.template.TemplateParameter;
import org.abreslav.grammatic.template.instantiator.TemplateInstantiatorInterpreter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class GrammaticGrammarTemplateModuleImplementationProvider
		implements IGrammaticGrammarTemplateModuleImplementationProvider {

	private final Map<String, TemplateParameter<?>> myTemplateParameters = new HashMap<String, TemplateParameter<?>>();
	private final ImportsModuleImplementationProvider myImportsModuleImplementationProvider;
	private final String myModuleName;
	private final IParsingContext myParsingContext;
	private final TemplateInstantiatorInterpreter myInstantiator;
	private String myCurrentTemplateName;

	public GrammaticGrammarTemplateModuleImplementationProvider(
			String moduleName, IParsingContext parsingContext,
			String currentTemplateName) {
		myModuleName = moduleName;
		myParsingContext = parsingContext;
		myCurrentTemplateName = currentTemplateName;
		// TODO:
		throw new UnsupportedOperationException("Initialize import provider and instantiator properly!");
	}

	@Override
	public ITypeFunctions getTypeFunctions() {
		return new ITypeFunctions() {
			
			@Override
			public EClass getEClass(String name) {
				if ("Production".equals(name)) {
					return GrammarPackage.eINSTANCE.getProduction();
				} else if ("Grammar".equals(name)) {
					return GrammarPackage.eINSTANCE.getGrammar();
				} else {
					reportError("Unknown template return type: %s", name);
					return null;
				}
			}
		};
	}

	@Override
	public ITemplateParametersFunctions getTemplateParametersFunctions() {
		return new ITemplateParametersFunctions() {
			
			@Override
			public void addParameter(Template<?> template,
					TemplateParameter<?> templateParameter) {
				String name = templateParameter.getName();
				if (myTemplateParameters.containsKey(name)) {
					reportError("Template parameter redeclared: %s", name);
				}
				myTemplateParameters.put(name, templateParameter);
				template.getParameters().add(templateParameter);				
			}
		};
	}

	@Override
	public ITemplateParameterReferenceFunctions getTemplateParameterReferenceFunctions() {
		return new ITemplateParameterReferenceFunctions() {
			
			@Override
			public ParameterReference<?> createParameterReference(String name) {
				@SuppressWarnings("unchecked")
				TemplateParameter<Object> templateParameter = (TemplateParameter<Object>) myTemplateParameters.get(name);
				if (templateParameter == null) {
					reportError("Template parameter not found: %s", name);
				}
				ParameterReference<Object> ref = TemplateFactory.eINSTANCE.createParameterReference();
				ref.setParameter(templateParameter);
				return ref; 
			}
		};
	}

	@Override
	public ITemplateParameterFunctions getTemplateParameterFunctions() {
		return new ITemplateParameterFunctions() {
			
			@Override
			public TemplateParameter<?> createTemplateParameter(boolean refer,
					boolean copy, EClass type, IterationTemplate operation, String name) {
				if (!EcoreUtil.equals(type, GrammarPackage.eINSTANCE.getProduction())) {
					reportError("Forbidden type for template parameter: %s", type.getName());
				}
				TemplateParameter<Object> parameter = TemplateFactory.eINSTANCE.createTemplateParameter();
				
				int lowerBound = operation == null ? 1 : operation.getLowerBound();
				int upperBound = operation == null ? 1 : operation.getUpperBound();
				parameter.setLowerBound(lowerBound);
				parameter.setLowerBound(upperBound);
				parameter.setType(type);
				parameter.setName(name);
				if (refer) {
					parameter.setUsagePolicy(ParameterUsagePolicy.REFER_ONLY);
				} else if (copy) {
					parameter.setUsagePolicy(ParameterUsagePolicy.COPY);
				} else {
					parameter.setUsagePolicy(ParameterUsagePolicy.CONTAIN_ONCE);
				}
				return parameter;
			}
		};
	}

	@Override
	public ITemplateDeclarationFunctions getTemplateDeclarationFunctions() {
		return new ITemplateDeclarationFunctions() {
			
			@Override
			public Template<Grammar> createTemplate(EClass type, String name) {
				if (!EcoreUtil.equals(type, GrammarPackage.eINSTANCE.getGrammar())) {
					reportError("Forbidden type for template parameter: %s", type.getName());
				}
				Template<Grammar> result = TemplateFactory.eINSTANCE.createTemplate();
				result.setName(name);
				myParsingContext.registerTemplate(myModuleName, result);
				myCurrentTemplateName = name;				
				return result;
			}
			
			@Override
			public void addBody(Template<Grammar> result,
					GrammarTemplate templateBody) {
				result.setBody(templateBody);
			}
			
			@Override
			public void clear() {
				myTemplateParameters.clear();
				myCurrentTemplateName = null;
			}
		};
	}

	@Override
	public ITemplateBodyFunctions getTemplateBodyFunctions() {
		return new ITemplateBodyFunctions() {
			
			@Override
			public GrammarTemplate createGrammarTemplate() {
				return GrammarTemplateFactory.eINSTANCE.createGrammarTemplate();
			}
			
			@Override
			public void addRule(GrammarTemplate result, SymbolTemplate rule) {
				result.getSymbols().add(rule);
			}
		};
	}

	@Override
	public ITemplateArgumentsFunctions getTemplateArgumentsFunctions() {
		return new ITemplateArgumentsFunctions() {
			
			@Override
			public void addArgument(TemplateApplication<?> application,
					TemplateArgument<Production> templateArgument) {
				// TODO: This code is risky: with imports it's OK, but if the template is defined in the same file,
				// it might not be loaded and this method will fail (unresolved proxy object)
				int count = application.getArguments().size();
				Template<?> template = application.getTemplate();
				List<TemplateParameter<?>> parameters = template.getParameters();
				if (parameters.size() <= count) {
					reportError("%s: too many arguments", template.getName());
				}
				@SuppressWarnings("unchecked")
				TemplateParameter<Production> templateParameter = (TemplateParameter<Production>) parameters.get(count);
				EClass expectedType = templateParameter.getType();
				if (!EcoreUtil.equals(expectedType, templateParameter.getType())) {
					reportError("%s: argument %d has incorrect type: %s (expected %s)", template.getName(), count, "Production", expectedType.getName());
				}
				templateArgument.setParameter(templateParameter);
				application.getArguments().add(templateArgument);
			}
		};
	}

	@Override
	public ITemplateArgumentFunctions getTemplateArgumentFunctions() {
		return new ITemplateArgumentFunctions() {
			
			@Override
			public TemplateArgument<Production> createTemplateArgument() {
				return TemplateFactory.eINSTANCE.createTemplateArgument();
			}
			
			@Override
			public void addProduction(TemplateArgument<Production> result,
					Production production) {
				ObjectContainer<Production> container = TemplateFactory.eINSTANCE.createObjectContainer();
				container.setObject(production);
				result.getValues().add(container);
			}
		};
	}

	@Override
	public ITemplateApplicationFunctions getTemplateApplicationFunctions() {
		return new ITemplateApplicationFunctions() {
			
			@Override
			@SuppressWarnings("unchecked")
			public TemplateApplication<Grammar> createTemplateApplication(String name) {
				TemplateApplication<Grammar> application = TemplateFactory.eINSTANCE.createTemplateApplication();
				application.setTemplate((Template<Grammar>) getTemplate(name));				
				return application;
			}
		};
	}

	@Override
	public ISequenceFunctions getSequenceFunctions() {
		return new ISequenceFunctions() {
			
			@Override
			public TemplateBody<? extends Expression> getResult(
					ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate> builder) {
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate> createListBuilder() {
				return CombinationUtils.createSequenceListBuilder();
			}
			
			@Override
			public void addItem(
					ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate> builder,
					TemplateBody<? extends Expression> iteration) {
				builder.item(iteration);
			}
		};
	}

	@Override
	public IRuleFunctions getRuleFunctions() {
		return new IRuleFunctions() {
			
			@Override
			public SymbolTemplate createSymbolTemplate(String name) {
				SymbolTemplate result = GrammarTemplateFactory.eINSTANCE.createSymbolTemplate();
				result.setName(name);
				return result;
			}
			
			@Override
			public void addProduction(SymbolTemplate result,
					TemplateBody<Production> productionTemplate) {
				result.getProductions().add(productionTemplate);
			}
		};
	}

	@Override
	public IProductionTemplateFunctions getProductionTemplateFunctions() {
		return new IProductionTemplateFunctions() {
			
			@Override
			public TemplateBody<Production> createProductionTemplate(
					TemplateBody<? extends Expression> alternative) {
				ProductionTemplate result = GrammarTemplateFactory.eINSTANCE.createProductionTemplate();
				result.setExpression(alternative);
				return result;
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public TemplateBody<Production> convertToProductionTemplate(
					ParameterReference<?> templateParameterReference) {
				TemplateParameter<?> parameter = templateParameterReference.getParameter();
				EClass type = parameter.getType();
				if (!EcoreUtil.equals(GrammarPackage.eINSTANCE.getProduction(), type)) {
					reportError("Parameter reference <%s> has a wrong type: %s (expected Production*)", parameter.getName(), type.getName());
				}
				return (TemplateBody<Production>) templateParameterReference;
			}
		};
	}

	@Override
	public IProductionFunctions getProductionFunctions() {
		return new IProductionFunctions() {
			
			@Override
			public Production createProduction(
					TemplateBody<? extends Expression> alternative) {
				Production result = GrammarFactory.eINSTANCE.createProduction();
				result.setExpression(unwrap(myInstantiator.instantiateTemplateBody(alternative)));
				return result;
			}
		};
	}

	@Override
	public IOperationFunctions getOperationFunctions() {
		return new IOperationFunctions() {
			
			@Override
			public IterationTemplate null1() {
				return null;
			}
			
			@Override
			public IterationTemplate createIterationTemplate(String token) {
				IterationTemplate it = GrammarTemplateFactory.eINSTANCE.createIterationTemplate();
				switch (token.charAt(0)) {
				case '?':
					it.setLowerBound(0);
					it.setUpperBound(1);
					break;
				case '*':
					it.setLowerBound(0);
					it.setUpperBound(-1);
					break;
				case '+':
					it.setLowerBound(1);
					it.setUpperBound(-1);
					break;
				default:
					throw new IllegalStateException(token);
				}
				return it;
			}
		};
	}

	@Override
	public ILexicalAtomIndependentFunctions getLexicalAtomIndependentFunctions() {
		return new ILexicalAtomIndependentFunctions() {
			
			@Override
			public Expression instantiate(
					TemplateBody<? extends Expression> basicLexicalAtom) {
				// TODO: Duplication?
				return unwrap(myInstantiator.instantiateTemplateBody(basicLexicalAtom));
			}
		};
	}

	@Override
	public IIterationFunctions getIterationFunctions() {
		return new IIterationFunctions() {
			
			@Override
			public TemplateBody<? extends Expression> createIteration(
					TemplateBody<? extends Expression> atom, IterationTemplate operation) {
				if (operation == null) {
					return atom;
				} 
				operation.setExpression(atom);
				return operation;
			}
		};
	}

	@Override
	public IGrammarFunctions getGrammarFunctions() {
		return new IGrammarFunctions() {
			
			@Override
			public Grammar instantiate(TemplateApplication<Grammar> templateApplication) {
				// TODO: Duplication!
				return unwrap(myInstantiator.instantiate(templateApplication));
			}
		};
	}

	@Override
	public IExpressionFunctions getExpressionFunctions() {
		return new IExpressionFunctions() {
			
			@Override
			public Expression instantiate(TemplateBody<? extends Expression> alternative) {
				// TODO: Duplication!
				return unwrap(myInstantiator.instantiateTemplateBody(alternative));
			}
		};
	}

	@Override
	public IDefaultTemplateFunctions getDefaultTemplateFunctions() {
		return new IDefaultTemplateFunctions() {
			
			@Override
			public void addRule(Grammar grammar1, Symbol symbol) {
				grammar1.getSymbols().add(symbol);
			}

			@Override
			public Grammar createGrammar() {
				myCurrentTemplateName = "<default>";
				return GrammarFactory.eINSTANCE.createGrammar();
			}

			@Override
			public Symbol instantiate(SymbolTemplate rule) {
				// TODO: Duplication?
				return unwrap(myInstantiator.instantiateTemplateBody(rule));
			}

			@Override
			public void cleanCurrentTemplateName() {
				myCurrentTemplateName = null;
			}
		};
	}

	@Override
	public IAtomFunctions getAtomFunctions() {
		return new IAtomFunctions() {
			
			@Override
			public TemplateBody<? extends Expression> createSymbolReference(String name) {
				return new LazySymbolReference(resolveName(name));
			}
			
			@Override
			public TemplateBody<? extends Expression> createEmpty() {
				return GrammarTemplateFactory.eINSTANCE.createEmptyTemplate();
			}
		};
	}

	@Override
	public IAlternativeFunctions getAlternativeFunctions() {
		return new IAlternativeFunctions() {
			
			@Override
			public TemplateBody<? extends Expression> getResult(
					ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> builder) {
				// TODO Duplication?
				return builder.getResult();
			}
			
			@Override
			public ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> createListBuilder() {
				return CombinationUtils.createAlternativeListBuilder();
			}
			
			@Override
			public void addItem(
					ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> builder,
					TemplateBody<? extends Expression> sequence) {
				// TODO: Duplication
				builder.item(sequence);
			}
		};
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(myModuleName + ": " + string, objects));
	}
	
	private final Template<?> getTemplate(String name) {
		return myParsingContext.getTemplate(resolveName(name));
	}

	private IKey resolveName(String name) {
		IKey key = myImportsModuleImplementationProvider.resolveName(name);
		if (key == null) {
			key = TemplateKey.createKey(myModuleName + "/" + myCurrentTemplateName, name);
		}
		return key;
	}
	
	public static <T> T unwrap(
			Collection<? extends T> result) {
		int size = result.size();
		if (size != 1) {
			throw new IllegalStateException("Template application has given " + size + " results");
		}
		return result.iterator().next();
	}
	
}