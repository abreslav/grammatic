package org.abreslav.grammatic.antlr.generator.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.abreslav.grammatic.antlr.generator.frontend.ANTLRGrammarGenerator;
import org.antlr.stringtemplate.StringTemplateGroup;

public class TemplateUtils {
	public static StringTemplateGroup loadTemplateGroup(String groupName) {
		InputStream groupStream = ANTLRGrammarGenerator.class.getClassLoader().getResourceAsStream("templates/" + groupName + ".tg");
		StringTemplateGroup group = new StringTemplateGroup(new InputStreamReader(groupStream));
		return group;
	}
}
