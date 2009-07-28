package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.AntlrFactory;
import org.abreslav.grammatic.atf.java.antlr.LexicalRule;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.generator.ModuleImplementationBuilder;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.utils.INull;

public class InitialObjectCreator {

	// Private state
	private final Set<Symbol> myVisited = EMFProxyUtil.customHashSet();
	private final List<Rule> myTopologicalOrder = new LinkedList<Rule>();
	private final Set<Grammar> myVisitedGrammars = new HashSet<Grammar>(); 
	private final GrammarSwitch<INull> mySymbolReferenceTraverser = new SymbolReferenceTraverser() {
		public INull caseSymbolReference(SymbolReference object) {
			visit(object.getSymbol());
			return super.caseSymbolReference(object);
		};
	};

	// Input data
	private final ANTLRGrammar myResultGrammar;
	private final ATFToANTLRTrace myTrace;
	private final IMetadataProvider myMetadataProvider;
	private final SymbolMetadataProvider mySymbolMetadataProvider;
	
	public InitialObjectCreator(ANTLRGrammar resultGrammar,
			ATFToANTLRTrace trace, IMetadataProvider metadataProvider,
			SymbolMetadataProvider symbolMetadataProvider) {
		myResultGrammar = resultGrammar;
		myTrace = trace;
		myMetadataProvider = metadataProvider;
		mySymbolMetadataProvider = symbolMetadataProvider;
	}

	public Set<Grammar> process(Grammar frontGrammar, Collection<Grammar> usedGrammars, ISymbolInclusionStrategy inclusionStrategy) {
		myVisited.clear();
		myVisitedGrammars.clear();
		myTopologicalOrder.clear();
		processGramar(frontGrammar, true, inclusionStrategy);
		for (Grammar grammar : usedGrammars) {
			processGramar(grammar, false, inclusionStrategy);
		}
		Collections.reverse(myTopologicalOrder);
		myResultGrammar.getRules().addAll(myTopologicalOrder);
		return new HashSet<Grammar>(myVisitedGrammars);
	}
	
	private void processGramar(Grammar grammar,
			boolean front, ISymbolInclusionStrategy inclusionStrategy) {
		for (Symbol symbol : grammar.getSymbols()) {
			IMetadataStorage metadata = mySymbolMetadataProvider.getSymbolMetadata(symbol);
			if (!myVisited.contains(symbol) 
					&& inclusionStrategy.doInclude(symbol, front, metadata)) {
				visit(symbol);
			}
		}
	}
	
	void visit(Symbol symbol) {
		if (myVisited.contains(symbol)) {
			return;
		}
		myVisited.add(symbol);
		myVisitedGrammars.add(StructureUtils.getGrammar(symbol));
		
		mySymbolReferenceTraverser.doSwitch(symbol);

		processSymbol(symbol);
	}
	
	private void processSymbol(Symbol symbol) {
		IMetadataStorage metadata = mySymbolMetadataProvider.getSymbolMetadata(symbol);
		
		if (metadata.isPresent(ATFMetadata.TOKEN)) {
			createLexicalRuleStub(symbol, metadata);
			return;
		}
		
		ModuleImplementation symbolModuleImpl = createSemanticModuleImpl(metadata);
		myTrace.putSymbolToModuleImpl(symbol, symbolModuleImpl);

		Map<String, Namespace> functionNamespaces = metadata.readObject(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
		for (Namespace namespace : functionNamespaces.values()) {
			IMetadataProvider projection = myMetadataProvider.getProjection(namespace);
			IMetadataStorage projectedMetadata = MetadataStorage.getMetadataStorage(symbol, projection);
			FunctionSignature function = projectedMetadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);

			ModuleImplementation functionModuleImpl = createSemanticModuleImpl(projectedMetadata);
			myTrace.putFunctionToModuleImpl(function, functionModuleImpl);
			
			myTrace.putFunctionToNamespace(function, namespace);
			createSyntacticalRuleStub(symbol, function);
		}
	}

	private LexicalRule createLexicalRuleStub(Symbol symbol, IMetadataStorage metadata) {
		LexicalRule result = AntlrFactory.eINSTANCE.createLexicalRule();
		
		result.setName(symbol.getName().toUpperCase()); // TODO : Scope, To underscored upper case
			
		myTrace.putTokenToRule(symbol, result);
		myResultGrammar.getRules().add(result);
		return result;
	}

	private SyntacticalRule createSyntacticalRuleStub(Symbol symbol, FunctionSignature function) {
		SyntacticalRule result = AntlrFactory.eINSTANCE.createSyntacticalRule();

		result.setName(function.getName()); // TODO : Scope, To CamelCase
		
		myTrace.putFunctionToRule(function, result);
		myTrace.putSyntacticalRuleToSymbol(result, symbol);
		myTopologicalOrder.add(0, result);
		return result;
	}

	private ModuleImplementation createSemanticModuleImpl(
			IMetadataStorage metadata) {
		SemanticModule semanticModule = metadata.readEObject(ATFMetadata.SEMANTIC_MODULE);
		if (semanticModule != null) {
			// TODO : Interface name and package
			return ModuleImplementationBuilder.INSTANCE.buildModuleImplementation(semanticModule, myTrace);
		}
		return null;
	}
}

