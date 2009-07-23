package org.abreslav.grammatic.antlr.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRAlternative;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRCharacterRange;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRExpression;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRGrammar;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRIteration;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRProduction;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRSequence;
import org.abreslav.grammatic.antlr.generator.antlr.AntlrFactory;
import org.abreslav.grammatic.antlr.generator.antlr.AssignableValueReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.IterationType;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalLiteral;
import org.abreslav.grammatic.antlr.generator.antlr.LexicalRule;
import org.abreslav.grammatic.antlr.generator.antlr.Option;
import org.abreslav.grammatic.antlr.generator.antlr.Parameter;
import org.abreslav.grammatic.antlr.generator.antlr.ParameterReferenceArgument;
import org.abreslav.grammatic.antlr.generator.antlr.Rule;
import org.abreslav.grammatic.antlr.generator.antlr.RuleCall;
import org.abreslav.grammatic.antlr.generator.antlr.SyntacticalRule;
import org.abreslav.grammatic.antlr.generator.antlr.TokenSwitch;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderFactory;
import org.abreslav.grammatic.antlr.generator.parsers.ImportParser;
import org.abreslav.grammatic.antlr.generator.parsers.RuleCallParser;
import org.abreslav.grammatic.antlr.generator.parsers.RuleHeaderParser;
import org.abreslav.grammatic.antlr.generator.parsers.RuleCallBuilders.IResolver;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.metadata.util.ValueStream;
import org.abreslav.grammatic.parsingutils.DeferredResolver;
import org.abreslav.grammatic.utils.CustomLinkedHashMap;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


public class GrammaticToANTLR {

