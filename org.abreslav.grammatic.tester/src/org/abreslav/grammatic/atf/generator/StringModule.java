/**
 * 
 */
package org.abreslav.grammatic.atf.generator;

import org.abreslav.grammatic.grammar1.IString;

public class StringModule implements IString {
	@Override
	public String createString(String token) {
		return token.substring(1, token.length() - 1);
	}

	@Override
	public String null1() {
		return null;
	}
}