package org.abreslav.grammatic.atf.interpreter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.parser.ATFJavaParserImplementationFactory;
import org.abreslav.grammatic.atf.java.parser.JavaLangPackage;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.IATFParserImplementationFactory;
import org.abreslav.grammatic.atf.parser.ITypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.utils.FileLocator;
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

//	@SuppressWarnings("deprecation")
	private <T> void performTest(File baseDir,
			ITypeSystemBuilder<T> typeSystemBuilder,
			IATFParserImplementationFactory parserImplementationFactory)
			throws IOException, RecognitionException, FileNotFoundException, InterruptedException {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		
		ATFInterpreter.INSTANCE.loadATFGrammar(
				AspectApplication.createApplicationsFromDirectory(baseDir) ,
				new FileLocator(baseDir), 
				typeSystemBuilder, 
				parserImplementationFactory,
				aspect,
				new HashMap<SemanticModule, SemanticModuleDescriptor>());

//		DotPrinter.printDot(controlFlowGraph, new PrintStream(new FileOutputStream(new File(dataDir, "graph.dot"))), new IVertexNameProvider() {
//
//			@Override
//			public String getVertexName(ControlFlowVertex vertex) {
//				if (fakeVertices.contains(vertex)) {
//					return "fake" + System.identityHashCode(vertex);
//				}
//				if (vertex instanceof StartVertex) {
//					return trace.getSymbol(vertex).getName();
//				}
//				return IVertexNameProvider.DEFAULT.getVertexName(vertex);
//			}
//			
//		});
		
//		String dotFile = dataDir + "/types.dot";
//		((FiniteTypeSystem<?>) typeSystem).__dump(dotFile);
//		String outFile = dataDir + "/types.png";
//		Runtime runtime = Runtime.getRuntime();
//		runtime.exec("dot -Tpng -o " + outFile + " " + dotFile);
		
	}

}
