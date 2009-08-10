package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFExpression;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.Block;
import org.abreslav.grammatic.atf.CollectionAppending;
import org.abreslav.grammatic.atf.FunctionCall;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.Statement;
import org.abreslav.grammatic.atf.java.antlr.ANTLRAlternative;
import org.abreslav.grammatic.atf.java.antlr.ANTLRCharacterRange;
import org.abreslav.grammatic.atf.java.antlr.ANTLRExpression;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.ANTLRIteration;
import org.abreslav.grammatic.atf.java.antlr.ANTLRProduction;
import org.abreslav.grammatic.atf.java.antlr.ANTLRSequence;
import org.abreslav.grammatic.atf.java.antlr.AntlrFactory;
import org.abreslav.grammatic.atf.java.antlr.BeforeAfter;
import org.abreslav.grammatic.atf.java.antlr.IterationType;
import org.abreslav.grammatic.atf.java.antlr.LexicalLiteral;
import org.abreslav.grammatic.atf.java.antlr.LexicalRule;
import org.abreslav.grammatic.atf.java.antlr.Option;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.RuleCall;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.generator.ModuleImplementationBuilder;
import org.abreslav.grammatic.atf.java.antlr.generator.TypeUtils;
import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaAssignment;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaExpression;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.MethodCall;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationField;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableDefinition;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableReference;
import org.abreslav.grammatic.atf.java.parser.JavaTypeStringRepresentationProvider;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.atf.util.AtfSwitch;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;

// Takes as input: Grammatic grammars annotated with ATF specification (ATF.metadataScheme)
// Returns as output: ANTLR grammar model (antlr.ecore) with references to ModuleImplementations
public class ATFToANTLR {

	public static ANTLRGrammar generate(Grammar frontGrammar, 
			Collection<Grammar> usedGrammars, 
			ISymbolInclusionStrategy inclusionStrategy,
			IMetadataProvider metadataProvider,
			Map<SemanticModule, SemanticModuleDescriptor> semanticModuleDescriptors,
			Collection<ModuleImplementationProvider> moduleImplementationProviders,
			Collection<ModuleImplementation> moduleImplementations) {
		return new ATFToANTLR(metadataProvider).doGenerate(frontGrammar, usedGrammars, inclusionStrategy, semanticModuleDescriptors, moduleImplementationProviders, moduleImplementations);
	}
	
	private final JavaTypeStringRepresentationProvider myJavaTypeStringRepresentationProvider = new JavaTypeStringRepresentationProvider();
	
	private final ANTLRGrammar myResultGrammar = AntlrFactory.eINSTANCE.createANTLRGrammar();
	private final ATFToANTLRTrace myTrace = new ATFToANTLRTrace();
	private final SymbolMetadataProvider mySymbolMetadataProvider;
	private final IMetadataProvider myMetadataProvider;
	private final Map<ModuleImplementation, Method> myGetMethods = new HashMap<ModuleImplementation, Method>();
	private final Map<ModuleImplementation, Method> myReleaseMethods = new HashMap<ModuleImplementation, Method>();
	
	private ATFToANTLR(IMetadataProvider metadataProvider) {
		myMetadataProvider = metadataProvider;
		mySymbolMetadataProvider = new SymbolMetadataProvider(metadataProvider.getProjection(ATFMetadata.ATF_NAMESPACE));
	}

