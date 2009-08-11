package org.abreslav.grammatic.query.tester;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.GrammaticCharacterBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticGrammarTemplateBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticLexicalGrammarBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticMetadataBuilders;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.parser.GrammaticQueryBuilders;
import org.abreslav.grammatic.query.parser.GrammaticQueryLexer;
import org.abreslav.grammatic.query.parser.GrammaticQueryParser;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class QueryParser {

	public static QueryContainer<RuleQuery> parseRuleQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		GrammaticQueryBuilders grammaticQueryBuilders = configureParser(parser);
		
		QueryContainer<RuleQuery> container = parser.ruleQueryContainer();
		grammaticQueryBuilders.resolveVariableReferences();
		
		return container;
	}

	public static ExpressionQuery parseExpressionQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		GrammaticQueryBuilders grammaticQueryBuilders = configureParser(parser);

		ExpressionQuery expression = parser.alternativeQuery();
		grammaticQueryBuilders.resolveVariableReferences();
		
		return expression;
	}
	
	public static List<AttributeQuery> parseAttributeQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		configureParser(parser);

		EmptyQuery query = QueryFactory.eINSTANCE.createEmptyQuery();
		parser.queryMetadata(query);
		
		return query.getAttributes();
	}
	
	private static GrammaticQueryBuilders configureParser(
			GrammaticQueryParser parser) {
		GrammaticQueryBuilders grammaticQueryBuilders = new GrammaticQueryBuilders();
		IParsingContext parsingContext = new ParsingContext(GrammarParserUtils.GRAMMAR_PARSER, new FileLocator(new File(".")), IWritableAspect.NONE, IGrammarLoadHandler.NONE);
		parser.setBuilders(
				new GrammaticCharacterBuilders(), 
				new GrammaticGrammarTemplateBuilders("<test module>", parsingContext),
				new GrammaticLexicalGrammarBuilders(),
				new GrammaticMetadataBuilders(parsingContext), 
				grammaticQueryBuilders
		);
		return grammaticQueryBuilders;
	}

	private static GrammaticQueryParser createParser(InputStream input)
			throws IOException {
		ANTLRInputStream in = new ANTLRInputStream(input);
		GrammaticQueryLexer lexer = new GrammaticQueryLexer(in);
		CommonTokenStream stream = new CommonTokenStream(lexer);
		GrammaticQueryParser parser = new GrammaticQueryParser(stream);
		return parser;
	}

}