package org.abreslav.grammatic.utils;

public class CharacterUtils {
	public static String getChararcterRepresentation(int c) {
		String charRepresentation;
		switch (c) {
		case '\n':
			charRepresentation = "\\n";
			break;
		case '\r':
			charRepresentation = "\\r";
			break;
		case '\\':
			charRepresentation = "\\\\";
			break;
		case '\'':
			charRepresentation = "\\'";
			break;
		default:
			if (31 < c && c < 128 && c != '\'' && c != '\\') {
				charRepresentation = String.valueOf((char) c);
			} else {
				charRepresentation = String.format("\\u%04x", c);
			}
		}
		return charRepresentation;
	}

	public static CharSequence escape(String value) {
		StringBuilder builder = new StringBuilder();
		int length = value.length();
		for (int i = 0; i < length; i++) {
			builder.append(getChararcterRepresentation(value.charAt(i)));
		}
		return builder.toString();
	}


}