	private ANTLRGrammar doGenerate(
			Grammar frontGrammar, 
			Collection<Grammar> usedGrammars, 
			ISymbolInclusionStrategy inclusionStrategy,
			Map<SemanticModule, SemanticModuleDescriptor> semanticModuleDescriptors, 
			Collection<ModuleImplementationProvider> moduleImplementationProviders,
			Collection<ModuleImplementation> moduleImplementations) {
		
		Set<Grammar> allGrammars = new InitialObjectCreator(myResultGrammar, myTrace, myMetadataProvider, mySymbolMetadataProvider)
			.process(frontGrammar, usedGrammars, inclusionStrategy);
		
		Map<ModuleImplementation, Variable> moduleVariables = new HashMap<ModuleImplementation, Variable>();
		for (Grammar grammar : allGrammars) {
			processGlobalModules(grammar, semanticModuleDescriptors, moduleImplementations, moduleVariables);
		}
		
		for (Map.Entry<Symbol, LexicalRule> tokenToRule : myTrace.getAllTokenRulePairs()) {
			Symbol symbol = tokenToRule.getKey();
			fillInLexicalRule(tokenToRule.getValue(), symbol);
		}
		
		for (Entry<FunctionSignature, SyntacticalRule> functionToRule : myTrace.getAllFunctionRulePairs()) {
			SyntacticalRule rule = functionToRule.getValue();
			Symbol symbol = myTrace.getSymbolBySyntacticalRule(rule);
			fillInSyntacticalRule(rule, symbol, functionToRule.getKey(), moduleVariables);
		}
		
		new NameBeautifier().beautifyNames(myResultGrammar);
		
		myTrace.dumpModuleImplementationProviders(moduleImplementationProviders);
		
		return myResultGrammar;
	}

	private void processGlobalModules(Grammar grammar,
			Map<SemanticModule, SemanticModuleDescriptor> semanticModuleDescriptors, Collection<ModuleImplementation> moduleImplementations,
			Map<ModuleImplementation, Variable> moduleVariables) {
		
		IMetadataStorage metadataStorage = MetadataStorage.getMetadataStorage(grammar, myMetadataProvider);

		SemanticModule localModule = metadataStorage.readEObject(ATFMetadata.SEMANTIC_MODULE);
		if (localModule != null) {
			// create an implementation
			// create a field in a parser (a change to the model -- we must know that we do not need a constructor parameter for it)
			// put it into module-to-field map
			ModuleImplementationField field = createModuleImplementationAndFields(localModule, null, moduleImplementations, moduleVariables);
			field.setConstructorParameter(null);
			ModuleImplementation implementation = field.getModule();
			
			// put it inside a provider
			ModuleImplementationProvider moduleImplementationProvider = getModuleImplementationProvider(grammar);
			moduleImplementationProvider.getModuleImplementations().add(implementation);
			// add a method for creating it (no method for releasing it, by the way)
			Method getMethod = createGetMethod(moduleImplementationProvider, implementation);
			// initialize it inside a constructor of the parser (by calling provider's method, this requires a change to the model, a constructor does not have a body model by now)
			Variable providerVariable = getModuleImplementationProviderVariable(moduleImplementationProvider);
			MethodCall methodCall = SemanticsFactory.eINSTANCE.createMethodCall();
			methodCall.setMethod(getMethod);
			methodCall.setVariable(providerVariable);
			field.setInitExpression(methodCall);
		}

		List<SemanticModule> modules = metadataStorage.readEObjects(ATFMetadata.USED_SEMANTIC_MODULES);
		if (modules == null) {
			return;
		}
		for (SemanticModule semanticModule : modules) {
			if (moduleVariables.containsKey(semanticModule)) {
				continue;
			}
			SemanticModuleDescriptor descriptor = semanticModuleDescriptors.get(semanticModule);
			String pack = (String) descriptor.getOptions().get("modulePackage");
			createModuleImplementationAndFields(semanticModule, pack, moduleImplementations, moduleVariables);
		}
	}

	private ModuleImplementationField createModuleImplementationAndFields(SemanticModule semanticModule, String pack, Collection<ModuleImplementation> moduleImplementations, Map<ModuleImplementation, Variable> moduleVariables) {
		ModuleImplementation implementation = ModuleImplementationBuilder.INSTANCE.buildModuleImplementation(semanticModule, myTrace);
		implementation.setPackage(pack);
		moduleImplementations.add(implementation);
		ModuleImplementationField field = SemanticsFactory.eINSTANCE.createModuleImplementationField();
		field.setModule(implementation);
		
		field.setField(createImplVariable(semanticModule.getName(), implementation));
		moduleVariables.put(implementation, field.getField());

		field.setConstructorParameter(createImplVariable(semanticModule.getName(), implementation));

		myResultGrammar.getModuleFields().add(field);

		VariableReference reference = SemanticsFactory.eINSTANCE.createVariableReference();
		reference.setVariable(field.getConstructorParameter());
		field.setInitExpression(reference);

		return field;
	}
	
