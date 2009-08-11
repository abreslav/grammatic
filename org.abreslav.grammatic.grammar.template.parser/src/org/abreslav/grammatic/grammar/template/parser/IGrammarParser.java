package org.abreslav.grammatic.grammar.template.parser;

import java.io.IOException;
import java.io.InputStream;

import org.abreslav.grammatic.grammar.Grammar;
import org.antlr.runtime.RecognitionException;

public interface IGrammarParser {

	Grammar parseGrammarWithGivenContext(String fileName, InputStream file,
			IParsingContext parsingContext) throws IOException, RecognitionException;

	void parseTemplatesWithGivenContext(String fileName, InputStream file,
			IParsingContext parsingContext) throws IOException, RecognitionException;

}
