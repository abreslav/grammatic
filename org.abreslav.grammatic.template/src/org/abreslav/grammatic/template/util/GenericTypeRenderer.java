package org.abreslav.grammatic.template.util;

import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcorePackage;

public class GenericTypeRenderer {
	
	public static String render(EGenericType genericType) {
		if (genericType == null) {
			return "null";
		}
		if (genericType.getETypeParameter() != null) {
			return rawRender(genericType);
		}
		switch (genericType.getEClassifier().getClassifierID()) {
		case EcorePackage.ECHAR:
			return "char";
		case EcorePackage.EBYTE:
			return "byte";
		case EcorePackage.EINT:
			return "int";
		case EcorePackage.ELONG:
			return "long";
		case EcorePackage.ESHORT:
			return "short";
		case EcorePackage.EBOOLEAN:
			return "boolean";
		case EcorePackage.EFLOAT:
			return "float";
		case EcorePackage.EDOUBLE:
			return "double";
		default:
			return rawRender(genericType);
		}
	}

	private static String rawRender(EGenericType genericType) {
		String gts = genericType.toString();
		return gts.substring(gts.indexOf(':') + 2, gts.indexOf(')'));
	}
	
}
