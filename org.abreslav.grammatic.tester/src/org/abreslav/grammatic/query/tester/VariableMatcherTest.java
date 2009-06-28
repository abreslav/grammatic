package org.abreslav.grammatic.query.tester;

import static org.abreslav.grammatic.testing.Utils.$;
import static org.abreslav.grammatic.testing.Utils.alternative;
import static org.abreslav.grammatic.testing.Utils.character;
import static org.abreslav.grammatic.testing.Utils.empty;
import static org.abreslav.grammatic.testing.Utils.sequence;
import static org.abreslav.grammatic.testing.Utils.symbolReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.grammar.template.parser.GrammarParser;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.query.ExpressionQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.interpreter.IVariableValues;
import org.abreslav.grammatic.query.interpreter.Matchers;
import org.abreslav.grammatic.query.interpreter.VariableValues;
import org.abreslav.grammatic.query.variables.AlternativePartValue;
import org.abreslav.grammatic.query.variables.ExpressionValue;
import org.abreslav.grammatic.query.variables.SequencePartValue;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.abreslav.grammatic.query.variables.VariablesFactory;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VariableMatcherTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
				{
					"a | a", "$a=# | $a", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
					}
				},
				{
					"a (a | b)", "$a=# ($a | $b=#)", 
					true,
					new Object[][] {
							{"b", "b"}, 	
							{"a", "a", "a"}, 	
					}
				},
				{
					"a a", "$a=# $a", 
					true,
					new Object[][] {
							{"a", "a", "a"}, 	
					}
				},
				{
					"a a a a", "$a=# $b=# $a $b", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
						{"b", "a", "a"}, 	
					}
				},
				{
					"a b", "$a=# $b=#", 
					true,
					new Object[][] {
						{"a", "a"}, 	
						{"b", "b"}, 	
					}
				},
				{
					"#empty #empty", "$a=..", 
					true,
					new Object[][] {
						{"a", sequence(empty(), empty())}, 	
					}
				},
				{
					"a c b b", "$a=# .. $b=# $b", 
					false,
					new Object[][] {
						{"a", "a"}, 	
						{"b", "b", "b"}, 	
					}
				},
				{
					"a c b b", "$a=# $c=.. $b=# $b", 
					false,
					new Object[][] {
						{"a", "a"}, 	
						{"b", "b", "b"}, 	
						{"c", "c"}, 	
					}
				},
				{
					"#empty #empty | a", "$a=(#empty #empty) | $b=#", 
					true,
					new Object[][] {
						{"a", sequence(empty(), empty())}, 	
						{"b", "a"}, 	
					}
				},
				{
					"b | a | a", "$a | $a=# | #", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
						}
					
				},
				{
					"x | a (b | b b (c d)) c d (b | d)", 
					"# ($b=# | $b $b ($c=# $d=#)) $c $d ($b | $d) | #", 
					true, 
					new Object[][] {
							{"b", "b", "b", "b", "b"}, 	
							{"c", "c", "c"}, 	
							{"d", "d", "d", "d"}, 	
						}
					
				},
				{
					"x | (b | d a)", 
					"($b=# | # #) | #", 
					true, 
					new Object[][] {
							{"b", "b"}, 	
					}
					
				},
				{
					"a b a", 
					"$a=# $b=.. $a", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"b", "b"}, 	
						}
				},
				{
					"a #empty #empty a", 
					"$a=# $b=.. $a", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"b", ' ', $(empty(), empty())}, 	
						}
				},
				{
					"a a", 
					"$a=# $b=.. $a", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"b", empty()}, 	
						}
				},
				{
					"a c", 
					"$a=# # $b=..", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"b", empty()}, 	
						}
				},
				{
					"a #empty #empty", 
					"$a=# $b=..", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"b", ' ', $(empty(), empty())}, 	
						}
				},
				{
					"a (c c) c", 
					"$a=# .. $c=#", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"c", "c"}, 	
						}
				},

				{
					"a b", "$a=# $a", 
					false, null
				},
				{
					"a | b | c", "$a | $a=# | #", 
					false, null
				},
				{
					"a b", "$a=(#|#) $a", 
					false, null
				},
				{
					"a | b | c | d ", "$a=# | $b=# | $c=# | $a ", 
					false, null
				},
				{
					"a c b", "$a=# .. $b=#", 
					false, null
				},

				{
					"a b | b", 
					"$a=# # | $b=...", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"b", "b"}, 	
						}
				},
				{
					"a b | #empty | #empty", 
					"$a=# # | $b=...", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"b", '|', $(empty(), empty())}, 	
						}
				},
				{
					"a b", 
					"$a=# # | $b=...", 
					true, 
					new Object[][] {
							{"a", "a"}, 	
							{"b", empty()}, 	
						}
				},
				
				{
					"(a #empty #empty) (a | #empty #empty)", 
					"($a=# $seq=..) ($a | $seq)", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"seq", ' ', $(empty(), empty()), sequence(empty(), empty())}, 	
						}
				},
				
				{
					"(a #empty b) (a | #empty #empty)", 
					"($a=# $seq=..) ($a | $seq)", 
					false, null 
				},
				
				{
					"(a #empty) (a | #empty)", 
					"($a=# $seq=..) ($a | $seq)", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"seq", empty(), empty()}, 	
						}
				},
				{
					"(a | #empty | #empty) (a (#empty | #empty))", 
					"($a=# | $alt=...) ($a $alt)", 
					true, 
					new Object[][] {
							{"a", "a", "a"}, 	
							{"alt", '|', $(empty(), empty()), alternative(empty(), empty())}, 	
						}
				},
				{
					"(a | #empty | #empty) (a (#empty | #empty | a))", 
					"($a=# | $alt=...) ($a $alt)", 
					false, null
				},
				
				{
					"(a | #empty | #empty) (a a)", 
					"($a=# | $alt=...) ($a $alt)", 
					false, null
				},
				
				{
					"(a #empty #empty) (a #empty #empty)", 
					"($a=# $seq=..) ($a $seq)", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
						{"seq", ' ', $(empty(), empty()), $(empty(), empty())}, 	
					}
				},
				{
					"(a #empty #empty) a #empty #empty", 
					"($a=# $seq=..) $a $seq", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
						{"seq", ' ', $(empty(), empty()), $(empty(), empty())}, 	
					}
				},
				{
					"a #empty #empty (a #empty #empty)", 
					"$a=# $seq=.. ($a $seq)", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
						{"seq", ' ', $(empty(), empty()), $(empty(), empty())}, 	
					}
				},
				{
					"a #empty #empty a #empty #empty", 
					"$a=# $seq=.. $a $seq", 
					true,
					new Object[][] {
						{"a", "a", "a"}, 	
						{"seq", ' ', $(empty(), empty()), $(empty(), empty())}, 	
					}
				},
				{
					"a #empty #empty a #empty #empty #empty", 
					"$a=# $seq=.. $a $seq", 
					false, null
				},
				{
					"a #empty #empty a #empty", 
					"$a=# $seq=.. $a $seq", 
					false, null
				},
				{
					"a #empty #empty a #empty a", 
					"$a=# $seq=.. $a $seq", 
					false, null
				},
				
				{
					"(a | #empty | #empty) (a a | #empty | #empty | #empty)", 
					"($a=# | $alt=...) ($a $a | $alt)", 
					false, 
					null
				},
				{
					"(a | #empty | #empty) (a a | #empty | #empty | #empty)", 
					"($a=# | $alt=...) ($a $a | #empty | $alt)", 
					true, 
					new Object[][] {
						{"a", "a", "a", "a"},
						{"alt", '|', $(empty(), empty())},
					}
				},
				{
					"(a a #empty #empty a #empty #empty a) | x", 
					"($a=# $a $seq=.. $a $seq $a) | #", 
					true, 
					new Object[][] {
							{"a", "a", "a", "a", "a"},
							{"seq", ' ', $(empty(), empty()), $(empty(), empty())},
					}
				},
				{
					"(('a' 'a') | ('b' 'b') | 'm') ('a' | ('b' 'b') | 'm')", 
					"(('a' 'a') | $alt=...) ('a' | $b=... )", 
					true, 
					new Object[][] {
							{"b", '|', $(
									sequence(character('b'), character('b')),
									character('m')
							)},
							{"alt", '|', $(
									sequence(character('b'), character('b')),
									character('m')
							)},
					}
				},
		});
	}
	
	private final Expression myExpression;
	private final String myExpressionString;
	private final String myQuery;
	private final boolean myResult;
	private final Map<String, List<VariableValue>> myVariables = new LinkedHashMap<String, List<VariableValue>>();
	private MetadataAspect myMetadataAspect;
	
	@SuppressWarnings("unchecked")
	public VariableMatcherTest(String expression, String query, boolean result, Object[][] vars) throws IOException, RecognitionException {
		myQuery = query;
		myResult = result;
		myExpressionString = expression;
		Map<String, Symbol> symbols = new HashMap<String, Symbol>();
		myMetadataAspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(myMetadataAspect);
		myExpression = GrammarParser.parseExpressionFromString(expression + ";", symbols, writableAspect);
		if (result) {
			for (Object[] objects : vars) {
				List<VariableValue> values = new ArrayList<VariableValue>();
				if (objects[1] instanceof String) { 
					for (int i = 1; i < objects.length; i++) {
						String symbolName = (String) objects[i];
						SymbolReference symbolReference = symbolReference(symbols.get(symbolName));
						values.add(createExpressionValue(symbolReference));
					}
				} else if (objects[1] instanceof Expression){
					for (int i = 1; i < objects.length; i++) {
						values.add(createExpressionValue((Expression) objects[i]));
					}
				} else if (objects[1] == Character.valueOf(' ')) {
					for (int i = 2; i < objects.length; i++) {
						if (objects[i] instanceof Collection) {
							SequencePartValue value = VariablesFactory.eINSTANCE.createSequencePartValue();
							value.getItems().addAll((Collection<? extends Expression>) objects[i]);
							values.add(value);
						} else {
							values.add(createExpressionValue((Expression) objects[i]));
						}
					}
				} else if (objects[1] == Character.valueOf('|')) {
					for (int i = 2; i < objects.length; i++) {
						if (objects[i] instanceof Collection) {
							AlternativePartValue value = VariablesFactory.eINSTANCE.createAlternativePartValue();
							value.getItems().addAll((Collection<? extends Expression>) objects[i]);
							values.add(value);
						} else {
							values.add(createExpressionValue((Expression) objects[i]));
						}
					}
				}
				myVariables.put((String) objects[0], values);
			}
		}
	}

	private ExpressionValue createExpressionValue(Expression expr) {
		ExpressionValue value = VariablesFactory.eINSTANCE.createExpressionValue();
		value.setItem(expr);
		return value;
	}

	@Test
	public void test() throws Exception {
		ExpressionQuery query = QueryParser.parseExpressionQuery(myQuery + ";");
		IVariableValues resultVariables = new VariableValues();
		Matchers matchers = new Matchers(new MetadataProvider(myMetadataAspect));
		assertEquals(myExpressionString + " - " + myQuery, myResult, matchers.getExpressionMatcher().matchExpression(query, myExpression, resultVariables));
		if (myResult) {
			Set<String> varNames = new HashSet<String>(myVariables.keySet());
			for (Iterator<VariableDefinition> variableIterator = resultVariables.variableIterator(); variableIterator.hasNext(); ) {
				VariableDefinition var = variableIterator.next();
				List<VariableValue> expectedValues = myVariables.get(var.getName());
				VariableValue expectedExpression = expectedValues.get(0);
				VariableValue resultValue = resultVariables.getVariableValue(var);
				assertTrue(myQuery + " :: " + var.getName() + " = " + resultValue, 
						EMFProxyUtil.equals(resultValue, expectedExpression));
				
				Collection<VariableValue> resultValues = resultVariables.getAllVariableValues(var);
				Iterator<VariableValue> resultIterator = resultValues.iterator();
				for (VariableValue expectedValue : expectedValues) {
					if (!resultIterator.hasNext()) {
						fail(myQuery + " :: " + var.getName() + " has less values than expected: " 
								+ resultValues.size() + " instead of " + expectedValues.size());
					}
					VariableValue result = resultIterator.next();
					assertTrue(myQuery + " :: " + var.getName() + " has value " + result 
							+ " when expected " + expectedValue, 
							EMFProxyUtil.equals(expectedValue, result));
				}
				assertFalse(myQuery + " :: " + var.getName() + " has more values than expected: " 
						+ resultValues.size() + " instead of " + expectedValues.size(), 
						resultIterator.hasNext());
				
				varNames.remove(var.getName());
			}
			assertTrue("Absent variables: " + varNames.toString(), varNames.isEmpty());
		}
	}
	
}
