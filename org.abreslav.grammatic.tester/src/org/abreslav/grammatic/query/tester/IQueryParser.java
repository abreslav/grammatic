package org.abreslav.grammatic.query.tester;

import java.io.IOException;
import java.util.List;

import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.RuleQuery;
import org.antlr.runtime.RecognitionException;

public interface IQueryParser {

	QueryContainer<RuleQuery> parseRuleQuery(String input) throws IOException,
			RecognitionException;

	ExpressionQuery parseExpressionQuery(String input) throws IOException,
			RecognitionException;

	List<AttributeQuery> parseAttributeQuery(String input) throws IOException,
			RecognitionException;

}