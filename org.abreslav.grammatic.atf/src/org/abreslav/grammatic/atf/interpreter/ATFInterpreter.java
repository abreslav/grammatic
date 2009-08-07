package org.abreslav.grammatic.atf.interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.dataflow.DataFlowAnalyzer;
import org.abreslav.grammatic.atf.dataflow.GrammarControlFlowGraphBuilder;
import org.abreslav.grammatic.atf.dataflow.GrammarGraphBuilderTrace;
import org.abreslav.grammatic.atf.dataflow.IControlFlowVertexHandler;
import org.abreslav.grammatic.atf.parser.ATFModuleLoader;
import org.abreslav.grammatic.atf.parser.IATFParserImplementationFactory;
import org.abreslav.grammatic.atf.parser.ITypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.checker.ATFTypeChecker;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.parser.AspectDefinitionParser;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;

public class ATFInterpreter {

	public static final ATFInterpreter INSTANCE = new ATFInterpreter();
	
	private ATFInterpreter() {}
	
	public <T> Map<String, Grammar> loadATFGrammar(
			Collection<AspectApplication> aspectApplications,
			FileLocator fileLocator,
			ITypeSystemBuilder<T> typeSystemBuilder,
			IATFParserImplementationFactory parserImplementationFactory,
			MetadataAspect aspect,
			Map<SemanticModule, SemanticModuleDescriptor> descriptors)
			throws IOException, RecognitionException, FileNotFoundException, InterruptedException {
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		ParsingContext parsingContext = new ParsingContext(fileLocator, writableAspect, IGrammarLoadHandler.NONE);
		
		ATFModuleLoader moduleParser = new ATFModuleLoader(typeSystemBuilder, parserImplementationFactory, fileLocator);
		
		Map<String, Grammar> grammars = new HashMap<String, Grammar>();
		for (AspectApplication aspectApplication : aspectApplications) {
			String grammarName = aspectApplication.getGrammarName();
			Grammar grammar = parsingContext.loadGrammar(grammarName);
			grammars.put(grammarName, grammar);
			
			String aspectName = aspectApplication.getAspectName();
			AspectDefinition aspectDefinition;
			if (aspectName.endsWith(".atf")) {
				aspectDefinition = moduleParser.loadATFModule(aspectName);
			} else {
				aspectDefinition = AspectDefinitionParser.parseAspectDefinition(aspectName, fileLocator);
			}
			AspectDefinitionInterpreter.runDefinition(
					aspectDefinition, grammar, IMetadataProvider.EMPTY, 
					writableAspect, IErrorHandler.EXCEPTION);
		}
		
		parsingContext.handleUnresolvedKeys();
		
		applyATFSecifications(new HashSet<Grammar>(grammars.values()), aspect, typeSystemBuilder);
		
		descriptors.putAll(moduleParser.getSemanticModuleDescriptors());
		
		return grammars;
	}


	private <T> void applyATFSecifications(
			Set<Grammar> grammars, MetadataAspect aspect,
			ITypeSystemBuilder<T> typeSystemBuilder) {
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);

		MetadataProvider metadataProvider = new MetadataProvider(aspect);
		for (Grammar grammar : grammars) {
			new ATFPostProcessor<RuntimeException>().process(
					grammar, 
					typeSystemBuilder.getStringType(), 
					metadataProvider, 
					writableAspect, IErrorHandler.EXCEPTION);
		}
		
		final GrammarGraphBuilderTrace trace = new GrammarGraphBuilderTrace();
		ControlFlowGraph controlFlowGraph = buildControlFlowGraph(grammars,	trace);
		
		DataFlowAnalyzer.completeAnalysis(
				controlFlowGraph, trace, metadataProvider, 
				IErrorHandler.EXCEPTION, new IErrorHandler<RuntimeException>() {

			@Override
			public void reportError(String messageTemplate, Object... objects)
					throws RuntimeException {
				System.err.format(messageTemplate + "\n", objects);
				System.err.flush();
			}
			
		});
		
		ITypeSystem<T> typeSystem = typeSystemBuilder.getTypeSystem(ITypeErrorHandler.ERROR_HANDLER);
		
		ATFTypeChecker.checkAndInferTypes(
				grammars, 
				new MetadataProvider(aspect), 
				typeSystem, 
				IErrorHandler.EXCEPTION);
	}

	private ControlFlowGraph buildControlFlowGraph(Set<Grammar> grammars,
			final GrammarGraphBuilderTrace trace) {
		//		final Set<BodyVertex> fakeVertices = new HashSet<BodyVertex>();
		ControlFlowGraph controlFlowGraph = GrammarControlFlowGraphBuilder.buildControlFlowGraph(
				grammars, 
				new IControlFlowVertexHandler() {

					@Override
					public void expressionFirstVertex(Expression expression,
							BodyVertex vertex) {
						trace.prependBeforeExpression(vertex, expression);
					}

					@Override
					public void expressionLastVertex(Expression expression,
							BodyVertex vertex) {
						trace.appendAfterExpression(vertex, expression);
					}

					@Override
					public void productionFirstVertex(Production production,
							BodyVertex vertex) {
						trace.setProductionFirstVertex(production, vertex);
					}

					@Override
					public void productionLastVertex(Production production,
							BodyVertex vertex) {
						trace.setProductionLastVertex(production, vertex);
					}

					@Override
					public void symbolEndVertex(Symbol symbol, EndVertex vertex) {
						trace.setEndVertex(vertex, symbol);
					}

					@Override
					public void symbolStartVertex(Symbol symbol,
							StartVertex vertex) {
						trace.setStartVertex(vertex, symbol);
					}
					
					@Override
					public void fakeVertex(BodyVertex vertex) {
//						fakeVertices.add(vertex);
					}
					
				});
		return controlFlowGraph;
	}

}
