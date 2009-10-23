package org.abreslav.grammatic.query.tester;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.query.AttributeQuery;
import org.abreslav.grammatic.query.interpreter.AttributeMatcher;
import org.abreslav.grammatic.query.interpreter.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class AttributeMatcherTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		Object[][] result = new Object[][] {
				{"#empty{a=b}", "{a}", true},
				{"#empty{a=b}", "{a:#ID}", true},
				{"#empty{a=b}", "{a=b}", true},
				{"#empty{a=b}", "{!b}", true},
				{"#empty", "{!b}", true},
				
				{"#empty{a=b}", "{!a}", false},
				{"#empty{aa=b}", "{a}", false},
				{"#empty{a=b}", "{a:#EXPRESSION}", false},
				{"#empty{a=b}", "{a=bb}", false},
				{"#empty{a=b}", "{b=xx}", false},
				
				{"#empty{a=b;c=1;d='a'}", "{a=b;d:#STRING}", true},
				
				{"#empty{a=b;c=1;d='a'}", "{a=b;x;d:#STRING}", false},
				
		};
		Collection<Object[]> res = new ArrayList<Object[]>();
		for (Object[] objects : result) {
			String name = objects[0] + " ~ " + objects[1];
			Object[] resultLine = new Object[objects.length + 1];
			resultLine[0] = name;
			System.arraycopy(objects, 0, resultLine, 1, objects.length);
			res.add(resultLine);
		}
		return res;
	}
					
	private final String myExpression;
	private final String myQueries;
	private final boolean myResult;
	
	public AttributeMatcherTest(String expression, String queries, boolean result) {
		myExpression = expression;
		myQueries = queries;
		myResult = result;
	}

	@Test
	public void testExactValue() throws Exception {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		Expression expression = GrammarParserUtils.parseExpressionFromString(myExpression + ";", new HashMap<String, Symbol>(), writableAspect);
		
		List<AttributeQuery> queries = getQueryParser().parseAttributeQuery(myQueries);
		MetadataProvider metadataProvider = new MetadataProvider(aspect);
		AttributeMatcher attributeMatcher = new Matchers(metadataProvider).getAttributeMatcher();

		
		assertEquals(myExpression + " - " + myQueries, myResult, attributeMatcher.matchAttributes(queries, expression));
	}

	protected IQueryParser getQueryParser() {
		return QueryParser.INSTANCE;
	}
	
}
