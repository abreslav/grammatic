package org.abreslav.grammatic.grammar.template.parser;

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
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
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
import org.abreslav.grammatic.utils.IErrorHandler;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class GrammaticGrammarTemplateBuilders implements
		IGrammaticGrammarTemplateBuilders {

	private final Map<String, TemplateParameter<?>> myTemplateParameters = new HashMap<String, TemplateParameter<?>>();
	private final IParsingContext myParsingContext;
	private final String myModuleName;
	private final TemplateInstantiatorInterpreter myInstantiator;
	private final ImportsBuilders myImportsBuilders;
	private String myCurrentTemplateName;

	public GrammaticGrammarTemplateBuilders(String moduleName, IParsingContext parsingContext) {
		myParsingContext = parsingContext;
		myModuleName = moduleName;
		myInstantiator = parsingContext.getInstantiator(myModuleName);
		myImportsBuilders = new ImportsBuilders(myParsingContext, new IErrorHandler<RuntimeException>() {

			@Override
			public void reportError(String string, Object... objects) {
				GrammaticGrammarTemplateBuilders.this.reportError(string, objects);
			}
			
		});
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(myModuleName + ": " + string, objects));
	}
	
	public IImportsBuilders getImportsBuilders() {
		return myImportsBuilders;
	}
	
	private IKey resolveName(String name) {
		IKey key = myImportsBuilders.resolveName(name);
		if (key == null) {
			key = TemplateKey.createKey(myModuleName + "/" + myCurrentTemplateName, name);
		}
		return key;
	}
	
	private final Template<?> getTemplate(String name) {
		return myParsingContext.getTemplate(resolveName(name));
	}

	@Override
	public IGrammar_Builder getGrammar_Builder() {
		return new IGrammar_Builder() {

			@Override
			public void init() {
				// Nothing
			}

			@Override
			public void release() {
				// Nothing
			}

			@Override
			public Grammar grammar_(Grammar defaultTemplate,
					TemplateApplication<Grammar> templateApplication) {
				if (defaultTemplate != null) {
					return defaultTemplate;
				}
				return unwrap(myInstantiator.instantiate(templateApplication));
			}
			
		};
	}
	
	@Override
	public IDefaultTemplateBuilder getDefaultTemplateBuilder() {
		return new IDefaultTemplateBuilder() {
			
			private GrammarTemplate myGrammar;

			@Override
			public void init() {
				myCurrentTemplateName = "<default>";
				myGrammar = GrammarTemplateFactory.eINSTANCE.createGrammarTemplate();
			}
			
			@Override
			public void release() {
				myGrammar = null;
				myCurrentTemplateName = null;
			}
			
			@Override
			public void rule(TemplateBody<Symbol> rule) {
				myGrammar.getSymbols().add(rule);
			}
			
			@Override
			public Grammar getResult() {
				return unwrap(myInstantiator.instantiateTemplateBody(myGrammar));
			}

			@Override
			public void attributeList(TupleValue attributeList) {
				IWritableAspect writableAspect = myParsingContext.getWritableAspectForTemplates();
				for (Attribute attribute : attributeList.getAttributes()) {
					writableAspect.setAttribute(myGrammar, 
							attribute.getNamespace(), 
							attribute.getName(), 
							attribute.getValue());
				}
				
			}
			
		};
	}

	@Override
	public ITemplateLibraryBuilder getTemplateLibraryBuilder() {
		return new ITemplateLibraryBuilder() {

			@Override
			public void init() {
			}

			@Override
			public void release() {
			}

			@Override
			public void templateDeclaration(
					Template<Grammar> templateDeclaration) {
				// Nothing to do :)
			}
		};
	}
	@Override
	public ITemplateApplicationBuilder getTemplateApplicationBuilder() {
		return new ITemplateApplicationBuilder() {
			
			private TemplateApplication<Grammar> myApplication;

			@Override
			public void init() {
				myApplication = TemplateFactory.eINSTANCE.createTemplateApplication();
			}
			
			@Override
			public void release() {
				myApplication = null;
			}

			@Override
			public TemplateApplication<Grammar> createResult() {
				return myApplication;
			}

			@Override
			@SuppressWarnings("unchecked")
			public void name(String name) {
				myApplication.setTemplate((Template<Grammar>) getTemplate(name));
			}

		};
	}
	
	@Override
	public ITemplateArgumentsBuilder getTemplateArgumentsBuilder() {
		return new ITemplateArgumentsBuilder() {
			
			private TemplateApplication<?> myApplication;
			private int myCount;

			@Override
			public void init(TemplateApplication<?> templateApp) {
				myApplication = templateApp;
				myCount = 0;
			}
			
			@Override
			public void release() {
				myApplication = null;
			}

			@Override
			public void templateArgument(
					TemplateArgument<Production> templateArgument) {
				// TODO: This code is risky: with imports it's OK, but if the template is defined in the same file,
				// it might not be loaded and this method will fail (unresolved proxy object)
				Template<?> template = myApplication.getTemplate();
				List<TemplateParameter<?>> parameters = template.getParameters();
				if (parameters.size() <= myCount) {
					reportError("%s: too many parameters", template.getName());
				}
				@SuppressWarnings("unchecked")
				TemplateParameter<Production> templateParameter = (TemplateParameter<Production>) parameters.get(myCount);
				EClass expectedType = templateParameter.getType();
				if (!EcoreUtil.equals(expectedType, templateParameter.getType())) {
					reportError("%s: argument %d has incorrect type: %s (expected %s)", template.getName(), myCount, "Production", expectedType.getName());
				}
				templateArgument.setParameter(templateParameter);
				myApplication.getArguments().add(templateArgument);
				myCount++;
			}
			
		};
	}
	
	@Override
	public ITemplateArgumentBuilder getTemplateArgumentBuilder() {
		return new ITemplateArgumentBuilder() {
			
			private TemplateArgument<Production> myArgument;

			@Override
			public void init() {
				myArgument = TemplateFactory.eINSTANCE.createTemplateArgument();
			}
			
			@Override
			public void release() {
				myArgument = null;
			}

			@Override
			public void production(Production production) {
				ObjectContainer<Production> container = TemplateFactory.eINSTANCE.createObjectContainer();
				container.setObject(production);
				myArgument.getValues().add(container);
			}
			
			@Override
			public TemplateArgument<Production> getResult() {
				return myArgument;
			}
			
		};
	}
	
	@Override
	public IProductionBuilder getProductionBuilder() {
		return new IProductionBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public Production production(
					TemplateBody<? extends Expression> alternative) {
				Production result = GrammarFactory.eINSTANCE.createProduction();
				result.setExpression(unwrap(myInstantiator.instantiateTemplateBody(alternative)));
				return result;
			}

		};
	}

	@Override
	public ITemplateDeclarationBuilder getTemplateDeclarationBuilder() {
		return new ITemplateDeclarationBuilder() {
			
			private Template<Grammar> myTemplate;

			@Override
			public void init() {
				myTemplate = TemplateFactory.eINSTANCE.createTemplate();
			}
			
			@Override
			public void release() {
				myTemplate = null;
				myTemplateParameters.clear();
				myCurrentTemplateName = null;
			}

			@Override
			public void name(String name) {
				myTemplate.setName(name);
				myParsingContext.registerTemplate(myModuleName, myTemplate);
				myCurrentTemplateName = name;
			}

			@Override
			public void templateBody(TemplateBody<Grammar> templateBody) {
				myTemplate.setBody(templateBody);
			}

			@Override
			public void type(EClass type) {
				if (!EcoreUtil.equals(type, GrammarPackage.eINSTANCE.getGrammar())) {
					reportError("Forbidden type for template parameter: %s", type.getName());
				}
			}
			
			@Override
			public Template<Grammar> createResult() {
				return myTemplate;
			}
			
		};
	}
	
	@Override
	public ITypeBuilder getTypeBuilder() {
		return new ITypeBuilder() {
			
			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
				
			}

			@Override
			public EClass type(String name) {
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
	public ITemplateParametersBuilder getTemplateParametersBuilder() {
		return new ITemplateParametersBuilder() {
			
			private Template<?> myTemplate;

			@Override
			public void init(Template<?> template) {
				myTemplate = template;
			}
			
			@Override
			public void release() {
				myTemplate = null;
			}

			@Override
			public void templateParameter(TemplateParameter<?> templateParameter) {
				String name = templateParameter.getName();
				if (myTemplateParameters.containsKey(name)) {
					reportError("Template parameter redeclared: %s", name);
				}
				myTemplateParameters.put(name, templateParameter);
				myTemplate.getParameters().add(templateParameter);
			}
			
		};
	}
	
	@Override
	public ITemplateParameterBuilder getTemplateParameterBuilder() {
		return new ITemplateParameterBuilder() {
			
			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
				
			}

			@Override
			public TemplateParameter<?> templateParameter(String refer,
					String copy, EClass type, IterationTemplate operation,
					String name) {
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
				if (refer != null) {
					parameter.setUsagePolicy(ParameterUsagePolicy.REFER_ONLY);
				} else if (copy != null) {
					parameter.setUsagePolicy(ParameterUsagePolicy.COPY);
				} else {
					parameter.setUsagePolicy(ParameterUsagePolicy.CONTAIN_ONCE);
				}
				return parameter;
			}

		};
	}
	
	@Override
	public ITemplateBodyBuilder getTemplateBodyBuilder() {
		return new ITemplateBodyBuilder() {
			
			private GrammarTemplate myGrammarTemplate;

			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				myGrammarTemplate = null;
			}
			
			@Override
			public TemplateBody<Grammar> createResult() {
				myGrammarTemplate = GrammarTemplateFactory.eINSTANCE.createGrammarTemplate();
				return myGrammarTemplate;
			}

			@Override
			public void rule(TemplateBody<Symbol> rule) {
				myGrammarTemplate.getSymbols().add(rule);
			}

		};
	}
	
	@Override
	public ITemplateParameterReferenceBuilder getTemplateParameterReferenceBuilder() {
		return new ITemplateParameterReferenceBuilder() {
			
			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
				
			}

			@Override
			public ParameterReference<?> templateParameterReference(String name) {
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
	public IProductionTemplateBuilder getProductionTemplateBuilder() {
		return new IProductionTemplateBuilder() {
			
			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
				
			}

			@Override
			public TemplateBody<Production> alternative(
					TemplateBody<? extends Expression> alternative) {
				ProductionTemplate result = GrammarTemplateFactory.eINSTANCE.createProductionTemplate();
				result.setExpression(alternative);
				return result;
			}

			@Override
			@SuppressWarnings("unchecked")
			public TemplateBody<Production> templateParameterReference(
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
	public IRuleBuilder getRuleBuilder() {
		return new IRuleBuilder() {
			private SymbolTemplate mySymbol;

			@Override
			public void init() {
				mySymbol = GrammarTemplateFactory.eINSTANCE.createSymbolTemplate();
			}
			
			@Override
			public void release() {
				mySymbol = null;
			}
			
			@Override
			public void name(String name) {
				mySymbol.setName(name);
			}

			@Override
			public void productionTemplate(
					TemplateBody<Production> productionTemplate) {
				mySymbol.getProductions().add(productionTemplate);
			}

			@Override
			public TemplateBody<Symbol> createResult() {
				return mySymbol;
			}
			
		};
	}

	@Override
	public IAlternativeBuilder getAlternativeBuilder() {
		return new IAlternativeBuilder() {
			private final ListBuilder<TemplateBody<? extends Expression>, AlternativeTemplate> myBuilder = CombinationUtils.createAlternativeListBuilder();
			@Override
			public void init() {
				myBuilder.init();
			}
			
			@Override
			public void release() {
				myBuilder.init();
			}
			
			@Override
			public TemplateBody<? extends Expression> getResult() {
				return myBuilder.getResult();
			}

			@Override
			public void sequence(TemplateBody<? extends Expression> sequence) {
				myBuilder.item(sequence);
			}
			
		};
	}

	@Override
	public IExpressionBuilder getExpressionBuilder() {
		return new IExpressionBuilder() {
			
			@Override
			public void init() {
				// Nothing
				
			}
			
			@Override
			public void release() {
				// Nothing
				
			}

			@Override
			public Expression expression(TemplateBody<? extends Expression> alternative) {
				return unwrap(myInstantiator.instantiateTemplateBody(alternative));
			}
			
		};
	}

	@Override
	public ISequenceBuilder getSequenceBuilder() {
		return new ISequenceBuilder() {
			private final ListBuilder<TemplateBody<? extends Expression>, SequenceTemplate> myBuilder = CombinationUtils.createSequenceListBuilder();

			@Override
			public void init() {
				myBuilder.init();
			}
			
			@Override
			public void release() {
				myBuilder.init();
			}
			
			@Override
			public TemplateBody<? extends Expression> getResult() {
				return myBuilder.getResult();
			}

			@Override
			public void iteration(TemplateBody<? extends Expression> iteration) {
				myBuilder.item(iteration);
			}
			
		};
	}

	@Override
	public IIterationBuilder getIterationBuilder() {
		return new IIterationBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public TemplateBody<? extends Expression> iteration(
					TemplateBody<? extends Expression> atom,
					IterationTemplate operation) {
				if (operation == null) {
					return atom;
				} 
				operation.setExpression(atom);
				return operation;
			}
			
		};
	}

	@Override
	public IOperationBuilder getOperationBuilder() {
		return new IOperationBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public IterationTemplate token(String token) {
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
				}
				return it;
			}

			@Override
			public IterationTemplate empty() {
				return null;
			}
			
		};
	}

	@Override
	public IAtomBuilder getAtomBuilder() {
		return new IAtomBuilder() {

			@Override
			public void init() {
				// Nothing
			}
			
			@Override
			public void release() {
				// Nothing
			}
			
			@Override
			public TemplateBody<? extends Expression> alternative(TemplateBody<? extends Expression> alternative) {
				return alternative;
			}

			@Override
			public TemplateBody<? extends Expression> empty() {
				return GrammarTemplateFactory.eINSTANCE.createEmptyTemplate();
			}

			@Override
			public TemplateBody<? extends Expression> lexicalAtom(TemplateBody<? extends Expression> lexicalAtom) {
				return lexicalAtom;
			}

			@Override
			public TemplateBody<? extends Expression> name(String name) {
				return new LazySymbolReference(resolveName(name));
			}
			
		};
	}

	@Override
	public ILexicalAtomBuilder getLexicalAtomBuilder() {
		return new ILexicalAtomBuilder() {
			
			@Override
			public void init() {
			}
			
			@Override
			public void release() {
				// Nothing
			}

			@Override
			public TemplateBody<? extends Expression> alternative(TemplateBody<? extends Expression> alternative) {
				return alternative;
			}

			@Override
			public TemplateBody<? extends Expression> basicLexicalAtom(TemplateBody<? extends Expression> basicLexicalAtom) {
				return basicLexicalAtom;
			}

		};
	}

	@Override
	public ILexicalAtomIndependentBuilder getLexicalAtomIndependentBuilder() {
		return new ILexicalAtomIndependentBuilder() {

			@Override
			public void init() {
				
			}
			
			@Override
			public void release() {
				
			}

			@Override
			public Expression lexicalAtomIndependent(
					TemplateBody<? extends Expression> basicLexicalAtom) {
				return unwrap(myInstantiator.instantiateTemplateBody(basicLexicalAtom));
			}
			
		};
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