	private Variable createImplVariable(String name,
			Type type) {
		Variable variable = SemanticsFactory.eINSTANCE.createVariable();
		variable.setName(name);
		variable.setType(type);
		return variable;
	}

	private void fillInLexicalRule(LexicalRule rule, Symbol symbol) {
		fillProductions(rule, symbol, null, myMetadataProvider, null, null);
		
		IMetadataStorage symbolMetadata = mySymbolMetadataProvider.getSymbolMetadata(symbol);
		List<String> classes = symbolMetadata.readObjects(ATFMetadata.TOKEN_CLASSES);
		if (classes != null) {
			rule.setFragment(classes.contains(ANTLRMetadata.FRAGMENT_TOKEN_CLASS));
			rule.setWhitespace(classes.contains(ANTLRMetadata.WHITESPACE_TOKEN_CLASS));
		}
	}
	
	private void fillInSyntacticalRule(SyntacticalRule rule, Symbol symbol, FunctionSignature function, Map<ModuleImplementation, Variable> moduleVariables) {
		List<ATFAttribute> inputAttributes = function.getInputAttributes();
		Map<ATFAttribute, Variable> variableMap = new HashMap<ATFAttribute, Variable>();
		for (ATFAttribute atfAttribute : inputAttributes) {
			Variable variable = getOrCreateVariableInAMap(atfAttribute, variableMap);
			rule.getParameters().add(variable);
		}
		
		List<ATFAttribute> outputAttributes = function.getOutputAttributes();
		if (outputAttributes.size() > 1) {
			multipleReturnValuesAreNotSupported();
		}
		
		if (!outputAttributes.isEmpty()) {
			ATFAttribute atfAttribute = outputAttributes.get(0);
			rule.setResultVariable(getOrCreateVariableInAMap(atfAttribute, variableMap));
		} else {
			rule.setResultVariable(createImplVariable("<no result>", TypeUtils.getVoidType()));
		}
		
		Namespace namespace = myTrace.getNamespaceByFunction(function);
		assertNotNull(namespace);
		IMetadataProvider projection = myMetadataProvider.getProjection(namespace);
		fillProductions(rule, symbol, variableMap, projection, moduleVariables, function);
	}
	
	private void fillProductions(Rule rule, Symbol symbol,
			Map<ATFAttribute, Variable> parameters, IMetadataProvider metadataProvider, Map<ModuleImplementation, Variable> moduleVariables, FunctionSignature function) {
		RuleContentBuilder ruleContentBuilder = new RuleContentBuilder(metadataProvider, parameters, moduleVariables);
		ruleContentBuilder.fillProductions(rule, symbol, function);
	}

	private Variable getOrCreateVariableInAMap(ATFAttribute attribute,
			Map<ATFAttribute, Variable> map) {
		Variable variable = map.get(attribute);
		if (variable != null) {
			return variable;
		}
		variable = createImplVariable(attribute.getName(), 
				TypeUtils.getType((EGenericType) attribute.getType(), myJavaTypeStringRepresentationProvider));
		map.put(attribute, variable);
		return variable;
	}
	
	private Variable getModuleImplementationProviderVariable(ModuleImplementationProvider moduleImplementationProvider) {
		ImplementationPoolField field = myTrace.getPoolField(moduleImplementationProvider);
		if (field == null) {
			field = SemanticsFactory.eINSTANCE.createImplementationPoolField();
			field.setProvider(moduleImplementationProvider);
			// Strip the first "I"
			String varName = moduleImplementationProvider.getProviderInterface().getName().substring(1);
			Variable variable = createImplVariable(varName, 
				moduleImplementationProvider.getPoolsClass());
			field.setField(variable);
			
			field.setConstructorParameter(createImplVariable(varName, 
					moduleImplementationProvider.getProviderInterface()));
			
			myResultGrammar.getPoolFields().add(field);
			
			myTrace.putPoolField(moduleImplementationProvider, field);
		}
		return field.getField();
	}

