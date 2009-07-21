package org.abreslav.grammatic.atf.java.antlr.generator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.AntlrFactory;
import org.abreslav.grammatic.atf.java.antlr.Rule;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.util.GrammarSwitch;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class ATFToANTLR {

	// Takes as input: Grammatic grammars annotated with ATF specification (ATF.metadataScheme)
	// Returns as output: ANTLR grammar model (antlr.ecore) with references to ModuleImplementations
	
	public interface ISymbolInclusionStrategy {
		boolean doInclude(Symbol symbol, boolean isFront, IMetadataStorage metadata);
	}
	
	private final IMetadataProvider myMetadataProvider;
	private final Map<Symbol, IMetadataStorage> mySymbolsMetadata = EMFProxyUtil.customHashMap();
	
	private ATFToANTLR(IMetadataProvider metadataProvider) {
		myMetadataProvider = metadataProvider;
	}

	public ANTLRGrammar generate(
			Grammar frontGrammar, 
			Collection<Grammar> usedGrammars, 
			ISymbolInclusionStrategy inclusionStrategy,
			Collection<ModuleImplementationProvider> moduleImplementationProviders) {
		
		ANTLRGrammar resultGrammar = AntlrFactory.eINSTANCE.createANTLRGrammar();
		
		Set<Symbol> symbols = new GrammarGraphTraverser().process(frontGrammar, usedGrammars, inclusionStrategy);
		// Store correspondence (symbol, function) -> rule
		
		HashSet<Grammar> allGrammars = new HashSet<Grammar>(usedGrammars);
		allGrammars.add(frontGrammar);
//		createAllModuleImplementationProviders(allGrammars, symbols, moduleImplementationProviders);
		
//		fillAllRules();
		
		return resultGrammar;
	}

	private void processSymbol(Symbol symbol) {
		IMetadataStorage metadata = getSymbolMetadata(symbol);
		// Create default semantic module (if not empty)
		processSemanticFunctions(symbol.getName(), metadata);
		// Obtain syntactical functions
		Map<String, Namespace> functionNamespaces = metadata.readObject(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
		for (Namespace namespace : functionNamespaces.values()) {
			IMetadataProvider projection = myMetadataProvider.getProjection(namespace);
			IMetadataStorage projectedMetadata = MetadataStorage.getMetadataStorage(symbol, projection);
			FunctionSignature function = projectedMetadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);

			processSemanticFunctions(function.getName(), projectedMetadata);
			
			Rule rule = createRuleStub(symbol, function);
		}
	}

	private Rule createRuleStub(Symbol symbol, FunctionSignature function) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	private IMetadataStorage getSymbolMetadata(Symbol symbol) {
		IMetadataStorage metadata = mySymbolsMetadata.get(symbol);
		if (metadata == null) {
			metadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			mySymbolsMetadata.put(symbol, metadata);
		}
		return metadata;		
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
}
