package org.abreslav.grammatic.atf.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.abreslav.grammatic.grammar.template.parser.GrammaticCharacterBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticGrammarTemplateBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticLexicalGrammarBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticMetadataBuilders;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.IImportsBuilders;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.query.parser.GrammaticQueryBuilders;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import org.antlr.runtime.Token;

public abstract class ANTLRParserImplementationFactory<P extends Parser> implements IATFParserImplementationFactory {

	protected static abstract class ANTLRParserImplementation<P extends Parser> implements IATFParserImplementation {

		protected final P myParser;
		
		public ANTLRParserImplementation(P parser) {
			myParser = parser;
		}

		@Override
		public String getPositionString() {
			Token lt = myParser.getTokenStream().LT(0);
			if (lt == null) {
				lt = myParser.getTokenStream().LT(-1);
			}
			int line = lt.getLine();
			int offset = lt.getCharPositionInLine();
			String position = line + ":" + offset;
			return position;
		}
		
	}
	
	@Override
	public IATFParserImplementation createParserImplementation(
			IATFParser parserFrontEnd, 
			File file, 
			ITypeSystemBuilder<?> typeSystemBuilder, 
			FileLocator fileLocator, IErrorHandler<RuntimeException> errorHandler) throws IOException {
		Lexer lexer = createLexer(new ANTLRInputStream(new FileInputStream(file)));
		P parser = createParser(new CommonTokenStream(lexer));
		ATFBuilders atfBuilders = new ATFBuilders(parserFrontEnd, typeSystemBuilder, errorHandler);
		IParsingContext parsingContext = new ParsingContext(
				fileLocator, 
				IWritableAspect.ERROR, // Specifying metadata for attribute values is not supported 
				IGrammarLoadHandler.NONE);
		GrammaticGrammarTemplateBuilders grammaticGrammarTemplateBuilders = new GrammaticGrammarTemplateBuilders("<inside aspect>", parsingContext);
		setBuilders(
				parser,
				atfBuilders,
				new GrammaticCharacterBuilders(),
				grammaticGrammarTemplateBuilders,
				new GrammaticLexicalGrammarBuilders(),
				new GrammaticMetadataBuilders(parsingContext),
				new GrammaticQueryBuilders(),
				atfBuilders.getImportsBuilders()
		);
		return createParserImplementation(parser);
	}

	protected abstract IATFParserImplementation createParserImplementation(P parser);

	protected abstract void setBuilders(P parser, ATFBuilders atfBuilders,
			GrammaticCharacterBuilders grammaticCharacterBuilders,
			GrammaticGrammarTemplateBuilders grammaticGrammarTemplateBuilders,
			GrammaticLexicalGrammarBuilders grammaticLexicalGrammarBuilders,
			GrammaticMetadataBuilders grammaticMetadataBuilders,
			GrammaticQueryBuilders grammaticQueryBuilders,
			IImportsBuilders importsBuilders);

	protected abstract P createParser(CommonTokenStream commonTokenStream);

	protected abstract Lexer createLexer(ANTLRInputStream inputStream);

}
