package org.abreslav.grammatic.antlr.generator;

import org.abreslav.grammatic.antlr.generator.antlr.ANTLRCharacterRange;

public class ANTLRUtils {
	
	public static boolean isSingleCharacter(final ANTLRCharacterRange object) {
		return object.getLowerBound() == object.getUpperBound();
	}

}
