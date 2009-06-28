package org.abreslav.grammatic.atf.java.antlr.generator;

import org.abreslav.grammatic.atf.java.antlr.ANTLRCharacterRange;

public class ANTLRUtils {
	
	public static boolean isSingleCharacter(final ANTLRCharacterRange object) {
		return object.getLowerBound() == object.getUpperBound();
	}

}
