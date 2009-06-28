package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.query.SymbolQuery;

public class SymbolMatcher {
	public static boolean matchSymbol(SymbolQuery query, Symbol symbol) {
		String expectedName = query.getName();
		return expectedName == null 
			|| expectedName.equals(symbol.getName());
		
	}
}
