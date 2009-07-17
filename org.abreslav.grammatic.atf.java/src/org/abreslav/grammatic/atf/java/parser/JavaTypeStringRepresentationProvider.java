/**
 * 
 */
package org.abreslav.grammatic.atf.java.parser;

import java.util.Map;
import java.util.WeakHashMap;

import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.template.util.GenericTypeRenderer;
import org.eclipse.emf.ecore.EGenericType;

public final class JavaTypeStringRepresentationProvider implements
		IStringRepresentationProvider<EGenericType> {
	private final Map<EGenericType, String> myCache = new WeakHashMap<EGenericType, String>();

	@Override
	public String getStringRepresentation(EGenericType type) {
		String result = myCache.get(type);
		if (result == null) {
			result = GenericTypeRenderer.render(type).intern();
			myCache.put(type, result);
		}
		return result;
	}
}