package org.abreslav.grammatic.atf.java.antlr.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.interpreter.ATFInterpreter;
import org.abreslav.grammatic.atf.interpreter.AspectApplication;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr.ANTLRMetadata;
import org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr.ATFToANTLR;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.atf.java.parser.ATFJavaParserImplementationFactory;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.RecognitionException;

public class ATFGeneratorFrontend {
	
	public static ATFGeneratorFrontend INSTANCE = new ATFGeneratorFrontend();
	
	private ATFGeneratorFrontend() {}

	public void generate(String sourceDir, String targetDir,
			String packageDir, String grammarBaseName, Collection<AspectApplication> applications) throws IOException,
			RecognitionException, FileNotFoundException, InterruptedException {
		JavaTypeSystemBuilder typeSystemBuilder = new JavaTypeSystemBuilder();
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		
		Map<SemanticModule, SemanticModuleDescriptor> descriptors = new HashMap<SemanticModule, SemanticModuleDescriptor>();
		File baseDir = new File(sourceDir);
		Map<String, Grammar> grammars = ATFInterpreter.INSTANCE.loadATFGrammar(
				applications,
				new FileLocator(baseDir), 
				typeSystemBuilder, 
				new ATFJavaParserImplementationFactory(typeSystemBuilder),
				aspect,
				descriptors);
		
		String frontGrammarName = grammarBaseName + ".grammar";
		Grammar frontGrammar = grammars.get(frontGrammarName);
		if (frontGrammar == null) {
			throw new IllegalArgumentException("No front grammar found");
		}
		HashSet<Grammar> usedGrammars = new HashSet<Grammar>(grammars.values());
		generateCode(targetDir, packageDir, grammarBaseName, aspect,
				descriptors, frontGrammar, usedGrammars);
	}

	public void generateCode(String targetDir, String packageDir,
			String grammarBaseName, MetadataAspect aspect,
			Map<SemanticModule, SemanticModuleDescriptor> descriptors,
			Grammar frontGrammar, Set<Grammar> usedGrammars)
			throws FileNotFoundException, IOException {
		usedGrammars.remove(frontGrammar);
		List<ModuleImplementation> moduleImplementations = new ArrayList<ModuleImplementation>();
		List<ModuleImplementationProvider> moduleImplementationProviders = new ArrayList<ModuleImplementationProvider>();
		ANTLRGrammar generate = ATFToANTLR.generate(
				frontGrammar, 
				usedGrammars, 
				ANTLRMetadata.USED_FORCED_FRONT_WHITESPACE, 
				new MetadataProvider(aspect), 
				descriptors, 
				moduleImplementationProviders, 
				moduleImplementations);
		
		FileOutputStream fileOutputStream = new FileOutputStream(new File(targetDir + packageDir + grammarBaseName + ".g"));
		ANTLRGrammarPrinter.printGrammar(generate, new PrintStream(fileOutputStream));
		fileOutputStream.close();
		
		for (ModuleImplementation moduleImplementation : moduleImplementations) {
			if (moduleImplementation.eContainer() instanceof ModuleImplementationProvider) {
				continue;
			}
			FileWriter fos = getWriterForType(targetDir, moduleImplementation);
			ImplementationClassGenerator.generateModuleImplementationCode(fos, moduleImplementation);
			fos.close();
		}
		
		for (ModuleImplementationProvider provider : moduleImplementationProviders) {
			FileWriter fos = getWriterForType(targetDir, provider.getProviderInterface());
			ImplementationClassGenerator.generateImplementationProviderCode(fos, provider);
			fos.close();
			
			fos = getWriterForType(targetDir, provider.getPoolsClass());
			ImplementationClassGenerator.generatePoolsImplementationCode(fos, provider);
			fos.close();
		}
	}

	private FileWriter getWriterForType(String targetDir, Type type)
			throws IOException {
		String dirName = targetDir + type.getPackage().replace('.', '/');
		new File(dirName).mkdirs();
		String fileName = dirName + "/" + type.getName() + ".java";
		FileWriter fos = new FileWriter(fileName);
		return fos;
	}

}
