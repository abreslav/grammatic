import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import org.abreslav.grammatic.astrans.ATFAspectGenerator;
import org.abreslav.grammatic.astrans.EcoreGenerator;
import org.abreslav.grammatic.astrans.SemanticsAspectGenerator;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.interpreter.ATFPostProcessor;
import org.abreslav.grammatic.atf.java.antlr.generator.ATFGeneratorFrontend;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.aspects.GrammarAspectInterpreter;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.parser.AspectDefinitionParser;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.ecore.EPackage;


public class Main {

	public static void main(String[] args) throws IOException, RecognitionException {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		FileLocator fileLocator = new FileLocator(new File("examples"));
		Grammar grammar = GrammarParserUtils.parseGrammar(
				"jess.as.grammar", 
				fileLocator, 
				writableAspect, 
				IGrammarLoadHandler.NONE);

		MetadataAspect semanticalAspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect semWritableAspect = AspectWriter.createWritableAspect(semanticalAspect);
		EcoreGenerator generator = EcoreGenerator.create(SemanticsAspectGenerator.create(semWritableAspect));
		EPackage ePackage = generator.generateEcore(grammar, new MetadataProvider(aspect));
		
		AspectDefinition aspectDefinition = AspectDefinitionParser.parseAspectDefinition("jess.as.aspect", fileLocator);
		GrammarAspectInterpreter.applyGrammarAspect(grammar, aspectDefinition);
//		AspectDefinitionInterpreter.runDefinition(aspectDefinition, grammar, IMetadataProvider.EMPTY, writableAspect, IErrorHandler.EXCEPTION);

		MetadataAspect atfAspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		generateATF(grammar, atfAspect, new MetadataProvider(semanticalAspect));
		
		ResourceLoader resourceLoader = new ResourceLoader(".");
		resourceLoader.save("examples/model/jess.as.ecore", ePackage);
		
		System.out.println("over");
	}

	private static void generateATF(Grammar grammar, MetadataAspect aspect, IMetadataProvider metadataProvider) throws FileNotFoundException, IOException {
		HashMap<SemanticModule, SemanticModuleDescriptor> moduleDescriptors = new HashMap<SemanticModule, SemanticModuleDescriptor>();
		ATFAspectGenerator.generate(grammar, metadataProvider, AspectWriter.createWritableAspect(aspect), moduleDescriptors);
		ATFPostProcessor<RuntimeException> postProcessor = new ATFPostProcessor<RuntimeException>();
		JavaTypeSystemBuilder typeSystemBuilder = new JavaTypeSystemBuilder();
		postProcessor.process(
				grammar, 
				typeSystemBuilder.getStringType(), 
				new MetadataProvider(aspect), 
				AspectWriter.createWritableAspect(aspect), 
				IErrorHandler.EXCEPTION);
		ATFGeneratorFrontend.INSTANCE.generateCode(
				"./", 
				"./", 
				"grammar", 
				aspect, 
				moduleDescriptors, 
				grammar, 
				Collections.<Grammar>emptySet());
	}
}
