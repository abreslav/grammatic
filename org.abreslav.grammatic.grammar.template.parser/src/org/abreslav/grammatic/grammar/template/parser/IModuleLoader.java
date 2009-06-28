package org.abreslav.grammatic.grammar.template.parser;

import java.io.IOException;

import org.abreslav.grammatic.grammar.Grammar;
import org.antlr.runtime.RecognitionException;

public interface IModuleLoader {

	void loadTemplates(String fileName) throws IOException, RecognitionException;
	Grammar loadGrammar(String fileName) throws IOException, RecognitionException;
	
}
