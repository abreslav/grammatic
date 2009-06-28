package org.abreslav.grammatic.metadata.aspectdef.parser;

import java.io.IOException;
import java.io.InputStream;

import org.abreslav.grammatic.grammar.template.parser.GrammaticCharacterBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticGrammarTemplateBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticLexicalGrammarBuilders;
import org.abreslav.grammatic.grammar.template.parser.GrammaticMetadataBuilders;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.query.parser.GrammaticQueryBuilders;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;


public class AspectDefinitionParser {

	public static AspectDefinition parseAspectDefinition(InputStream input, FileLocator fileLocator) throws IOException, RecognitionException {
		ANTLRInputStream in = new ANTLRInputStream(input);
		GrammaticMetadataAspectsLexer lexer = new GrammaticMetadataAspectsLexer(in);
		CommonTokenStream stream = new CommonTokenStream(lexer);
		GrammaticMetadataAspectsParser parser = new GrammaticMetadataAspectsParser(stream);
		
		GrammaticQueryBuilders grammaticQueryBuilders = new GrammaticQueryBuilders();
		GrammaticMetadataAspectsBuilders grammaticMetadataAspectBuilders = new GrammaticMetadataAspectsBuilders(grammaticQueryBuilders);
		
		IParsingContext parsingContext = new ParsingContext(
				fileLocator, 
				IWritableAspect.ERROR, // Specifying metadata for attribute values is not supported 
				IGrammarLoadHandler.NONE);
		GrammaticGrammarTemplateBuilders grammaticGrammarTemplateBuilders = new GrammaticGrammarTemplateBuilders("<inside aspect>", parsingContext);
		parser.setBuilders(
				new GrammaticCharacterBuilders(), 
				grammaticGrammarTemplateBuilders,
				new GrammaticLexicalGrammarBuilders(),
				grammaticMetadataAspectBuilders, 
				new GrammaticMetadataBuilders(parsingContext), 
				grammaticQueryBuilders,
				grammaticGrammarTemplateBuilders.getImportsBuilders()
		);
		
		AspectDefinition aspect = parser.aspect();
		
		return aspect;
	}

}