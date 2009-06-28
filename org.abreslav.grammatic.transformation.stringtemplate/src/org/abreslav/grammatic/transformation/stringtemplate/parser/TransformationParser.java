//package org.abreslav.grammatic.transformation.stringtemplate.parser;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.abreslav.grammatic.query.Query;
//import org.abreslav.grammatic.query.QueryContainer;
//import org.abreslav.grammatic.query.parser.GrammaticQueryLexer;
//import org.abreslav.grammatic.query.parser.RuleQueryParser;
//import org.antlr.runtime.ANTLRInputStream;
//import org.antlr.runtime.CommonTokenStream;
//import org.antlr.runtime.RecognitionException;
//import org.antlr.stringtemplate.StringTemplate;
//
//public class TransformationParser {
//	public static Map<QueryContainer<? extends Query>, StringTemplate> parseTransformation(
//			FileInputStream input) throws IOException {
//		ANTLRInputStream in = new ANTLRInputStream(input);
//		
//		GrammaticQueryLexer lexer = new GrammaticQueryLexer(in);
//		CommonTokenStream stream = new CommonTokenStream(lexer);
//
//		Map<QueryContainer<? extends Query>, StringTemplate> queryToTemplate = new HashMap<QueryContainer<? extends Query>, StringTemplate>();
//		while (true) {
//			try {
//				QueryContainer<? extends Query> parseRule = RuleQueryParser.parseRuleFromCommonTokenStream(stream);
//				
//				GrammaticStringTemplateParser parser = new GrammaticStringTemplateParser(stream) {
//					@Override
//					public void reportError(RecognitionException e) {
//						throw new IllegalArgumentException(e);
//					}
//				};
//				
//				String template = parser.template();
//				
//				queryToTemplate.put(parseRule, new StringTemplate(template));
//			} catch (IllegalArgumentException e) {
//				break;
//			} catch (RecognitionException e) {
//				break;
//			}
//		}
//		return queryToTemplate;
//	}
//
//}
