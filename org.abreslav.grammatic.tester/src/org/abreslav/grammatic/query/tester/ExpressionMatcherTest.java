package org.abreslav.grammatic.query.tester;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.GrammarParser;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.interpreter.ExpressionMatcher;
import org.abreslav.grammatic.query.interpreter.Matchers;
import org.abreslav.grammatic.query.interpreter.VariableValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExpressionMatcherTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
				{"a", "#", true},
				{"a b", "# #", true},
				{"a b c", "# #", false},
				{"a a", "# # #", false},
				
				{"a b c e f g h", "# # ..", true},
				{"a b", "# # ..", true},
				{"a", "# # ..", false},
				{"a a a a{a=a}", "# .. #{a}", true},
				{"a a a a", "# .. #{a}", false},
				{"a a a a{a}", ".. #{a}", true},
				{"a a a a{a} a", ".. #{a} #", true},
				{"a{a} a", ".. #{a} #", true},
				{"a{a} a", "..", true},
				{"a", "# ..", true},

				{"(a b){a=x}", "(..){a}", true},
				{"(a b){a=x}", "(..){!a}", false},

				{"#empty", "#empty", true},
				{"#empty", "#", false},
				{"a", "#empty", false},

				
				{"a", "#{!a}", true},
				
				{"a", "# | ...", true},
				{"a | a", "# | #", true},
				{"a | a{a}", "# | #{a}", true},
				{"a{a} | a", "#{!a} | #{a}", true},
				{"a{a} | a", "#{!a} | ... ", true},
				
				{"a | a", "# | # | #", false},
				{"a | a", "# | # | # | ...", false},
				{"a | a | a", "# | #", false},
				{"a | a", "#{!a} | #{a}", false},

				{"a a", "$a=# $a", true},
				{"a b", "$a=# $a", false},

				{"a*", "#*", true},
				{"a?", "#*", false},
				{"a b", "# (..)*", false},
				{"(a b | c)+ d", "(.. | #)+ ..", true},

				
				{"'a'", "'a'", true},
				{"'a' | [['ab']]", "'a' | [['ab']]", true},
				{"'a' | [['xb']]", "'a' | [['ab']]", false},
				{"'a' | [['xb']]", "#lex | #lex", true},
				{"'a' 'b' | [['xb']]", "#lex | #lex", false},
				{"('b' 'a') | 'a'", "('b' $a=..) | $a", true},
				{"('b' 'a') | 'a'+", "('b' $a=..) | $a+", true},
				
		});
	}
	
	private final String myExpression;
	private final String myQuery;
	private final boolean myResult;
	
	public ExpressionMatcherTest(String expression, String query, boolean result) {
		myExpression = expression;
		myQuery = query;
		myResult = result;
	}

	@Test
	public void test() throws Exception {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		Expression expression = GrammarParser.parseExpressionFromString(myExpression + ";", new HashMap<String, Symbol>(), writableAspect);
		ExpressionQuery query = QueryParser.parseExpressionQuery(myQuery + ";");
		ExpressionMatcher expressionMatcher = new Matchers(new MetadataProvider(aspect)).getExpressionMatcher();
		assertEquals(myExpression + " - " + myQuery, myResult, expressionMatcher.matchExpression(query, expression, new VariableValues()));
	}
	
}
