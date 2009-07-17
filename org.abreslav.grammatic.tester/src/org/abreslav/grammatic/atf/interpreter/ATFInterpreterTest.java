package org.abreslav.grammatic.atf.interpreter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.dataflow.DataFlowAnalyzer;
import org.abreslav.grammatic.atf.dataflow.GrammarControlFlowGraphBuilder;
import org.abreslav.grammatic.atf.dataflow.GrammarGraphBuilderTrace;
import org.abreslav.grammatic.atf.dataflow.IControlFlowVertexHandler;
import org.abreslav.grammatic.atf.java.parser.ATFJavaParserImplementationFactory;
import org.abreslav.grammatic.atf.java.parser.JavaLangPackage;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.ATFModuleLoader;
import org.abreslav.grammatic.atf.parser.IATFParserImplementationFactory;
import org.abreslav.grammatic.atf.parser.ITypeSystemBuilder;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.checker.ATFTypeChecker;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystem;
import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.graph.DotPrinter;
import org.abreslav.grammatic.graph.IVertexNameProvider;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.util.EcoreValidator;
import org.junit.Test;


public class ATFInterpreterTest {

	private static final String DATA_DIR = "testData/atf/interpreter";
	
//	@Test
	public void testSubtyping() throws Exception {
		JavaTypeSystemBuilder builder = new JavaTypeSystemBuilder();
		EGenericType _char = JavaTypeSystemBuilder.createGenericType(JavaLangPackage.getPredefinedType("char"));
		builder.addType(_char);
		EGenericType _object = JavaTypeSystemBuilder.createGenericType(JavaLangPackage.getClassByShortName("Object"));
		builder.addType(_object);
		ITypeSystem<EGenericType> typeSystem = builder.getTypeSystem(ITypeErrorHandler.ERROR_HANDLER);
		ISubtypingRelation<EGenericType> subtypingRelation = typeSystem.getSubtypingRelation();
		assertTrue(subtypingRelation.isSubtypeOf(_char, _object));
		assertFalse(subtypingRelation.isSubtypeOf(_object, _char));
		boolean bounded = EcoreValidator.isBounded(_char, _object, null);
		assertTrue(bounded);
		bounded = EcoreValidator.isBounded(_object, _char, null);
		assertFalse(bounded);
	}
	
	@Test
	public void javaTest() throws Exception {
		JavaTypeSystemBuilder typeSystemBuilder = new JavaTypeSystemBuilder();
		IATFParserImplementationFactory parserImplementationFactory = new ATFJavaParserImplementationFactory(typeSystemBuilder);
		performTest(new File(DATA_DIR + "/java"), typeSystemBuilder,
				parserImplementationFactory);
	}
	
	@Test
	public void simpleTest() throws Exception {
		ITypeSystemBuilder<String> typeSystemBuilder = new TestTypeSystemBuilder();
		IATFParserImplementationFactory parserImplementationFactory = new TestFactory();
		performTest(new File(DATA_DIR), typeSystemBuilder,
				parserImplementationFactory);
	}

	@SuppressWarnings("deprecation")
	private <T> void performTest(File dataDir,
			ITypeSystemBuilder<T> typeSystemBuilder,
			IATFParserImplementationFactory parserImplementationFactory)
			throws IOException, RecognitionException, FileNotFoundException, InterruptedException {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		FileLocator fileLocator = new FileLocator(dataDir);
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		ParsingContext parsingContext = new ParsingContext(fileLocator, writableAspect, IGrammarLoadHandler.NONE);
		ATFModuleLoader moduleParser = new ATFModuleLoader(typeSystemBuilder, parserImplementationFactory, fileLocator);
		Map<Grammar, AspectDefinition> grammarToATF = new LinkedHashMap<Grammar, AspectDefinition>();
		
		File[] listFiles = dataDir.listFiles();
		for (File file : listFiles) {
			String name = file.getName();
			if (name.endsWith(".atf")) {
				String grammarName = name.replace(".atf", ".grammar");
				Grammar grammar = parsingContext.loadGrammar(grammarName);
				AspectDefinition atfSpecification = moduleParser.loadATFModule(name);
				grammarToATF.put(grammar, atfSpecification);
			}
		}
		parsingContext.handleUnresolvedKeys();
		
		for (Grammar grammar : grammarToATF.keySet()) {
			AspectDefinition atf = grammarToATF.get(grammar);
			AspectDefinitionInterpreter.runDefinition(
					atf, grammar, IMetadataProvider.EMPTY, 
					writableAspect, IErrorHandler.EXCEPTION);
		}
		MetadataProvider metadataProvider = new MetadataProvider(aspect);
		for (Grammar grammar : grammarToATF.keySet()) {
			new ATFPostProcessor<RuntimeException>().process(
					grammar, typeSystemBuilder.getStringType(), metadataProvider, writableAspect, IErrorHandler.EXCEPTION);
		}
		final GrammarGraphBuilderTrace trace = new GrammarGraphBuilderTrace();
		final Set<BodyVertex> fakeVertices = new HashSet<BodyVertex>();
		ControlFlowGraph controlFlowGraph = GrammarControlFlowGraphBuilder.buildControlFlowGraph(
				grammarToATF.keySet(), 
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
						fakeVertices.add(vertex);
					}
					
				});
		
		DotPrinter.printDot(controlFlowGraph, new PrintStream(new FileOutputStream(new File(dataDir, "graph.dot"))), new IVertexNameProvider() {

			@Override
			public String getVertexName(ControlFlowVertex vertex) {
				if (fakeVertices.contains(vertex)) {
					return "fake" + System.identityHashCode(vertex);
				}
				if (vertex instanceof StartVertex) {
					return trace.getSymbol(vertex).getName();
				}
				return IVertexNameProvider.DEFAULT.getVertexName(vertex);
			}
			
		});
		
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
		String dotFile = dataDir + "/types.dot";
		((FiniteTypeSystem<?>) typeSystem).__dump(dotFile);
		String outFile = dataDir + "/types.png";
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("dot -Tpng -o " + outFile + " " + dotFile);
		ATFTypeChecker.checkAndInferTypes(
				grammarToATF.keySet(), 
				new MetadataProvider(aspect), 
				typeSystem, 
				IErrorHandler.EXCEPTION);
	}

	
	
//	String[] allTypes = new String[] {
//	"int",
//	"Object",
//	"String",
//	"char",
//	"TemplateBody<? extends Expression>",
//	"TemplateBody<CharacterRange>",
//	"SetComplementBuilder",
//	"EObject",
//	"TupleValue",
//	"Namespace",
//	"Annotated",
//	"Value",
//	"PunctuationType",
//	"RenamingManager",
//	"Grammar",
//	"NULL"
//};
//
}
