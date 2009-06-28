package org.abreslav.grammatic.template.instantiator;

import static org.junit.Assert.assertEquals;
import instantiator_test.InstantiatorTest;
import instantiator_test.InstantiatorTests;
import instantiator_test.Instantiator_testPackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.abreslav.grammatic.emfutils.EMFCopier;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplatePackage;
import org.abreslav.grammatic.query.tester.GenericModelBasedTest;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.abreslav.grammatic.template.TemplateApplication;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class TemplateInstantiatorInterpreterTest extends GenericModelBasedTest<InstantiatorTest, InstantiatorTests> {

	private static final TestUtil<InstantiatorTest, InstantiatorTests> TEST_UTIL = new TestUtil<InstantiatorTest, InstantiatorTests>(
		"testData/templateInstantiator",
		Instantiator_testPackage.eINSTANCE,
		"../../model/instantiator_test.ecore"
	);
	private static final InstantiatorModel GRAMMAR_INSTANTIATOR;
	static {
		try {
			GRAMMAR_INSTANTIATOR = (InstantiatorModel) new ResourceLoader(".").loadStaticModel(
					"../org.abreslav.grammatic.grammar.template/model/instantiators/grammarInstantiator.xmi", 
						InstantiatorPackage.eINSTANCE,
						GrammarTemplatePackage.eINSTANCE
					);
		} catch (IOException e) {
			throw new IllegalStateException("cannot load instantiator model", e);
		}
	}
	
	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		return TEST_UTIL.loadTests("Tests.xmi", false);
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		TEST_UTIL.saveResults("Results.xmi");
	}
	
	public TemplateInstantiatorInterpreterTest(String testsName,
			InstantiatorTest test, InstantiatorTest result) {
		super(testsName, test, result);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testInstantiator() throws Exception {
		TemplateApplication templateApplication = myTest.getTemplateApplication();
		TemplateInstantiatorInterpreter instantiator = new TemplateInstantiatorInterpreter(IInstantiationHandler.DEFAULT, new URIBasedMetadataResolver(), ISpecialTemplateInstantiator.DEFAULT, EMFCopier.DEFAULT_COPY_HANDLER);
		instantiator.addInstantiatorModel(GRAMMAR_INSTANTIATOR);
		Collection result = instantiator.instantiate(templateApplication);
		EObject data = (EObject) result.iterator().next();
		compareXMIRepresentations(data, myTest.getData());
		myResult.setData(data);
	}

	private void compareXMIRepresentations(EObject data, EObject expected)
			throws IOException {
		ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
		ByteArrayOutputStream expectedBytes = new ByteArrayOutputStream();
		ResourceLoader resourceLoader = new ResourceLoader(".");
		Map<String, Boolean> options = Collections.singletonMap(XMLResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
		resourceLoader.printToSteam(dataBytes, options, data);
		resourceLoader.printToSteam(expectedBytes, options, expected);
		byte[] dataArray = dataBytes.toByteArray();
		byte[] expectedArray = expectedBytes.toByteArray();
		assertEquals(new String(expectedArray), new String(dataArray));
	}
}
