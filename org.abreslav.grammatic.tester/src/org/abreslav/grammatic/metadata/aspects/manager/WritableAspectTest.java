package org.abreslav.grammatic.metadata.aspects.manager;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import metadata_provider_test.MetadataProviderTests;
import metadata_provider_test.Metadata_provider_testPackage;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.AttributeAssignment;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.query.tester.GenericModelBasedTest;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class WritableAspectTest extends GenericModelBasedTest<metadata_provider_test.MetadataProviderTest, MetadataProviderTests> {

	private static final TestUtil<metadata_provider_test.MetadataProviderTest, MetadataProviderTests> TEST_UTIL = new TestUtil<metadata_provider_test.MetadataProviderTest, MetadataProviderTests>(
			"testData/metadataProvider/.",
			Metadata_provider_testPackage.eINSTANCE,
			"../../model/metadata_provider_test.ecore"			
	);
	
	@Parameters
	public static Collection<Object[]> parameters() throws IOException {
		return TEST_UTIL.loadTests("Tests.xmi", false);
	}
	
	@AfterClass
	public static void afterClass() throws IOException {
		TEST_UTIL.saveResults("Results.xmi");
	}

	public WritableAspectTest(String testsName, metadata_provider_test.MetadataProviderTest test,
			metadata_provider_test.MetadataProviderTest result) {
		super(testsName, test, result);
	}

	@Test
	public void testSetAttribute() {
		MetadataAspect aspect = myTest.getAspect();
		MetadataAspect result = AspectsFactory.eINSTANCE.createMetadataAspect();
		IWritableAspect writableAspect = AspectWriter.createWritableAspect(result);
		for (AttributeAssignment assignment : aspect.getAssignments()) {
			for (Attribute attribute : assignment.getAttributes()) {
				Value valueCopy = attribute.getValue() == null ? null : EMFProxyUtil.copy(attribute.getValue());
				writableAspect.setAttribute(assignment.getSubject(), attribute.getNamespace(), attribute.getName(), valueCopy);
			}
		}
		myResult.setAspect(result);
		assertTrue(EMFProxyUtil.equals(aspect, result));
	}

}
