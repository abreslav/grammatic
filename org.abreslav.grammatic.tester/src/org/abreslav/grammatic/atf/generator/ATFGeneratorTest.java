package org.abreslav.grammatic.atf.generator;

import java.io.File;
import java.util.Collection;

import org.abreslav.grammatic.atf.interpreter.AspectApplication;
import org.abreslav.grammatic.atf.java.antlr.generator.ATFGeneratorFrontend;
import org.abreslav.grammatic.utils.FileLocator;
import org.junit.Test;

public class ATFGeneratorTest {

	@Test
	public void test() throws Exception {
		String sourceDir = "testData/atf/interpreter/java";
		String targetDir = "generated/";
		String packageDir = "org/abreslav/grammatic/grammar1/";
		Collection<AspectApplication> applications = AspectApplication.createApplicationsFromDirectory(new File(sourceDir));
		FileLocator fileLocator = new FileLocator(new File(sourceDir));
		ATFGeneratorFrontend.INSTANCE.generate(fileLocator, targetDir, packageDir, "GrammaticMetadata", applications);
		ATFGeneratorFrontend.INSTANCE.generate(fileLocator, targetDir, packageDir, "GrammaticLexicalGrammar", applications);
		ATFGeneratorFrontend.INSTANCE.generate(fileLocator, targetDir, packageDir, "GrammaticQuery", applications);
		ATFGeneratorFrontend.INSTANCE.generate(fileLocator, targetDir, packageDir, "GrammaticGrammarTemplate", applications);
	}

}
