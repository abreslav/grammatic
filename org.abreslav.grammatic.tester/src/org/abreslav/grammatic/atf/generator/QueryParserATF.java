package org.abreslav.grammatic.atf.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.grammar1.GrammaticQueryLexer;
import org.abreslav.grammatic.grammar1.GrammaticQueryParser;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.EmptyQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.QueryFactory;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.tester.IQueryParser;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class QueryParserATF implements IQueryParser {

	public static IQueryParser INSTANCE = new QueryParserATF();
	
	public QueryContainer<RuleQuery> parseRuleQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		GrammaticQueryModuleImplementationProvider implementationProvider = configureParser(parser);
		
		QueryContainer<RuleQuery> container = parser.ruleQueryContainer();
		implementationProvider.resolveVariableReferences();
		
		return container;
	}

	public ExpressionQuery parseExpressionQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		GrammaticQueryModuleImplementationProvider implementationProvider = configureParser(parser);

		ExpressionQuery expression = parser.alternativeQuery();
		implementationProvider.resolveVariableReferences();
		
		return expression;
	}
	
	public List<AttributeQuery> parseAttributeQuery(String input) throws IOException, RecognitionException {
		GrammaticQueryParser parser = createParser(new ByteArrayInputStream(input.getBytes()));
		configureParser(parser);

		EmptyQuery query = QueryFactory.eINSTANCE.createEmptyQuery();
		parser.queryMetadata(query);
		
		return query.getAttributes();
	}
	
	private static GrammaticQueryModuleImplementationProvider configureParser(
			GrammaticQueryParser parser) {
		GrammaticQueryModuleImplementationProvider implementationProvider = new GrammaticQueryModuleImplementationProvider();
		ParsingContext parsingContext = new ParsingContext(GrammarParserATF.INSTANCE, new FileLocator(new File(".")), IWritableAspect.NONE, IGrammarLoadHandler.NONE);
		String fileName = "<file>";
		GrammaticGrammarTemplateModuleImplementationProvider temp = 
			new GrammaticGrammarTemplateModuleImplementationProvider(fileName , parsingContext);
		parser.setModuleImplementations(
				new GrammaticCharacterModuleImplementationProvider(),
				temp,
				new GrammaticLexicalGrammarModuleImplementationProvider(), 
				new GrammaticMetadataModuleImplementationProvider(parsingContext),
				implementationProvider,
				new BooleanModule(),
				new StringModule());
		return implementationProvider;
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
