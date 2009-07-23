package org.abreslav.grammatic.atf.java.antlr.generator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFExpression;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.AtfFactory;
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
import org.abreslav.grammatic.atf.java.antlr.semantics.CodeBlock;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaAssignment;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaExpression;
import org.abreslav.grammatic.atf.java.antlr.semantics.JavaStatement;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.MethodCall;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsFactory;
import org.abreslav.grammatic.atf.java.antlr.semantics.Variable;
import org.abreslav.grammatic.atf.java.antlr.semantics.VariableReference;
import org.abreslav.grammatic.atf.java.parser.JavaTypeStringRepresentationProvider;
import org.abreslav.grammatic.atf.util.AtfSwitch;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Combination;
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
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

// Takes as input: Grammatic grammars annotated with ATF specification (ATF.metadataScheme)
// Returns as output: ANTLR grammar model (antlr.ecore) with references to ModuleImplementations
public class ATFToANTLR {

	public interface ISymbolInclusionStrategy {
		boolean doInclude(Symbol symbol, boolean isFront, IMetadataStorage metadata);
	}
	
	public static final ISymbolInclusionStrategy USED_FORCED_FRONT_WHITESPACE = new ISymbolInclusionStrategy() {
		
		@Override
		public boolean doInclude(Symbol symbol, boolean isFront,
				IMetadataStorage metadata) {
			if (isFront || metadata.isPresent(FORCE_RETAIN_ATTRIBUTE)) {
				return true;
			}
			List<String> classes = metadata.readObjects(ATFMetadata.TOKEN_CLASSES);
			return classes != null && classes.contains(WHITESPACE_TOKEN_CLASS);
		}
	}; 
	
	public static final String FRAGMENT_TOKEN_CLASS = "fragment";
	public static final String FORCE_RETAIN_ATTRIBUTE = "forceRetain";
	public static final String WHITESPACE_TOKEN_CLASS = "whitespace";

	public static ANTLRGrammar generate(Grammar frontGrammar, 
			Collection<Grammar> usedGrammars, 
			ISymbolInclusionStrategy inclusionStrategy,
			IMetadataProvider metadataProvider,
			Collection<ModuleImplementationProvider> moduleImplementationProviders) {
		return new ATFToANTLR(metadataProvider).doGenerate(frontGrammar, usedGrammars, inclusionStrategy, moduleImplementationProviders);
	}
	
	private final Map<Symbol, IMetadataStorage> mySymbolsMetadata = EMFProxyUtil.customHashMap();
	private final Map<FunctionSignature, SyntacticalRule> myFunctionToRule = new HashMap<FunctionSignature, SyntacticalRule>();
	private final Map<FunctionSignature, Namespace> myFunctionToNamespace = new HashMap<FunctionSignature, Namespace>();
	private final Map<Symbol, LexicalRule> myTokenToRule = new HashMap<Symbol, LexicalRule>();
	private final Map<SyntacticalRule, Symbol> mySyntacticalRuleToSymbol = new HashMap<SyntacticalRule, Symbol>();
	private final Map<FunctionSignature, Method> myFunctionToMethod = new HashMap<FunctionSignature, Method>();
	private final IMetadataProvider myMetadataProvider;
	
	private ATFToANTLR(IMetadataProvider metadataProvider) {
		myMetadataProvider = metadataProvider;
	}

	private ANTLRGrammar doGenerate(
			Grammar frontGrammar, 
			Collection<Grammar> usedGrammars, 
			ISymbolInclusionStrategy inclusionStrategy,
			Collection<ModuleImplementationProvider> moduleImplementationProviders) {
		
		ANTLRGrammar resultGrammar = AntlrFactory.eINSTANCE.createANTLRGrammar();
		
		new GrammarGraphTraverser().process(frontGrammar, usedGrammars, inclusionStrategy);
		
		HashSet<Grammar> allGrammars = new HashSet<Grammar>(usedGrammars);
		allGrammars.add(frontGrammar);
		
//		createAllModuleImplementationProviders(allGrammars, symbols, moduleImplementationProviders);
		
		for (Map.Entry<Symbol, LexicalRule> tokenToRule : myTokenToRule.entrySet()) {
			fillInLexicalRule(tokenToRule.getValue(), tokenToRule.getKey());
		}
		
		for (Entry<FunctionSignature, SyntacticalRule> functionToRule : myFunctionToRule.entrySet()) {
			SyntacticalRule rule = functionToRule.getValue();
			Symbol symbol = mySyntacticalRuleToSymbol.get(rule);
			fillInSyntacticalRule(rule, symbol, functionToRule.getKey());
		}
		
		return resultGrammar;
	}

