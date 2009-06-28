package org.abreslav.grammatic.atf.interpreter;

import static org.junit.Assert.assertEquals;

import org.abreslav.grammatic.atf.java.parser.JavaLangPackage;
import org.abreslav.grammatic.atf.java.parser.JavaSubtypingRelation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Test;


public class JavaTypeSystemTest {
	
	@Test
	public void testSubtyping() throws Exception {
		int[][] subtypingMatrix = new int[][] {
			//   bo ch in by lo sh do fl
				{1, 0, 0, 0, 0, 0, 0, 0}, // bo
				{0, 1, 1, 0, 1, 0, 1, 1}, // ch
				{0, 0, 1, 0, 1, 0, 1, 1}, // in
				{0, 0, 1, 1, 1, 1, 1, 1}, // by
				{0, 0, 0, 0, 1, 0, 1, 1}, // lo
				{0, 0, 1, 0, 1, 1, 1, 1}, // sh
				{0, 0, 0, 0, 0, 0, 1, 0}, // do
				{0, 0, 0, 0, 0, 0, 1, 1}, // fl
		};
		
		EClassifier[] primitiveTypes = {
				JavaLangPackage.BOOLEAN,
				JavaLangPackage.CHAR,
				JavaLangPackage.INT,
				JavaLangPackage.BYTE,
				JavaLangPackage.LONG,
				JavaLangPackage.SHORT,
				JavaLangPackage.DOUBLE,
				JavaLangPackage.FLOAT,
		};
		
		JavaSubtypingRelation relation = JavaSubtypingRelation.INSTANCE;
		for (int i = 0; i < primitiveTypes.length; i++) {
			for (int j = 0; j < primitiveTypes.length; j++) {
				EClassifier a = primitiveTypes[i];
				EClassifier b = primitiveTypes[j];
				assertEquals(a.getName() + " iSO " + b.getName(), (subtypingMatrix[i][j] == 1), relation.isSubtypeOf(gt(a), gt(b)));
			}
		}
	}

	private EGenericType gt(EClassifier classifier) {
		EGenericType re = EcoreFactory.eINSTANCE.createEGenericType();
		re.setEClassifier(classifier);
		return re;
	}
}
