package org.abreslav.grammatic.template.validator;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.query.tester.GenericModelBasedTest;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.abreslav.grammatic.template.TemplatePackage;
import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import resolver_test.ResolverTest;
import resolver_test.ResolverTests;
import resolver_test.Resolver_testPackage;

@RunWith(MyParameterized.class)
public class GenericBasedTypeResolverTest extends GenericModelBasedTest<ResolverTest, ResolverTests> {

	private static final TestUtil<ResolverTest, ResolverTests> TEST_UTIL = new TestUtil<ResolverTest, ResolverTests>("testData/genericValidator/typeResolver/", Resolver_testPackage.eINSTANCE, "../../../model/type_resolver_test.ecore");
	
	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		return TEST_UTIL.loadTests("Tests.xmi", false);
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		TEST_UTIL.saveResults("Results.xmi");
	}
	
	public GenericBasedTypeResolverTest(String testsName,
			ResolverTest test, ResolverTest result) {
		super(testsName, test, result);
	}

	@Test
	public void testValidationResult() throws Exception {
		GenericBasedTypeResolver typeResolver = new GenericBasedTypeResolver();
		final EObject templateParameter_type = TEST_UTIL.getResourceSet().getEObject(URI.createURI(TemplatePackage.eINSTANCE.getNsURI() + "#//TemplateParameter/type"), false);
		EObject templateParameter_T = TEST_UTIL.getResourceSet().getEObject(URI.createURI(TemplatePackage.eINSTANCE.getNsURI() + "#//TemplateParameter/@eTypeParameters.0"), false);
		typeResolver.addInferer((ETypeParameter) templateParameter_T, new IInferer() {

			@Override
			public boolean inferTypeParameterSubstitution(
					ETypeParameter typeParameter, EObject object,
					ISubstitution substitution) {
				EClassifier type = (EClassifier) object.eGet((EStructuralFeature) templateParameter_type);
				if (type != null) {
					EGenericType genericType = EcoreFactory.eINSTANCE.createEGenericType();
					genericType.setEClassifier(type);
					substitution.addSubstitution(typeParameter, genericType);
				}
				return true;
			}
			
		});
		EObject data = myTest.getData();
		EGenericType type = typeResolver.resolveType(data);
		assertTrue(GenericTypeRenderer.render(type) +" != " + GenericTypeRenderer.render(myTest.getExpectedType()), EMFProxyUtil.equals(myTest.getExpectedType(), type));		
	}
}
