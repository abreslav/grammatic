package org.abreslav.grammatic.parsingutils;

import java.util.Collection;
import java.util.Set;



public class ScopeUtils {

	public static void registerNames(INameScope scope, Collection<? extends CharSequence> keywords) {
		for (CharSequence keyword : keywords) {
			scope.registerName(keyword.toString());
		}
	}
	
	public static void registerJavaKeywords(INameScope scope) {
		Set<String> keywords = JavaUtils.getJavaReservedWords();
		registerNames(scope, keywords);
	}
	
	public static INameScope getSafeToplevelScope() {
		INameScope scope = NameScope.createTopLevelScope();
		ScopeUtils.registerJavaKeywords(scope);
		return scope;
	}
}