	private void fillInLexicalRule(LexicalRule rule, Symbol symbol) {
		fillProductions(rule, symbol, Collections.<ATFAttribute, Variable>emptyMap(), myMetadataProvider);
		
		IMetadataStorage symbolMetadata = getSymbolMetadata(symbol);
		List<String> classes = symbolMetadata.readObjects(ATFMetadata.TOKEN_CLASSES);
		if (classes != null) {
			rule.setFragment(classes.contains(FRAGMENT_TOKEN_CLASS));
			rule.setWhitespace(classes.contains(WHITESPACE_TOKEN_CLASS));
		}
	}
	
	private void fillInSyntacticalRule(SyntacticalRule rule, Symbol symbol, FunctionSignature function) {
		List<ATFAttribute> inputAttributes = function.getInputAttributes();
		Map<ATFAttribute, Variable> map = new HashMap<ATFAttribute, Variable>();
		for (ATFAttribute atfAttribute : inputAttributes) {
			Variable variable = getOrCreateVariableInAMap(atfAttribute, map);
			rule.getParameters().add(variable);
		}
		
		List<ATFAttribute> outputAttributes = function.getOutputAttributes();
		if (outputAttributes.size() > 1) {
			multipleReturnValuesAreNotSupported();
		}
		
		if (!outputAttributes.isEmpty()) {
			ATFAttribute atfAttribute = outputAttributes.get(0);
			rule.setResultVariable(getOrCreateVariableInAMap(atfAttribute, map));
		}
		
		IMetadataProvider projection = myMetadataProvider.getProjection(myFunctionToNamespace.get(function));
		fillProductions(rule, symbol, map, projection);
	}
	
	private void fillProductions(Rule rule, Symbol symbol,
			Map<ATFAttribute, Variable> parameters, IMetadataProvider metadataProvider) {
		RuleContentBuilder ruleContentBuilder = new RuleContentBuilder(metadataProvider, parameters);
		ruleContentBuilder.fillProduction(rule, symbol);
	}

	private void processSymbol(Symbol symbol) {
		IMetadataStorage metadata = getSymbolMetadata(symbol);
		
		if (metadata.isPresent(ATFMetadata.TOKEN)) {
			createLexicalRuleStub(symbol, metadata);
			return;
		}
		
		// Create default semantic module (if not empty)
		processSemanticFunctions(symbol.getName(), metadata);
		// Obtain syntactical functions
		Map<String, Namespace> functionNamespaces = metadata.readObject(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
		for (Namespace namespace : functionNamespaces.values()) {
			IMetadataProvider projection = myMetadataProvider.getProjection(namespace);
			IMetadataStorage projectedMetadata = MetadataStorage.getMetadataStorage(symbol, projection);
			FunctionSignature function = projectedMetadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);

			processSemanticFunctions(function.getName(), projectedMetadata);
			
			createSyntacticalRuleStub(symbol, function);
		}
	}

	private LexicalRule createLexicalRuleStub(Symbol symbol, IMetadataStorage metadata) {
		LexicalRule result = AntlrFactory.eINSTANCE.createLexicalRule();
		
		result.setName(symbol.getName().toUpperCase()); // TODO : Scope, To underscored upper case
			
		myTokenToRule.put(symbol, result);
		return result;
	}

	private SyntacticalRule createSyntacticalRuleStub(Symbol symbol, FunctionSignature function) {
		SyntacticalRule result = AntlrFactory.eINSTANCE.createSyntacticalRule();

		result.setName(symbol.getName()); // TODO : Scope, To CamelCase
		
		myFunctionToRule.put(function, result);
		mySyntacticalRuleToSymbol.put(result, symbol);
		return result;
	}


