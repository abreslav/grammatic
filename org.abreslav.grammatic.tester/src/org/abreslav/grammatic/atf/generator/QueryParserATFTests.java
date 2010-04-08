package org.abreslav.grammatic.atf.generator;

import java.io.IOException;


import matcher_test.MatcherTest;

import org.abreslav.grammatic.query.tester.AttributeMatcherTest;
import org.abreslav.grammatic.query.tester.ExpressionMatcherTest;
import org.abreslav.grammatic.query.tester.IQueryParser;
import org.abreslav.grammatic.query.tester.RuleMatcherTest;
import org.abreslav.grammatic.query.tester.VariableMatcherTest;
import org.antlr.runtime.RecognitionException;

public class QueryParserATFTests {

	public static class AttributeMatcherTestATF extends AttributeMatcherTest {
	
		public AttributeMatcherTestATF(String expression, String queries,
				boolean result) {
			super(expression, queries, result);
		}
	
		@Override
		protected IQueryParser getQueryParser() {
			return QueryParserATF.INSTANCE;
		}
	}

	public static class RuleMatcherTestATF extends RuleMatcherTest {

		public RuleMatcherTestATF(String testsName, MatcherTest test,
				MatcherTest result) {
			super(testsName, test, result);
		}

		@Override
		protected IQueryParser getQueryParser() {
			return QueryParserATF.INSTANCE;
		}
	}
	
	public static class ExpressionMatcherTestATF extends ExpressionMatcherTest {

		public ExpressionMatcherTestATF(String expression, String query,
				boolean result) {
			super(expression, query, result);
		}

		@Override
		protected IQueryParser getQueryParser() {
			return QueryParserATF.INSTANCE;
		}
	}
	
	public static class VariableMatcherTestATF extends VariableMatcherTest {

		public VariableMatcherTestATF(String expression, String query,
				boolean result, Object[][] vars) throws IOException,
				RecognitionException {
			super(expression, query, result, vars);
		}
		
		@Override
		protected IQueryParser getQueryParser() {
			return QueryParserATF.INSTANCE;
		}
	}
}
