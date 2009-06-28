package org.abreslav.grammatic.template.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.query.tester.GenericModelBasedTest;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import validator_test.DiagnosticTest;
import validator_test.ValidatorTest;
import validator_test.ValidatorTests;
import validator_test.Validator_testFactory;
import validator_test.Validator_testPackage;

@RunWith(MyParameterized.class)
public class GenericBasedEObjectValidatorTest extends GenericModelBasedTest<ValidatorTest, ValidatorTests> {

	private static final TestUtil<ValidatorTest, ValidatorTests> TEST_UTIL = new TestUtil<ValidatorTest, ValidatorTests>(
			"testData/genericValidator/",
			Validator_testPackage.eINSTANCE,
			"../../model/validator_test.ecore"
	);
	
	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		return TEST_UTIL.loadTests("Tests.xmi", false);
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		TEST_UTIL.saveResults("Results.xmi");
	}

	public GenericBasedEObjectValidatorTest(String testsName,
			ValidatorTest test, ValidatorTest result) {
		super(testsName, test, result);
	}

	@Test
	public void testValidationResult() throws Exception {
		EObject data = myTest.getData();
		GenericBasedTypeResolver typeResolver = new GenericBasedTypeResolver();
		GenericBasedEObjectValidator validator = new GenericBasedEObjectValidator(typeResolver);
		typeResolver.addInferer(data.eClass().getEPackage().getEClassifier("TemplateParameter").getETypeParameters().get(0), new IInferer() {
			
			@Override
			public boolean inferTypeParameterSubstitution(
					ETypeParameter typeParameter, EObject object,
					ISubstitution substitution) {
				Object get = object.eGet(object.eClass().getEStructuralFeature("type"));
				if (get != null) {
					EGenericType type = EcoreFactory.eINSTANCE.createEGenericType();
					type.setEClassifier((EClassifier) get);
					substitution.addSubstitution(typeParameter, type);
					return true;
				}
				return false;
			}
			
		});
		Diagnostic result = validator.validate(data);
		
		if (result.getSeverity() != Diagnostic.OK) {
			EList<DiagnosticTest> errors = myTest.getErrors();
			myResult.getErrors().clear();
			for (Diagnostic diagnostic : result.getChildren()) {
				DiagnosticTest convert = convert(diagnostic);
				myResult.getErrors().add(convert);
			}
			assertEquals("number of errors differ", errors.size(), result.getChildren().size());
			int i = 0;
			for (DiagnosticTest error : errors) {
				DiagnosticTest diagnostic = convert(result.getChildren().get(i));
				assertTrue(diagnostic.getMessage() + " != " + error.getMessage(), EMFProxyUtil.equals(error, diagnostic));
				i++;
			}
		}
		assertEquals(myTest.isExprectedToBeOK(), result.getSeverity() == Diagnostic.OK);
	}

	private DiagnosticTest convert(Diagnostic diagnostic) {
		DiagnosticTest result = Validator_testFactory.eINSTANCE.createDiagnosticTest();
		result.setCode(diagnostic.getCode());
		result.setMessage(diagnostic.getMessage());
		result.setSeverity(diagnostic.getSeverity());
		result.setSource(diagnostic.getSource());
		
		return result;
	}
}