	private void processSemanticFunctions(String name,
			IMetadataStorage metadata) {
		List<FunctionSignature> semanticFunctions = metadata.readEObjects(ATFMetadata.SEMANTIC_FUNCTIONS);
		if (!semanticFunctions.isEmpty()) {
			SemanticModule module = createSemanticModule(name, semanticFunctions);
			// TODO: put it somewhere
		}
	}

	private SemanticModule createSemanticModule(String name,
			List<FunctionSignature> semanticFunctions) {
		SemanticModule module = AtfFactory.eINSTANCE.createSemanticModule();
		module.setName(name);
		module.getFunctions().addAll(semanticFunctions);
		return module;
	}

	private IMetadataStorage getSymbolMetadata(Symbol symbol) {
		IMetadataStorage metadata = mySymbolsMetadata.get(symbol);
		if (metadata == null) {
			metadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			mySymbolsMetadata.put(symbol, metadata);
		}
		return metadata;		
	}

	private Variable getOrCreateVariableInAMap(ATFAttribute attribute,
			Map<ATFAttribute, Variable> map) {
		Variable variable = map.get(attribute);
		if (variable != null) {
			return variable;
		}
		variable = SemanticsFactory.eINSTANCE.createVariable();
		variable.setName(attribute.getName()); // TODO : Scope
		variable.setType(JavaTypeStringRepresentationProvider.DEFAULT.getStringRepresentation(attribute.getType()));
		map.put(attribute, variable);
		return variable;
	}

	private void reportError(String string, Object... objects) {
		throw new IllegalArgumentException(String.format(string, objects));
	}

	private void multipleReturnValuesAreNotSupported() {
		reportError("Multiple return values are not supported");
	}

	private class GrammarGraphTraverser {
		private final Set<Symbol> myVisited = EMFProxyUtil.customHashSet();
		
		private final GrammarSwitch<INull> mySymbolReferenceTraverser = new GrammarSwitch<INull>() {
			
			public INull caseSymbol(Symbol object) {
				List<Production> productions = object.getProductions();
				for (Production production : productions) {
					doSwitch(production);
				}
				return INull.NULL;
			};
			
			public INull caseProduction(Production object) {
				return doSwitch(object.getExpression());
			};
			
			@Override
			public INull caseCombination(Combination object) {
				for (Expression expr : object.getExpressions()) {
					doSwitch(expr);
				}
				return INull.NULL;
			}
			
			@Override
			public INull caseIteration(Iteration object) {
				doSwitch(object.getExpression());
				return INull.NULL;
			}
			
			@Override
			public INull caseLexicalExpression(LexicalExpression object) {
				doSwitch(object.getExpression());
				return INull.NULL;
			}
			
			@Override
			public INull caseSymbolReference(SymbolReference object) {
				visit(object.getSymbol());
				return INull.NULL;
			}
			
			@Override
			public INull caseExpression(Expression object) {
				return INull.NULL;
			}
			
			public INull defaultCase(EObject object) {
				throw new IllegalStateException();
			};
		};

		public Set<Symbol> process(Grammar frontGrammar, Collection<Grammar> usedGrammars, ISymbolInclusionStrategy inclusionStrategy) {
			myVisited.clear();
			processGramar(frontGrammar, true, inclusionStrategy);
			for (Grammar grammar : usedGrammars) {
				processGramar(grammar, false, inclusionStrategy);
			}
			return myVisited;
		}

		private void processGramar(Grammar grammar,
				boolean front, ISymbolInclusionStrategy inclusionStrategy) {
			for (Symbol symbol : grammar.getSymbols()) {
				IMetadataStorage metadata = getSymbolMetadata(symbol);
				if (!myVisited.contains(symbol) 
						&& inclusionStrategy.doInclude(symbol, front, metadata)) {
					visit(symbol);
				}
			}
		}
		
