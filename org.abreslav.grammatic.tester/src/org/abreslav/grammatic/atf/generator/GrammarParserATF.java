package org.abreslav.grammatic.atf.generator;

import java.io.IOException;
import java.io.InputStream;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.template.parser.IGrammarParser;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar1.GrammaticGrammarTemplateLexer;
import org.abreslav.grammatic.grammar1.GrammaticGrammarTemplateParser;
import org.abreslav.grammatic.grammar1.IImportsModuleImplementationProvider;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class GrammarParserATF implements IGrammarParser {
	
	public static final GrammarParserATF INSTANCE = new GrammarParserATF();
	
	private GrammarParserATF() {}

	@Override
	public Grammar parseGrammarWithGivenContext(String fileName,
			InputStream file, IParsingContext parsingContext)
			throws IOException, RecognitionException {
		GrammaticGrammarTemplateParser parser = configureParser(fileName, file,
				parsingContext);
		return parser.grammar1();
	}

	@Override
	public void parseTemplatesWithGivenContext(String fileName,
			InputStream file, IParsingContext parsingContext)
			throws IOException, RecognitionException {
		GrammaticGrammarTemplateParser parser = configureParser(fileName, file,
				parsingContext);
		parser.templateLibrary();
	}

	private GrammaticGrammarTemplateParser configureParser(String fileName,
			InputStream file, IParsingContext parsingContext)
			throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(file);
		GrammaticGrammarTemplateLexer tokenSource = new GrammaticGrammarTemplateLexer(input);
		GrammaticGrammarTemplateParser parser = new GrammaticGrammarTemplateParser(new CommonTokenStream(tokenSource));
		GrammaticGrammarTemplateModuleImplementationProvider temp = 
				new GrammaticGrammarTemplateModuleImplementationProvider(fileName, parsingContext);
		IImportsModuleImplementationProvider imp = new ImportsModuleImplementationProvider(parsingContext, temp.getErrorHandler());
		parser.setModuleImplementations(
				new GrammaticCharacterModuleImplementationProvider(),
				temp,
				new GrammaticLexicalGrammarModuleImplementationProvider(), 
				new GrammaticMetadataModuleImplementationProvider(parsingContext),
				new BooleanModule(),
				new StringModule(),
				imp);
		return parser;
	}

}
