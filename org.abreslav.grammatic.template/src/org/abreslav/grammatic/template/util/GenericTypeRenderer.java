package org.abreslav.grammatic.template.util;

import org.eclipse.emf.ecore.EGenericType;

public class GenericTypeRenderer {
	
	public static String render(EGenericType genericType) {
		if (genericType == null) {
			return "null";
		}
		String gts = genericType.toString();
		return gts.substring(gts.indexOf(':') + 2, gts.indexOf(')'));
	}
	
}