		private void visit(Symbol symbol) {
			if (myVisited.contains(symbol)) {
				return;
			}
			myVisited.add(symbol);
			
			processSymbol(symbol);
			
			mySymbolReferenceTraverser.doSwitch(symbol);
		}
		
	}
	
	private final class RuleContentBuilder extends GrammarSwitch<ANTLRExpression> {
		
		private final Map<ATFAttribute, Variable> myVariables = new HashMap<ATFAttribute, Variable>();
		// TODO : Fill this in somehow (a variable for each module)
		private final Map<ModuleImplementation, String> myModuleVariableNames = new HashMap<ModuleImplementation, String>();
		private final ExpressionConvertor myExpressionConvertor = new ExpressionConvertor();
		private final StatementConvertor myStatementConvertor = new StatementConvertor();
		
		private final IMetadataProvider myMetadataProvider;
		
		private RuleContentBuilder(IMetadataProvider metadataProvider, Map<ATFAttribute, Variable> parameters) {
			myMetadataProvider = metadataProvider;
			myVariables.putAll(parameters);
		}

		public void fillProduction(Rule rule, Symbol symbol) {
			// TODO Prepend builder initialization
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			setBefore(rule, symbolMetadata);
			for (Production production : symbol.getProductions()) {
				IMetadataStorage metadata = MetadataStorage.getMetadataStorage(production, myMetadataProvider);

				ANTLRProduction newProduction = AntlrFactory.eINSTANCE.createANTLRProduction();
				setBefore(newProduction, metadata);
				newProduction.setExpression(doSwitch(production.getExpression()));
				setAfter(newProduction, metadata);

				rule.getProductions().add(newProduction);
			}
			setAfter(rule, symbolMetadata);
			// TODO append builder finalization
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
				result.setAssignToVariable(getOrCreateVariable(attributeRef.getAttribute()));
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
				call.setRule(myTokenToRule.get(object.getSymbol()));
			} else {
				FunctionSignature function = metadata.readEObject(ATFMetadata.ASSOCIATED_FUNCTION);
				call.setRule(myFunctionToRule.get(function));
			}

			List<ATFAttributeReference> attributes = metadata.readEObjects(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
			if (attributes.size() > 1) {
				multipleReturnValuesAreNotSupported();
			}
			if (!attributes.isEmpty()) {
				ATFAttribute attribute = attributes.get(0).getAttribute();
				Variable variable = getOrCreateVariable(attribute);
				call.setAssignToVariable(variable);
			}
			
			List<ATFExpression> arguments = metadata.readEObjects(ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
			convertArguments(arguments, call.getArguments());
			
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
			Method method = myFunctionToMethod.get(functionCall.getFunction());
			methodCall.setMethod(method);
			methodCall.setVariable(myModuleVariableNames.get(method.getModule()));
			convertArguments(functionCall.getArguments(), methodCall.getArguments());
			return methodCall;
		}

		@Override
		public ANTLRExpression defaultCase(EObject object) {
			throw new IllegalStateException();
		}
		
		private void setBefore(BeforeAfter beforeAfter, IMetadataStorage metadata) {
			setBeforeAfter(beforeAfter, metadata, ATFMetadata.BEFORE);
		}

		private void setAfter(BeforeAfter beforeAfter, IMetadataStorage metadata) {
			setBeforeAfter(beforeAfter, metadata, ATFMetadata.AFTER);
		}
		
		private void setBeforeAfter(BeforeAfter beforeAfter,
				IMetadataStorage metadata, String attributeName) {
			JavaStatement before = myStatementConvertor.doSwitch(metadata.readEObject(attributeName));
			beforeAfter.setBefore(before);
		}

		private JavaStatement getBefore(IMetadataStorage metadata) {
			JavaStatement before = myStatementConvertor.doSwitch(metadata.readEObject(ATFMetadata.BEFORE));
			return before;
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
				Method method = myFunctionToMethod.get(function);
				call.setMethod(method);
				call.setVariable(myModuleVariableNames.get(method.getModule()));
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
				assignment.setVariable(getVariable(attribute.getAttribute()));
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
					block.getStatements().add(doSwitch(statement));
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