	private ModuleImplementationProvider getModuleImplementationProvider(
			Grammar grammar) {
		GrammarOptions grammarOptions = InitialObjectCreator.getGrammarOptions(grammar, myMetadataProvider);
		ModuleImplementationProvider moduleImplementationProvider = myTrace.getModuleImplementationProvider(grammar);
		if (moduleImplementationProvider == null) {
			moduleImplementationProvider = SemanticsFactory.eINSTANCE.createModuleImplementationProvider();
			String name = JavaUtils.applyTypeNameConventions(grammarOptions.getName());
			String pack = grammarOptions.getPackage();
			moduleImplementationProvider.setPoolsClass(TypeUtils.createJavaType(pack, name + "Pools"));
			moduleImplementationProvider.setProviderInterface(TypeUtils.createJavaType(pack, "I" + name + "ModuleImplementationProvider"));
			
			myTrace.putModuleImplementationProvider(grammar, moduleImplementationProvider);
		}
		return moduleImplementationProvider;
	}
	
	private Method createGetMethod(ModuleImplementationProvider moduleImplementationProvider, ModuleImplementation moduleImplementation) {
		Method method = myGetMethods.get(moduleImplementation);
		if (method != null) {
			return method;
		}
		method = SemanticsFactory.eINSTANCE.createMethod();
		method.setName("get" + moduleImplementation.getName());
		method.setType(moduleImplementation);
		moduleImplementationProvider.getGetImplementationMethods().add(method);
		myGetMethods.put(moduleImplementation, method);
		return method;
	}
	
