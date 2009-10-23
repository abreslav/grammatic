package org.abreslav.grammatic.query.tester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import matcher_test.ExpectedVariableValue;
import matcher_test.MatcherTest;
import matcher_test.MatcherTests;
import matcher_test.Matcher_testPackage;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;
import org.abreslav.grammatic.query.RuleQuery;
import org.abreslav.grammatic.query.VariableDefinition;
import org.abreslav.grammatic.query.interpreter.Matchers;
import org.abreslav.grammatic.query.interpreter.ModelPreprocessor;
import org.abreslav.grammatic.query.interpreter.VariableValues;
import org.abreslav.grammatic.query.variables.VariableValue;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.common.util.EList;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class RuleMatcherTest extends GenericModelBasedTest<MatcherTest, MatcherTests> {
	private static final TestUtil<MatcherTest, MatcherTests> TEST_UTIL = new TestUtil<MatcherTest, MatcherTests>(
		"testData/ruleMatcher",
		Matcher_testPackage.eINSTANCE,
		"../../model/matcher_test.ecore"
	);
	
	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		return TEST_UTIL.loadTests("Tests.xmi", false);
	}
	
	public RuleMatcherTest(String testsName, MatcherTest test,
			MatcherTest result) {
		super(testsName, test, result);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TEST_UTIL.saveResults("Results.xmi");
	}

	@Test
	public void testSymbolQuery() throws IOException, RecognitionException {
		QueryContainer<RuleQuery> parsedQuery = getQueryParser().parseRuleQuery(myTest.getText());
		VariableValues variables = new VariableValues();
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		new ModelPreprocessor(writableAspect).attachSystemMetadataToASymbol(myTest.getSymbol());
		Matchers matchers = new Matchers(new MetadataProvider(aspect));
		boolean result = matchers.getRuleMatcher().matchRule(parsedQuery.getQuery(), myTest.getSymbol(), variables);
		
		String message = myTest.getText() + " (" + myTestsName + ")";
		assertEquals(message, myTest.isExpectedToBeEqual(), result);

		if (!myTest.isExpectedToBeEqual()) {
			return;
		}
		
		Map<String, VariableDefinition> nameToVar = buildNameToVarMap(parsedQuery);
		
		fillResult(variables, nameToVar);
		
		Set<VariableDefinition> expectedVariables = checkVariables(parsedQuery,
				variables, message, nameToVar);
		
		String unsetVariables = "";
		for (VariableDefinition variableDefinition : expectedVariables) {
			unsetVariables += variableDefinition.getName() + " ";
		}
		assertTrue(message + " Unset vars: " + unsetVariables, expectedVariables.isEmpty());
	}

	protected IQueryParser getQueryParser() {
		return QueryParser.INSTANCE;
	}

	private Set<VariableDefinition> checkVariables(
			QueryContainer<? extends Query> parsedQuery, VariableValues variables,
			String message, Map<String, VariableDefinition> nameToVar) {
		EList<ExpectedVariableValue> values = myTest.getVariableValues();
		Set<VariableDefinition> expectedVariables = new HashSet<VariableDefinition>(parsedQuery.getVariableDefinitions());
		for (ExpectedVariableValue variableValue : values) {
			VariableDefinition vdef = nameToVar.get(variableValue.getName());
			expectedVariables.remove(vdef);
			assertNotNull(message + ": Variable not found: " + variableValue.getName(), vdef);
			VariableValue value = variables.getVariableValue(vdef);
			assertTrue(message + " Variable value does not match: " + variableValue.getName() + " " + value, EMFProxyUtil.equals(variableValue.getValue(), value));
		}
		return expectedVariables;
	}

	private Map<String, VariableDefinition> buildNameToVarMap(
			QueryContainer<? extends Query> parsedQuery) {
		Map<String, VariableDefinition> nameToVar = new HashMap<String, VariableDefinition>();
		for (VariableDefinition variableDefinition : parsedQuery.getVariableDefinitions()) {
			nameToVar.put(variableDefinition.getName(), variableDefinition);
		}
		return nameToVar;
	}

	private void fillResult(VariableValues variables,
			Map<String, VariableDefinition> nameToVar) {
		EList<ExpectedVariableValue> vars = myResult.getVariableValues();
		for (ExpectedVariableValue expectedVariableValue : vars) {
			expectedVariableValue.setValue(variables.getVariableValue(nameToVar.get(expectedVariableValue.getName())));
		}
	}
}
