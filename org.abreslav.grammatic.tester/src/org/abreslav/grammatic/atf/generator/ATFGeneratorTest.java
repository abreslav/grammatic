package org.abreslav.grammatic.atf.generator;

import java.io.File;
import java.util.Collection;

import org.abreslav.grammatic.atf.interpreter.ATFInterpreterTest;
import org.abreslav.grammatic.atf.interpreter.AspectApplication;
import org.abreslav.grammatic.atf.java.antlr.generator.ATFGeneratorFrontend;
import org.junit.Test;

public class ATFGeneratorTest {

	@Test
	public void test() throws Exception {
		String sourceDir = "testData/atf/interpreter/java";
		String targetDir = "generated/";
		String packageDir = "org/abreslav/grammatic/grammar1/";
		Collection<AspectApplication> applications = ATFInterpreterTest.createApplicationsFromDirectory(new File(sourceDir));
		ATFGeneratorFrontend.INSTANCE.generate(sourceDir, targetDir, packageDir, "GrammaticMetadata", applications);
		ATFGeneratorFrontend.INSTANCE.generate(sourceDir, targetDir, packageDir, "GrammaticLexicalGrammar", applications);
		ATFGeneratorFrontend.INSTANCE.generate(sourceDir, targetDir, packageDir, "GrammaticQuery", applications);
		ATFGeneratorFrontend.INSTANCE.generate(sourceDir, targetDir, packageDir, "GrammaticGrammarTemplate", applications);
	}

}
