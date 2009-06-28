package org.abreslav.grammatic.parsingutils;



public class ScopeUtils {

	public static void registerJavaKeywords(NameScope scope) {
		for (String keyword : JavaUtils.getJavaReservedWords()) {
			scope.registerName(keyword);
		}
	}
	
	public static NameScope getSafeToplevelScope() {
		NameScope scope = NameScope.createTopLevelScope();
		ScopeUtils.registerJavaKeywords(scope);
		return scope;
	}
}
