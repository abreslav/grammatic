package org.abreslav.grammatic.parsingutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtils {
	private static Set<String> ourJavaReservedWords;
	private static Map<Character, String> ourCharacterNames;

	public static Set<String> getJavaReservedWords() {
		if (ourJavaReservedWords == null) {
			Set<String> result = new HashSet<String>(100);
			result.add("abstract");
			result.add("assert");
			result.add("boolean");
			result.add("break");
			result.add("byte");
			result.add("case");
			result.add("catch");
			result.add("char");
			result.add("class");
			result.add("const");
			result.add("continue");
			result.add("default");
			result.add("do");
			result.add("double");
			result.add("else");
			result.add("enum");
			result.add("extends");
			result.add("false");
			result.add("final");
			result.add("finally");
			result.add("float");
			result.add("for");
			result.add("goto");
			result.add("if");
			result.add("implements");
			result.add("import");
			result.add("instanceof");
			result.add("int");
			result.add("interface");
			result.add("long");
			result.add("native");
			result.add("new");
			result.add("null");
			result.add("package");
			result.add("private");
			result.add("protected");
			result.add("public");
			result.add("return");
			result.add("short");
			result.add("static");
			result.add("strictfp");
			result.add("super");
			result.add("switch");
			result.add("synchronized");
			result.add("this");
			result.add("throw");
			result.add("throws");
			result.add("transient");
			result.add("true");
			result.add("try");
			result.add("void");
			result.add("volatile");
			result.add("while");
			ourJavaReservedWords = Collections.unmodifiableSet(result);
		}
		return ourJavaReservedWords;
	}

	public static String getCharacterName(char c) {
		if (ourCharacterNames == null) {
			Map<Character, String> result = new HashMap<Character, String>(20);
			result.put('\n', "CR");
			result.put('\r', "LF");
			result.put('\t', "Tab");
			result.put('\f', "EOF");
			result.put('\b', "Backspace");
			result.put('\'', "Apostrophe");
			result.put('\"', "Quotation");
			result.put('\\', "Backslash");
			result.put(' ', "Space");
			result.put('~', "Tilde");
			result.put('`', "Grave");
			result.put('!', "Exclamation");
			result.put('@', "At");
			result.put('#', "Hash");
			result.put('$', "Dollar");
			result.put('%', "Percent");
			result.put('^', "Circumflex");
			result.put('&', "Ampersand");
			result.put('*', "Asterisk");
			result.put('(', "LeftBrace");
			result.put(')', "RightBrace");
			result.put('-', "Minus");
			result.put('_', "Underscore");
			result.put('+', "Plus");
			result.put('=', "Equals");
			result.put('|', "Bar");
			result.put('[', "LeftBracket");
			result.put(']', "RightBracket");
			result.put('{', "LeftCurly");
			result.put('}', "RightCurly");
			result.put(';', "Semicolon");
			result.put(':', "Colon");
			result.put('/', "Slash");
			result.put('?', "Question");
			result.put('<', "Less");
			result.put('>', "Greater");
			result.put('.', "Dot");
			result.put(',', "Comma");
			ourCharacterNames = Collections.unmodifiableMap(result);
		}
		if (Character.isJavaIdentifierPart(c)) {
			return String.valueOf(c);
		}
		String name = ourCharacterNames.get(c);
		if (name == null) {
			return String.format("u%04x", c);
		}
		return name;
	}
	
	public static boolean isVoid(String type) {
		return type == null
			|| "void".equals(type.toLowerCase());
	}
	
	private enum Convention { 
		VAR {
			@Override
			public void processFragment(StringBuilder builder, String fragment) {
				builder.append(fragment.substring(0, 1).toUpperCase());
				builder.append(fragment.substring(1).toLowerCase());
			}

			@Override
			public void processFirstFragment(StringBuilder builder, String fragment) {
				builder.append(fragment.toLowerCase());
			}
		},
		
		TYPE {
			@Override
			public void processFragment(StringBuilder builder, String fragment) {
				VAR.processFragment(builder, fragment);
			}
		},
		
		CONST {
			@Override
			public void processFirstFragment(StringBuilder builder,
					String fragment) {
				builder.append(fragment.toUpperCase());
			}
			
			@Override
			public void processFragment(StringBuilder builder, String fragment) {
				builder.append("_").append(fragment.toUpperCase());
			}
		};
		
		public void processFirstFragment(StringBuilder builder, String fragment) {
			processFragment(builder, fragment);
		}
		
		public abstract void processFragment(StringBuilder builder, String fragment);
	}

	public static String applyVarNameConventions(String rawVariableName) {
		return applyConvention(Convention.VAR, rawVariableName);
	}

	public static String applyTypeNameConventions(String rawVariableName) {
		return applyConvention(Convention.TYPE, rawVariableName);
	}
	
	public static String applyConstNameConventions(String rawVariableName) {
		return applyConvention(Convention.CONST, rawVariableName);
	}
	
	private static String applyConvention(Convention convention, String rawName) {
		int length = rawName.length();
		if (length == 0) {
			return "v";
		}
		List<String> parsedName = parseName(rawName);
		if (parsedName.isEmpty()) {
			return convertCharsToNames(rawName);
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String fragment : parsedName) {
			if (first && !Character.isJavaIdentifierStart(fragment.charAt(0))) {
				builder.append("n");
			}
			if (first) {
				convention.processFirstFragment(builder, fragment);
			} else {
				convention.processFragment(builder, fragment);
			}
			first = false;
		}
		String result = builder.toString();
		if (!Character.isJavaIdentifierStart(result.charAt(0)) 
				|| getJavaReservedWords().contains(result)) {
			return result + "_";
		}
		return result;
	}
	
	private static String convertCharsToNames(String rawVariableName) {
		StringBuilder builder = new StringBuilder();
		int length = rawVariableName.length();
		for (int i = 0; i < length; i++) {
			char c = rawVariableName.charAt(i);
			if (i == 0) {
				builder.append(getCharacterName(c).toLowerCase());
			} else {
				builder.append(getCharacterName(c));
			}
		}
		return builder.toString();
	}

	private enum State {
		DEFAULT {
			@Override
			public boolean accepts(char c, char next) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean starts(char first, char second) {
				throw new UnsupportedOperationException();
			}
		},
		LOWER_CASE {
			@Override
			public boolean accepts(char c, char next) {
				return Character.isLowerCase(c);
			}

			@Override
			public boolean starts(char first, char second) {
				return Character.isLowerCase(first);
			}
		},
		CAPITALIZED {
			@Override
			public boolean accepts(char c, char next) {
				return Character.isLowerCase(c);
			}

			@Override
			public boolean starts(char first, char second) {
				return Character.isUpperCase(first) 
					&& (Character.isLowerCase(second) || !Character.isLetter(second));
			}
		},
		UPPER_CASE {
			@Override
			public boolean accepts(char c, char next) {
				return Character.isUpperCase(c) && upCaseOrNotLetter(next);
			}

			@Override
			public boolean starts(char first, char second) {
				return Character.isUpperCase(first) && upCaseOrNotLetter(second);
			}

			private boolean upCaseOrNotLetter(char second) {
				return Character.isUpperCase(second) || !Character.isLetter(second);
			}
		},
		DIGITS {
			@Override
			public boolean accepts(char c, char next) {
				return Character.isDigit(c);
			}

			@Override
			public boolean starts(char first, char second) {
				return Character.isDigit(first);
			}
		};
		
		public abstract boolean starts(char first, char second);
		public abstract boolean accepts(char c, char next);
	};

	/*
	 * Parse into equal-case substrings separated by any non-alphanums
	 */
	private static List<String> parseName(String name) {
		int length = name.length();
		if (length == 0) {
			return Collections.emptyList();
		}
		ArrayList<String> result = new ArrayList<String>();
		State state = State.DEFAULT;
		int start = 0;
		int end = 0;
		EnumSet<State> activeStates = EnumSet.allOf(State.class);
		activeStates.remove(State.DEFAULT);
		for (int i = 0; i < length; i++) {
			char c = name.charAt(i);
			char next = (i + 1 < length) ? name.charAt(i + 1) : c; 
			switch (state) {
			case DEFAULT:
				if (Character.isLetterOrDigit(c)) {
					start = i;
					end = i;
					for (State s : activeStates) {
						if (s.starts(c, next)) {
							state = s;
							break;
						} 
					}
				}
				break;
			case LOWER_CASE:
			case UPPER_CASE:
			case CAPITALIZED:
			case DIGITS:
				if (state.accepts(c, next)) {
					end = i;
				} else {
					result.add(name.substring(start, end + 1));
					state = State.DEFAULT;
					i--;
				}
				break;
			}
		}
		if (state != State.DEFAULT) {
			result.add(name.substring(start, end + 1));
		}
		return result;
	}

	public static String upcaseFirst(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}	
	
}
