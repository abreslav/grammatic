package org.abreslav.grammatic.atf.java.parser;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.parser.ANTLRParserImplementationFactory;
import org.abreslav.grammatic.atf.parser.ATFBuilders;
import org.abreslav.grammatic.atf.parser.IATFParserImplementation;
import org.abreslav.grammatic.grammar.template.parser.GrammaticCharacterBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticGrammarTemplateBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticLexicalGrammarBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticMetadataBuilders;
import org.abreslav.grammatic.grammar.template.parser.IImportsBuilders;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.query.parser.GrammaticQueryBuilders;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;

public class ATFJavaParserImplementationFactory extends ANTLRParserImplementationFactory<ATFJavaParser> {

	private final JavaTypeSystemBuilder myTypeSystemBuilder;
	
	public ATFJavaParserImplementationFactory(
			JavaTypeSystemBuilder typeSystemBuilder) {
		myTypeSystemBuilder = typeSystemBuilder;
	}

	@Override
	protected Lexer createLexer(ANTLRInputStream inputStream) {
		return new ATFJavaLexer(inputStream);
	}

	@Override
	protected ATFJavaParser createParser(CommonTokenStream commonTokenStream) {
		return new ATFJavaParser(commonTokenStream);
	}

	@Override
	protected IATFParserImplementation createParserImplementation(
			ATFJavaParser parser) {
		return new ATFJavaParserImpl(parser);
	}

	@Override
	protected void setBuilders(ATFJavaParser parser, ATFBuilders atfBuilders,
			GrammaticCharacterBuilders grammaticCharacterBuilders,
			GrammaticGrammarTemplateBuilders grammaticGrammarTemplateBuilders,
			GrammaticLexicalGrammarBuilders grammaticLexicalGrammarBuilders,
			GrammaticMetadataBuilders grammaticMetadataBuilders,
			GrammaticQueryBuilders grammaticQueryBuilders,
			IImportsBuilders importsBuilders) {
		parser.setBuilders(
				atfBuilders, 
				grammaticCharacterBuilders, 
				grammaticGrammarTemplateBuilders, 
				grammaticLexicalGrammarBuilders, 
				grammaticMetadataBuilders, 
				grammaticQueryBuilders, 
				importsBuilders, 
				new JavaTypesBuilders(myTypeSystemBuilder));
	}

	private static final class ATFJavaParserImpl extends ANTLRParserImplementation<ATFJavaParser> {
		private ATFJavaParserImpl(ATFJavaParser parser) {
			super(parser);
		}
		
		@Override
		public AspectDefinition atfModule() throws RecognitionException {
			return myParser.atfModule();
		}
		
		@Override
		public SemanticModule semanticModule() throws RecognitionException {
			return myParser.semanticModule();
		}
		
		@Override
		public void typeSystemModule() throws RecognitionException {
			myParser.typeSystemModule();
		}
	}
	
}
