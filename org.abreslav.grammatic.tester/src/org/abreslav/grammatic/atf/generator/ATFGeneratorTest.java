package org.abreslav.grammatic.atf.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.abreslav.grammatic.atf.interpreter.ATFInterpreter;
import org.abreslav.grammatic.atf.interpreter.ATFInterpreterTest;
import org.abreslav.grammatic.atf.java.antlr.generator.ATFToANTLR;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.parser.ATFJavaParserImplementationFactory;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.junit.Assert;
import org.junit.Test;

public class ATFGeneratorTest {

	@Test
	public void test() throws Exception {
		JavaTypeSystemBuilder typeSystemBuilder = new JavaTypeSystemBuilder();
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		
		File baseDir = new File("testData/atf/interpreter/java");
		Map<String, Grammar> grammars = ATFInterpreter.INSTANCE.loadATFGrammar(
				ATFInterpreterTest.createApplicationsFromDirectory(baseDir),
				new FileLocator(baseDir), 
				typeSystemBuilder, 
				new ATFJavaParserImplementationFactory(typeSystemBuilder),
				aspect);
		
		String frontGrammarName = "GrammaticLexicalGrammar.grammar";
		Grammar frontGrammar = grammars.get(frontGrammarName);
		if (frontGrammar == null) {
			Assert.fail("No front grammar found");
		}
		HashSet<Grammar> usedGrammars = new HashSet<Grammar>(grammars.values());
		usedGrammars.remove(frontGrammar);
		ATFToANTLR.generate(
				frontGrammar, 
				usedGrammars, 
				ATFToANTLR.USED_FORCED_FRONT_WHITESPACE, 
				new MetadataProvider(aspect), 
				new ArrayList<ModuleImplementationProvider>(), 
				new ArrayList<ModuleImplementation>());
	}


}
