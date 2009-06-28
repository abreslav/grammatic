package org.abreslav.grammatic.grammar.template.parser;

public class GrammaticCharacterBuilders implements IGrammaticCharacterBuilders {

	@Override
	public ICharacterBuilder getCharacterBuilder() {
		return new ICharacterBuilder() {

			@Override
			public void init() {
				// Nothing
			}

			@Override
			public void release() {
				// Nothing
			}

			@Override
			public char character(String character) {
				char c = character.charAt(1);
				if (c != '\\') {
					return c;
				}
				char escaped = character.charAt(2);
				switch (escaped) {
				case '\\':
				case '\'':
				case '\"':
					return escaped;
				case 'n':
					return '\n';
				case 'r':
					return '\r';
				case 't':
					return '\t';
				case 'f':
					return '\f';
				case 'b':
					return '\b';
				default:
					throw new IllegalArgumentException();
				}
			}

			@Override
			public char code(String code) {
				String s = code.substring(2);
				char c = 0;
				for (int i = 0; i < 4; i++) {
					char dig = Character.toLowerCase(s.charAt(i));
					c = (char) (c * 16);
					if (Character.isDigit(dig)) {
						c += dig - '0';
					} else {
						c += 10 + dig - 'a';
					}
				}
				return c;
			}			
		};
	}


}