	private class RuleProductionMaker extends
			GrammarSwitch<ANTLRExpression> {
		// TODO: Looks like this field is no longer in use
		private boolean myInLexical = true;

		@Override
		public ANTLRExpression doSwitch(EObject theEObject) {
			ANTLRExpression result = super.doSwitch(theEObject);
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(theEObject, myMetadataProvider);
			if (metadata.isPresent("backtrack")) {
				Option backtrack = AntlrFactory.eINSTANCE.createOption();
				backtrack.setName("backtrack");
				backtrack.setValue("true");
				result.getOptions().add(backtrack);
			}
			return result;
		}
		
		@Override
		public ANTLRExpression caseAlternative(Alternative object) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, myMetadataProvider);
			if (metadata.isPresent("tokenSwitch")) {
				return createTokenSwitch(object, metadata);
			}
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
			EList<ANTLRExpression> seqExpressions = seq.getExpressions();
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
			boolean oldInLexical = myInLexical;
			myInLexical = true;
			ANTLRExpression result = doSwitch(object.getExpression());
			myInLexical = oldInLexical;
			return result;
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
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(object.getSymbol(), myMetadataProvider);
			RuleCall call;
			if (symbolMetadata.isPresent("lexical")) {
				call = AntlrFactory.eINSTANCE.createRuleCall();
				myRuleResolver.addReferrer(call, object.getSymbol().getName());
			} else {
				call = createSyntacticalRuleCall(object, metadata);
			}
			call.setSeparate(metadata.isPresent("separate"));
			call.setVariableName(metadata.readId("variable"));
			registerRuleCall(call.getVariableName(), call);
			return call;
		}
		
		protected void registerRuleCall(String variableName, RuleCall call) {
		}

		@Override
		public ANTLRExpression defaultCase(EObject object) {
			throw new IllegalStateException();
		}

		private void doSwitchAll(EList<Expression> expressions,
				EList<ANTLRExpression> result) {
			for (Expression expression : expressions) {
				result.add(doSwitch(expression));
			}
		}

		protected RuleCall createSyntacticalRuleCall(SymbolReference object,
				IMetadataStorage metadata) {
			reportError("Syntactical calls are not allowed here: %s", object.getSymbol().getName());
			return null;
		}		
		
	}
	
	private class SyntacticalRuleProductionMaker extends RuleProductionMaker {
		private final SyntacticalRule myRule;
		private final Map<String, Parameter> myNamesToParameters;
		private final Map<String, RuleCall> myVarsToRuleCalls = new HashMap<String, RuleCall>();

		public SyntacticalRuleProductionMaker(SyntacticalRule rule) {
			myNamesToParameters = new HashMap<String, Parameter>();
			for (Parameter parameter : rule.getParameters()) {
				myNamesToParameters.put(parameter.getName(), parameter);
			}
			myRule = rule;
		}

		@Override
		protected void registerRuleCall(String variable, RuleCall call) {
			if (variable == null) {
				return;
			}
			myVarsToRuleCalls.put(variable, call);
		}
		
		@Override
		protected RuleCall createSyntacticalRuleCall(final SymbolReference object,
				IMetadataStorage metadata) {
			Value callValue = metadata.readValue("call");
			if (callValue == null) {
				SyntacticalRule defaultRule = myDefaultRules.get(object.getSymbol());
				if (defaultRule != null 
						&& defaultRule.getParameters().isEmpty()) {
					RuleCall ruleCall = AntlrFactory.eINSTANCE.createRuleCall();
					ruleCall.setRule(defaultRule);
					return ruleCall;
				}
				String error = defaultRule == null ? "no default rule" : "default rule must not have parameters";
				reportError("%s calls %s: No calls specified (%s)", 
										myRule.getName(), object.getSymbol().getName(), error);
			}
			List<Value> ruleCallValue;
			if (callValue instanceof MultiValue) {
				MultiValue call = (MultiValue) callValue;
				ruleCallValue = call.getValues();
			} else if (callValue instanceof TupleValue) {
				TupleValue tuple = (TupleValue) callValue;
				MetadataStorage tupleContent = new MetadataStorage(tuple.getAttributes());
				ruleCallValue = tupleContent.readMulti(myRule.getName());
			} else {
				reportError("%s calls %s: call attribute has to be a SEQUENCE of a TUPLE(<name> : SEQUENCE)", 
						myRule.getName(), object.getSymbol().getName());
				return null;
			}
			return new RuleCallParser(new ValueStream(ruleCallValue), new IResolver() {

				@Override
				public void assignRule(RuleCall call, String name) {
					myRuleResolver.addReferrer(call, name);
				}

				@Override
				public void assignVariableReference(
						AssignableValueReferenceArgument arg, String id) {
					RuleCall ruleCall = myVarsToRuleCalls.get(id);
					if (ruleCall == null) {
						reportError("%s calls %s: Symbol reference not found: $%s", 
										myRule.getName(), object.getSymbol().getName(), id);
					}
					arg.setAssignableValue(ruleCall);
				}

				@Override
				public void assignParameterReference(
						ParameterReferenceArgument arg, String id) {
					Parameter parameter = myNamesToParameters.get(id);
					if (parameter == null) {
						reportError("%s calls %s: Parameter not found: %s", 
										myRule.getName(), object.getSymbol().getName(), id);
					}
					arg.setParameter(parameter);
				}

				@Override
				public SyntacticalRule getContainingRule() {
					return myRule;
				}

			}).call();
		}

	}
	
	private final GrammarSwitch<INull> myReferencedRulesCreator = new GrammarSwitch<INull>() {
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
			createRules(object.getSymbol(), null, false);
			return INull.NULL;
		}
		
		@Override
		public INull caseExpression(Expression object) {
			return INull.NULL;
		}
	};

	private final DeferredResolver<String, Rule, RuleCall> myRuleResolver = new DeferredResolver<String, Rule, RuleCall>(DeferredResolver.THROW_EXCEPTION) {

		@Override
		protected void assignSubject(RuleCall referrer, Rule subject) {
			referrer.setRule(subject);
		}
		
	};

	private final GrammarSwitch<ANTLRExpression> myTokenSwitchItemCreator = new GrammarSwitch<ANTLRExpression>() {
		@Override
		public ANTLRExpression caseLexicalDefinition(
				LexicalDefinition object) {
			return myRuleProductionMaker.doSwitch(object);
		}
		
		@Override
		public ANTLRExpression caseSymbolReference(SymbolReference object) {
			return myRuleProductionMaker.caseSymbolReference(object);
		}
		
		//TODO: Looks like defaultCase is needed here.  
	};
	private final RuleProductionMaker myRuleProductionMaker = new RuleProductionMaker();
	private final Map<Rule, BuilderFactory> myRuleOrigins = new HashMap<Rule, BuilderFactory>();
	// Visited Symbols (for a DFS aimed to avoid generating rules for unused symbols)
	private final Set<Symbol> myVisited = EMFProxyUtil.customHashSet();
	private final CustomLinkedHashMap<Symbol, BuilderFactory> mySymbolOrigins;
	
	private final Map<SyntacticalRule, Symbol> myRulePrototypes = new HashMap<SyntacticalRule, Symbol>();
	private final Map<Symbol, SyntacticalRule> myDefaultRules = EMFProxyUtil.customHashMap();
	private final Set<Rule> myForceRetention = new HashSet<Rule>();
	
	private final Grammar myGrammar;
	private final IMetadataProvider myMetadataProvider;
	private List<Rule> myRules;
	
	public GrammaticToANTLR(Grammar grammar, IMetadataProvider metadataProvider, CustomLinkedHashMap<Symbol, BuilderFactory> symbolOrigins) {
		myGrammar = grammar;
		myMetadataProvider = metadataProvider;
		mySymbolOrigins = symbolOrigins;
	}

	public static String getImplicitNameForGrammar(IMetadataStorage metadata) {
		String name = metadata.readId("name");
		if (name == null) {
			name = (metadata.readString("grammarFileName") + ".<unknown>").split("\\.")[0];
		}
		return name;
	}

	private static String getGrammarPackage(IMetadataStorage metadata) {
		String pack = metadata.readString("parserPackage");
		if (pack == null) {
			pack = metadata.readString("package");
		}
		return pack;
	}

	public ANTLRGrammar generate() {
		IMetadataStorage metadata = MetadataStorage.getMetadataStorage(myGrammar, myMetadataProvider);
		
		ANTLRGrammar antlrGrammar = AntlrFactory.eINSTANCE.createANTLRGrammar();
		
		String name = metadata.readId("parserName");
		if (name == null) {
			name = getImplicitNameForGrammar(metadata);
		}
		antlrGrammar.setName(name);
		antlrGrammar.setPackage(getGrammarPackage(metadata));

		myRules = antlrGrammar.getRules();
		
		processOptions(metadata, antlrGrammar);

		processImports(metadata, antlrGrammar);
		
		// Do not strip whitespace definitions away even when nobody references them
		// TODO: Create a special attribute for the symbol to be forced 
		//       to retention in the final grammar
		Set<Symbol> keySet = mySymbolOrigins.keySet();
		for (Symbol symbol : keySet) {
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			if (symbolMetadata.isPresent("whitespace")) {
				createRules(symbol, symbolMetadata, true);
			}
		}
		
		for (Symbol symbol : myGrammar.getSymbols()) {
			createRules(symbol, null, false);
		}
		
		for (Entry<SyntacticalRule, Symbol> proto : myRulePrototypes.entrySet()) {
			SyntacticalRule rule = proto.getKey();
			Symbol protoSymbol = proto.getValue();
			for (Production production : protoSymbol.getProductions()) {
				rule.getProductions().add(createProduction(new SyntacticalRuleProductionMaker(rule), production));
			}
		}
		
		myRuleResolver.resolveAll();
		
		removeUnusedRules();
		
		return antlrGrammar;
	}

	private void removeUnusedRules() {
		Set<Rule> startFrom = new HashSet<Rule>(myForceRetention);
		for (Rule rule : myRules) {
			if (myRuleOrigins.get(rule) == null) {
				startFrom.add(rule);
			}
		}
		
		Set<Rule> visited = new ANTLRGrammarTraverser().walk(startFrom);
		for (Iterator<Rule> i = myRules.iterator(); i.hasNext(); ) {
			Rule rule = i.next();
			if (!visited.contains(rule)) {
				i.remove();
			}
		}
	}

	private void processImports(IMetadataStorage metadata,
			ANTLRGrammar antlrGrammar) {
		List<Value> imports = metadata.readMulti("imports");
		if (imports != null) {
			new ImportParser(new ValueStream(imports)).imports(antlrGrammar.getImports());
		}
	}

	private void processOptions(IMetadataStorage metadata,
			ANTLRGrammar antlrGrammar) {
		IMetadataStorage options = metadata.readTuple("options");
		if (options != null) {
			for (String name : options.getAttributeNames()) {
				Option option = AntlrFactory.eINSTANCE.createOption();
				option.setName(name);
				option.setValue(options.readId(name));
				antlrGrammar.getOptions().add(option);
			}
		}
	}

	private void createRules(Symbol symbol, IMetadataStorage metadata, boolean forceRetention) {
		// This implements a recursive DFS on a graph of symbols		
		if (myVisited.contains(symbol)) {
			return;
		}
		myVisited.add(symbol);
		
		if (metadata == null) {
			metadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
		}
		
		if (metadata.isPresent("lexical")) {
			Rule rule = createLexicalRule(symbol, metadata);
			createReferencedSymbols(symbol);
			myRules.add(rule);
			registerRule(symbol, forceRetention, rule);
			return;
		}
		
		List<Value> rulesDesc = metadata.readMulti("rules");
		if (rulesDesc == null) {
			reportError("%s: no rules specified", symbol.getName());
		}
		Collection<SyntacticalRule> newRules = new RuleHeaderParser(new ValueStream(rulesDesc)).headers();  
		if (newRules.size() == 1) {
			SyntacticalRule defaultRule = newRules.iterator().next();
			myDefaultRules.put(symbol, defaultRule);
		}
		for (SyntacticalRule rule : newRules) {
			myRuleResolver.addSubject(rule.getName(), rule);
			registerRule(symbol, forceRetention, rule);
			myRulePrototypes.put(rule, symbol);
		}
		createReferencedSymbols(symbol);
		myRules.addAll(newRules);
	}

	private void registerRule(Symbol symbol, boolean forceRetention, Rule rule) {
		myRuleOrigins.put(rule, mySymbolOrigins.get(symbol));
		if (forceRetention) {
			myForceRetention.add(rule);
		}
	}

	private void createReferencedSymbols(Symbol symbol) {
		for (Production production : symbol.getProductions()) {
			myReferencedRulesCreator.doSwitch(production.getExpression());
		}
	}

	private Rule createLexicalRule(Symbol symbol, IMetadataStorage metadata) {
		LexicalRule rule = AntlrFactory.eINSTANCE.createLexicalRule();
		rule.setFragment(metadata.isPresent("fragment"));
		rule.setWhitespace(metadata.isPresent("whitespace"));
		rule.setName(symbol.getName());
		myRuleResolver.addSubject(rule.getName(), rule);
		for (Production production : symbol.getProductions()) {
			rule.getProductions().add(createProduction(myRuleProductionMaker, production));
		}
		return rule;
	}

	private ANTLRProduction createProduction(RuleProductionMaker ruleProductionMaker, Production production) {
		ANTLRProduction antlrProduction = AntlrFactory.eINSTANCE.createANTLRProduction();
		ANTLRExpression expression = ruleProductionMaker.doSwitch(production.getExpression());
		antlrProduction.setExpression(expression);
		return antlrProduction ;
	}
	
	private static void reportError(String format, Object... args) {
		// Look: if you want to replace this, you have to care about control flow:
		// the code which uses this method expects it to throw exception and break 
		// method execution
		throw new IllegalArgumentException(String.format(format, args));
	}
	
	public Map<Rule, BuilderFactory> getRuleOrigins() {
		return myRuleOrigins;
	}

	private ANTLRExpression createTokenSwitch(Alternative object,
			IMetadataStorage metadata) {
		final TokenSwitch tokenSwitch = AntlrFactory.eINSTANCE.createTokenSwitch();
		tokenSwitch.setVariableName(metadata.readId("name"));
		EList<Expression> expressions = object.getExpressions();
		for (Expression expression : expressions) {
			ANTLRExpression token = myTokenSwitchItemCreator.doSwitch(expression);
			if (token == null) {
				reportError("Illegal expression in token switch: %s", expression.toString());
			} else {
				tokenSwitch.getExpressions().add(token);
			}
		}
		return tokenSwitch;
	}

}
