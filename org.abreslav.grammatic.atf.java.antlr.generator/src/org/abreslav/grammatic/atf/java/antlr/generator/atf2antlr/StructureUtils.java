package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;

public class StructureUtils {
	
	public static Grammar getGrammar(Symbol symbol) {
		return (Grammar) symbol.eContainer();
	}

}
