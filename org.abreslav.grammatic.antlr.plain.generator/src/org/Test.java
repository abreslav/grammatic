package org;

import org.abreslav.grammatic.antlr.generator.frontend.AspectApplication;
import org.abreslav.grammatic.antlr.generator.frontend.GrammarGeneratorTask;

public class Test {

	public static void main(String[] args) {
		String project = "/media/data/workspaces/default/aTest/";
		GrammarGeneratorTask task = new GrammarGeneratorTask();
		task.setGeneratedGrammarDir(project + "generated-grammar/");
		task.setGrammarDir(project + "grammar/");
		task.setGrammarName("GrammaticQuery");
		task.setTargetPackageDir(project + "src");
		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticGrammarTemplate"));
		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticCharacter"));
		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticLexicalGrammar"));
		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticMetadata"));
		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticQuery"));
//		task.addConfiguredAspectApplication(makeAspectApplication("GrammaticMetadataAspects"));
		task.execute();
	}

	private static AspectApplication makeAspectApplication(String string) {
		AspectApplication result = new AspectApplication();
		result.setAspectName(string);
		result.setGrammarName(string);
		return result;
	}
}
