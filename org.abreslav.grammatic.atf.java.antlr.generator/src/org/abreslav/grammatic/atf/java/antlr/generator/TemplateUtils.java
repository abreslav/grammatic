package org.abreslav.grammatic.atf.java.antlr.generator;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.stringtemplate.StringTemplateGroup;

public class TemplateUtils {
	public static StringTemplateGroup loadTemplateGroup(String groupName) {
		InputStream groupStream = TemplateUtils.class.getClassLoader().getResourceAsStream("templates/" + groupName + ".tg");
		StringTemplateGroup group = new StringTemplateGroup(new InputStreamReader(groupStream));
		return group;
	}
}