	private Method createReleaseMethod(ModuleImplementationProvider moduleImplementationProvider, ModuleImplementation moduleImplementation) {
		Method method = myReleaseMethods.get(moduleImplementation);
		if (method != null) {
			return method;
		}
		method = SemanticsFactory.eINSTANCE.createMethod();
		method.setName("release" + moduleImplementation.getName());
		method.setType(TypeUtils.getVoidType());
		moduleImplementationProvider.getReleaseImplementationMethods().add(method);
		Variable parameter = createImplVariable("functions", moduleImplementation);
		method.getParameters().add(parameter);
		myReleaseMethods.put(moduleImplementation, method);
		return method;
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(string, objects));
	}

	private void multipleReturnValuesAreNotSupported() {
		reportError("Multiple return values are not supported");
	}
	
	private void assertNotNull(Object x) {
		if (x == null) {
			throw new IllegalStateException();
		}
	}

	private final class RuleContentBuilder extends GrammarSwitch<ANTLRExpression> {
		
		private final Map<ATFAttribute, Variable> myVariables = new HashMap<ATFAttribute, Variable>();
		private final Map<ModuleImplementation, Variable> myModuleVariables = new HashMap<ModuleImplementation, Variable>();
		private final ExpressionConvertor myExpressionConvertor = new ExpressionConvertor();
		private final StatementConvertor myStatementConvertor = new StatementConvertor();
		
		private final IMetadataProvider myMetadataProvider;
		
		private RuleContentBuilder(IMetadataProvider metadataProvider, Map<ATFAttribute, Variable> parameters, Map<ModuleImplementation, Variable> moduleVariables) {
			myMetadataProvider = metadataProvider;
			if (parameters != null) {
				myVariables.putAll(parameters);
			}
			if (moduleVariables != null) {
				myModuleVariables.putAll(moduleVariables);
			}
		}

		public void fillProductions(Rule rule, Symbol symbol, FunctionSignature function) {
			CodeBlock before = SemanticsFactory.eINSTANCE.createCodeBlock();
			CodeBlock after = SemanticsFactory.eINSTANCE.createCodeBlock();
			
			// Symbol-level semantic module
			ModuleImplementation symbolModuleImplementation = myTrace.getModuleImplBySymbol(symbol);
			if (symbolModuleImplementation != null) {
				Variable variable = generateModuleInitAndRelease(before,
						after, symbolModuleImplementation,
						symbol);

				myModuleVariables.put(symbolModuleImplementation, variable);
			}
			
			// Function-level semantic module
			ModuleImplementation functionModuleImplementation = myTrace.getModuleImplByFunction(function);
			if (functionModuleImplementation != null) {
				
				Variable variable = generateModuleInitAndRelease(before,
						after, functionModuleImplementation,
						symbol);

				myModuleVariables.put(functionModuleImplementation, variable);
			}
			
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			JavaStatement beforeStatement = getBefore(symbolMetadata);
			if (beforeStatement != null) {
				before.getStatements().addAll(StructureUtils.contents(beforeStatement));
			}
			if (!before.getStatements().isEmpty()) {
				rule.setBefore(before);
			}
			for (Production production : symbol.getProductions()) {
				IMetadataStorage metadata = MetadataStorage.getMetadataStorage(production, myMetadataProvider);

				ANTLRProduction newProduction = AntlrFactory.eINSTANCE.createANTLRProduction();
				setBefore(newProduction, metadata);
				newProduction.setExpression(doSwitch(production.getExpression()));
				setAfter(newProduction, metadata);

				rule.getProductions().add(newProduction);
			}
			JavaStatement afterStatement = getAfter(symbolMetadata);
			if (afterStatement != null) {
				after.getStatements().addAll(0, StructureUtils.contents(afterStatement));
			}
			if (!after.getStatements().isEmpty()) {
				rule.setAfter(after);
			}
		}

		private Variable generateModuleInitAndRelease(CodeBlock before,
				CodeBlock after, ModuleImplementation moduleImplementation,
				Symbol symbol) {
			Variable variable = createImplVariable("functions__", moduleImplementation);
			VariableDefinition definition = SemanticsFactory.eINSTANCE.createVariableDefinition();
			definition.setVariable(variable);
			MethodCall createImplCall = SemanticsFactory.eINSTANCE.createMethodCall();
			ModuleImplementationProvider moduleImplementationProvider = getModuleImplementationProvider(StructureUtils.getGrammar(symbol));
			moduleImplementationProvider.getModuleImplementations().add(moduleImplementation);
			Variable providerVariable = getModuleImplementationProviderVariable(moduleImplementationProvider);
			createImplCall.setVariable(providerVariable);
			createImplCall.setMethod(createGetMethod(moduleImplementationProvider, moduleImplementation));
			definition.setValue(createImplCall);
			before.getStatements().add(definition);
			
			MethodCall releaseImplCall = SemanticsFactory.eINSTANCE.createMethodCall();
			releaseImplCall.setVariable(providerVariable);
			releaseImplCall.setMethod(createReleaseMethod(moduleImplementationProvider, moduleImplementation));
			VariableReference releaseArg = SemanticsFactory.eINSTANCE.createVariableReference();
			releaseArg.setVariable(variable);
			releaseImplCall.getArguments().add(releaseArg);
			after.getStatements().add(0, releaseImplCall);
			return variable;
		}

		private void doSwitchAll(List<Expression> expressions,
				List<ANTLRExpression> result) {
			for (Expression expression : expressions) {
				result.add(doSwitch(expression));
			}
		}

		@Override
		public ANTLRExpression doSwitch(EObject theEObject) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(theEObject, myMetadataProvider);
			// Cannot use setBefore() since it needs an object which is not yet created
			// This cannot be placed after the next line since new variables might be created 
			JavaStatement before = getBefore(metadata);
			ANTLRExpression result = super.doSwitch(theEObject);
			result.setBefore(before);
			
			if (metadata.isPresent("backtrack")) {
				Option backtrack = AntlrFactory.eINSTANCE.createOption();
				backtrack.setName("backtrack");
				backtrack.setValue("true");
				result.getOptions().add(backtrack);
			}

			ATFAttributeReference attributeRef = metadata.readEObject(ATFMetadata.ASSIGN_TEXT_TO_ATTRIBUTE);
			if (attributeRef != null) {
				ATFAttribute attribute = attributeRef.getAttribute();
				result.setAssignToVariable(getOrCreateVariable(attribute));
			}
			
			setAfter(result, metadata);
			return result;
		}

		@Override
		public ANTLRExpression caseAlternative(Alternative object) {
			ANTLRAlternative alt = AntlrFactory.eINSTANCE.createANTLRAlternative();
			doSwitchAll(object.getExpressions(), alt.getExpressions());
			return alt;
		}

		@Override
		public ANTLRExpression caseEmpty(Empty object) {
			return AntlrFactory.eINSTANCE.createANTLREmpty();
		}

		@Override
		public ANTLRExpression caseIteration(Iteration object) {
			int up = object.getUpperBound();
			int lo = object.getLowerBound();
			if (lo < 0 || lo > 1 || (up != -1 && up != 1)) {
				reportError("Unsupported multiplicity: %d..%d", lo, up);
			}
			ANTLRExpression inner = doSwitch(object.getExpression());
			IterationType type;
			if (lo == 0) {
				type = up == 1 ? IterationType.ZERO_OR_ONE : IterationType.ZERO_OR_MORE;
			} else {
				if (up == 1) {
					return inner;
				}
				type = IterationType.ONE_OR_MORE;
			}
			ANTLRIteration iteration = AntlrFactory.eINSTANCE.createANTLRIteration();
			iteration.setExpression(inner);
			iteration.setType(type);
			return iteration;
		}

		@Override
		public ANTLRExpression caseSequence(Sequence object) {
			ANTLRSequence seq = AntlrFactory.eINSTANCE.createANTLRSequence();
			List<ANTLRExpression> seqExpressions = seq.getExpressions();
			doSwitchAll(object.getExpressions(), seqExpressions);
			return seq;
		}

		@Override
		public ANTLRExpression caseStringExpression(StringExpression object) {
			LexicalLiteral literal = AntlrFactory.eINSTANCE.createLexicalLiteral();
			literal.setValue(object.getValue());
			return literal;
		}

		@Override
		public ANTLRExpression caseLexicalExpression(
				LexicalExpression object) {
			return doSwitch(object.getExpression());
		}

		@Override
		public ANTLRExpression caseCharacterRange(CharacterRange object) {
			ANTLRCharacterRange range = AntlrFactory.eINSTANCE.createANTLRCharacterRange();
			range.setLowerBound(object.getLowerBound());
			range.setUpperBound(object.getUpperBound());
			return range;
		}

		@Override
		public ANTLRExpression caseSymbolReference(final SymbolReference object) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, myMetadataProvider);
			
			RuleCall call;
			call = AntlrFactory.eINSTANCE.createRuleCall();
			if (metadata.isPresent(ATFMetadata.ASSOCIATED_WITH_TOKEN)) {
				call.setRule(myTrace.getRuleByToken(object.getSymbol()));
			} else {
				FunctionSignature function = metadata.readEObject(ATFMetadata.ASSOCIATED_FUNCTION);
				assertNotNull(function);
				SyntacticalRule rule = myTrace.getRuleByFunction(function);
				assertNotNull(rule);
				call.setRule(rule);
			}

			List<ATFAttributeReference> attributes = metadata.readEObjects(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
			if (attributes != null) {
				if (attributes.size() > 1) {
					multipleReturnValuesAreNotSupported();
				}
				if (!attributes.isEmpty()) {
					ATFAttribute attribute = attributes.get(0).getAttribute();
					Variable variable = getOrCreateVariable(attribute);
					call.setAssignToVariable(variable);
				}
			}
			
			List<ATFExpression> arguments = metadata.readEObjects(ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
			if (arguments != null) {
				convertArguments(arguments, call.getArguments());
			}
			
			return call;
		}

		private Variable getOrCreateVariable(ATFAttribute attribute) {
			return getOrCreateVariableInAMap(attribute, myVariables);
		}
		
		private Variable getVariable(ATFAttribute attribute) {
			return myVariables.get(attribute);
		}

		private void convertArguments(List<ATFExpression> atfArgs,
				List<JavaExpression> antlrArgs) {
			for (ATFExpression atfExpression : atfArgs) {
				antlrArgs.add(createSemanticalExpression(atfExpression));
			}
		}

		private JavaExpression createSemanticalExpression(ATFExpression atfExpression) {
			if (atfExpression instanceof FunctionCall) {
				return createMethodCall((FunctionCall) atfExpression); 
			} else if (atfExpression instanceof ATFAttributeReference) {
				return createVariableReference((ATFAttributeReference) atfExpression);
			}
			throw new IllegalArgumentException("Unknown expression class: " + 
					atfExpression.getClass().getSimpleName());
		}

		private JavaExpression createVariableReference(ATFAttributeReference ref) {
			VariableReference variableReference = SemanticsFactory.eINSTANCE.createVariableReference();
			variableReference.setVariable(getVariable(ref.getAttribute()));
			return variableReference;
		}

		private JavaExpression createMethodCall(FunctionCall functionCall) {
			MethodCall methodCall = SemanticsFactory.eINSTANCE.createMethodCall();
			Method method = myTrace.getMethodByFunction(functionCall.getFunction());
			methodCall.setMethod(method);
			methodCall.setVariable(myModuleVariables.get(method.getModule()));
			convertArguments(functionCall.getArguments(), methodCall.getArguments());
			return methodCall;
		}

		@Override
		public ANTLRExpression defaultCase(EObject object) {
			throw new IllegalStateException();
		}
		
		private void setBefore(BeforeAfter beforeAfter, IMetadataStorage metadata) {
			beforeAfter.setBefore(getBefore(metadata));
		}

		private void setAfter(BeforeAfter beforeAfter, IMetadataStorage metadata) {
			beforeAfter.setAfter(getAfter(metadata));
		}
		
		private JavaStatement getBefore(IMetadataStorage metadata) {
			return readStatement(metadata, ATFMetadata.BEFORE);
		}
		
		private JavaStatement getAfter(IMetadataStorage metadata) {
			return readStatement(metadata, ATFMetadata.AFTER);
		}

		private JavaStatement readStatement(IMetadataStorage metadata,
				String name) {
			EObject readEObject = metadata.readEObject(name);
			if (readEObject == null) {
				return null;
			}
			return myStatementConvertor.doSwitch(readEObject);
		}
		
		private final JavaExpression convertExpression(ATFExpression expression) {
			return myExpressionConvertor.doSwitch(expression);
		}
		
		private final class ExpressionConvertor extends AtfSwitch<JavaExpression> {
			
			@Override
			public JavaExpression caseATFAttributeReference(ATFAttributeReference object) {
				VariableReference ref = SemanticsFactory.eINSTANCE.createVariableReference();
				ref.setVariable(getVariable(object.getAttribute()));
				return ref;
			}
			
			@Override
			public MethodCall caseFunctionCall(FunctionCall object) {
				MethodCall call = SemanticsFactory.eINSTANCE.createMethodCall();
				FunctionSignature function = object.getFunction();
				Method method = myTrace.getMethodByFunction(function);
				call.setMethod(method);
				call.setVariable(myModuleVariables.get(method.getModule()));
				for (ATFExpression atfExpression : object.getArguments()) {
					call.getArguments().add(convertExpression(atfExpression));
				}
				return call;
			}
			
			@Override
			public JavaExpression defaultCase(EObject object) {
				throw new IllegalArgumentException();
			}
		}
		
		private final class StatementConvertor extends AtfSwitch<JavaStatement> {
			
			@Override
			public JavaStatement caseFunctionCall(FunctionCall object) {
				return myExpressionConvertor.caseFunctionCall(object);
			}
			
			@Override
			public JavaStatement caseATFAttributeAssignment(
					ATFAttributeAssignment object) {
				JavaAssignment assignment = SemanticsFactory.eINSTANCE.createJavaAssignment();
				List<ATFAttributeReference> attributes = object.getLeftSide();
				if (attributes.size() > 1) {
					multipleReturnValuesAreNotSupported();					
				}
				ATFAttributeReference attribute = attributes.get(0);
				assignment.setVariable(getOrCreateVariable(attribute.getAttribute()));
				assignment.setValue(convertExpression(object.getRightSide()));
				return assignment;
			}
			
			@Override
			public JavaStatement caseCollectionAppending(CollectionAppending object) {
				throw new IllegalArgumentException("Collection appending are not supported yet");
			}
			
			@Override
			public JavaStatement caseBlock(Block object) {
				CodeBlock block = SemanticsFactory.eINSTANCE.createCodeBlock();
				for (Statement statement : object.getStatements()) {
					JavaStatement substatement = doSwitch(statement);
					block.getStatements().addAll(StructureUtils.contents(substatement));
				}
				return block;
			}
			
			@Override
			public JavaStatement defaultCase(EObject object) {
				throw new IllegalArgumentException();
			}
		}
	}
	
}
