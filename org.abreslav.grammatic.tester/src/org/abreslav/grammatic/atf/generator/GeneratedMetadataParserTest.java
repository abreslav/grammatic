package org.abreslav.grammatic.atf.generator;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.grammar1.GrammaticMetadataLexer;
import org.abreslav.grammatic.grammar1.GrammaticMetadataParser;
import org.abreslav.grammatic.grammar1.IGrammaticCharacterModuleImplementationProvider;
import org.abreslav.grammatic.grammar1.IGrammaticMetadataModuleImplementationProvider;
import org.abreslav.grammatic.grammar1.IString;
import org.abreslav.grammatic.metadata.Annotated;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;


public class GeneratedMetadataParserTest {

	private final class StringModule implements IString {
		@Override
		public String createString(String token) {
			return token.substring(1, token.length() - 1);
		}
	}

	private final class GrammaticCharacterModuleImplementationProvider
			implements IGrammaticCharacterModuleImplementationProvider {
		@Override
		public ICharacterFunctions getCharacterFunctions() {
			return new ICharacterFunctions() {
				
				@Override
				public char getCharacter(String character) {
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
				public char getCharByCode(String code) {
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

	private final class GrammaticMetadataModuleImplementationProvider implements
			IGrammaticMetadataModuleImplementationProvider {
		private final Map<String, Namespace> myNamespaces = new HashMap<String, Namespace>();
		private final IWritableAspect myWritableAspect;

		public GrammaticMetadataModuleImplementationProvider(
				IWritableAspect writableAspect) {
			myWritableAspect = writableAspect;
		}

		@Override
		public ITermFunctions getTermFunctions() {
			return new ITermFunctions() {
				
				@Override
				public Value createPunctuationValue(PunctuationType punctuation) {
					PunctuationValue value = MetadataFactory.eINSTANCE.createPunctuationValue();
					value.setType(punctuation);
					return value;
				}
			};
		}

		@Override
		public IPunctuationFunctions getPunctuationFunctions() {
			return new IPunctuationFunctions() {
				
				@Override
				public PunctuationType punctuationType(String textTokens) {
					PunctuationType v;
					switch (textTokens.charAt(0)) {
					case '`':
						v = PunctuationType.GRAVE_ACCENT;
						break;
					case '~':
						v = PunctuationType.TILDE;
						break;
					case '!':
						v = PunctuationType.EXCLAMATION;
						break;
					case '@':
						v = PunctuationType.AT;
						break;
					case '#':
						v = PunctuationType.HASH;
						break;
					case '$':
						v = PunctuationType.DOLLAR;
						break;
					case '%':
						v = PunctuationType.PERCENT;
						break;
					case '^':
						v = PunctuationType.CIRCUMFLEX;
						break;
					case '&':
						v = PunctuationType.AMPERSANT;
						break;
					case '*':
						v = PunctuationType.ASTERISK;
						break;
					case '(':
						v = PunctuationType.LEFT_BRACE;
						break;
					case ')':
						v = PunctuationType.RIGHT_BRACE;
						break;
					case '-':
						v = PunctuationType.MINUS;
						break;
					case '+':
						v = PunctuationType.PLUS;
						break;
					case '=':
						v = PunctuationType.EQUALS;
						break;
					case '|':
						v = PunctuationType.BAR;
						break;
					case '\\':
						v = PunctuationType.BACKSLASH;
						break;
					case '[':
						v = PunctuationType.LEFT_BRACKET;
						break;
					case ']':
						v = PunctuationType.RIGHT_BRACKET;
						break;
					case ';':
						v = PunctuationType.SEMICOLON;
						break;
					case ':':
						v = PunctuationType.COLON;
						break;
					case ',':
						v = PunctuationType.COMMA;
						break;
					case '.':
						v = PunctuationType.DOT;
						break;
					case '/':
						v = PunctuationType.SLASH;
						break;
					case '?':
						v = PunctuationType.QUESTION;
						break;
					case '<':
						v = PunctuationType.LESS;
						break;
					case '>':
						v = PunctuationType.GREATER;
						break;
					case '{':
						v = PunctuationType.LEFT_CURLY;
						break;
					case '}':
						v = PunctuationType.RIGHT_CURLY;
						break;
					default:
						throw new IllegalArgumentException();
					}
					return v;
				}
			};
		}

		@Override
		public INamespaceFunctions getNamespaceFunctions() {
			return new INamespaceFunctions() {
				
				@Override
				public Namespace createNamespace(String uri) {
					Namespace ns = myNamespaces.get(uri);
					if (ns == null) {
						ns = MetadataFactory.eINSTANCE.createNamespace();
						ns.setUri(uri);
						myNamespaces.put(uri, ns);
					}
					return ns;
				}
			};
		}

		@Override
		public ILocal getLocal() {
			return new ILocal() {
				
				@Override
				public Value valueNull() {
					return null;
				}
			};
		}

		@Override
		public IAttributeValueFunctions getAttributeValueFunctions() {
			return new IAttributeValueFunctions() {
				
				@Override
				public Value createStringValue(String string) {
					StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
					sv.setValue(string);
					return sv;
				}
				
				@Override
				public MultiValue createMultiValue() {
					return MetadataFactory.eINSTANCE.createMultiValue();
				}
				
				@Override
				public Value createIntValue(String int1) {
					IntegerValue intv = MetadataFactory.eINSTANCE.createIntegerValue();
					intv.setValue(Integer.valueOf(int1));
					return intv;
				}
				
				@Override
				public Value createIdValue(String name) {
					IdValue idv = MetadataFactory.eINSTANCE.createIdValue();
					idv.setId(name);
					return idv;
				}
				
				@Override
				public Value createCharacterValue(char character) {
					StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
					sv.setValue(character + "");
					return sv;
				}
				
				@Override
				public void addItem(MultiValue multi, Value term) {
					multi.getValues().add(term);
				}
			};
		}

		@Override
		public IAttributeListFunctions getAttributeListFunctions() {
			return new IAttributeListFunctions() {
				
				@Override
				public TupleValue createTupleValue() {
					return MetadataFactory.eINSTANCE.createTupleValue();
				}
			};
		}

		@Override
		public IAttributeInListFunctions getAttributeInListFunctions() {
			return new IAttributeInListFunctions() {
				
				@Override
				public void addAttribute(Annotated subject, Namespace namespace,
						String name, Value attributeValue) {
					Attribute attr = MetadataFactory.eINSTANCE.createAttribute();
					attr.setName(name);
					attr.setNamespace(namespace);
					attr.setValue(attributeValue);
					subject.getAttributes().add(attr);
				}
			};
		}

		@Override
		public IAttributeFunctions1 getAttributeFunctions1() {
			return new IAttributeFunctions1() {
				
				@Override
				public void writeAttribute(EObject subject, Namespace namespace,
						String name, Value attributeValue) {
					myWritableAspect.setAttribute(subject, namespace, name, attributeValue);
				}
			};
		}

		@Override
		public IAttributeFunctions getAttributeFunctions() {
			return new IAttributeFunctions() {
				
				@Override
				public Namespace nsNull() {
					return null;
				}
			};
		}
	}

	@Test
	public void test() throws Exception {
		String data = "{a = b; b = 'c'; c = 5; d = {a; b; c;}; e = {{ d - 6 }}; f = 'sdfsd' }";
		ANTLRReaderStream input = new ANTLRReaderStream(new StringReader(data ));
		GrammaticMetadataLexer tokenSource = new GrammaticMetadataLexer(input);
		GrammaticMetadataParser parser = new GrammaticMetadataParser(new CommonTokenStream(tokenSource));
		parser.setModuleImplementations(new GrammaticCharacterModuleImplementationProvider(), 
				new GrammaticMetadataModuleImplementationProvider(IWritableAspect.NONE), 
				new StringModule());
		TupleValue attributeList = parser.attributeList();
		assertTrue(EcoreUtil.equals(attributeList,
				tuple(
						att("a", id("b")),
						att("b", ch('c')),
						att("c", int_(5)),
						att("d", tuple(att("a"), att("b"), att("c"))),
						att("e", multi(id("d"), punct("-"), int_(6))),
						att("f", str("sdfsd"))
				)));
	}
	
	private TupleValue tuple(Attribute... attributes) {
		TupleValue result = MetadataFactory.eINSTANCE.createTupleValue();
		result.getAttributes().addAll(Arrays.asList(attributes));
		return result;
	}
	
	public Value str(String string) {
		StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
		sv.setValue(string);
		return sv;
	}
	
	public MultiValue multi(Value... values) {
		MultiValue result = MetadataFactory.eINSTANCE.createMultiValue();
		result.getValues().addAll(Arrays.asList(values));
		return result;
	}
	
	public Value int_(int int1) {
		IntegerValue intv = MetadataFactory.eINSTANCE.createIntegerValue();
		intv.setValue(int1);
		return intv;
	}
	
	public Value id(String name) {
		IdValue idv = MetadataFactory.eINSTANCE.createIdValue();
		idv.setId(name);
		return idv;
	}
	
	public Value ch(char character) {
		StringValue sv = MetadataFactory.eINSTANCE.createStringValue();
		sv.setValue(character + "");
		return sv;
	}
	
	public Attribute att(String name, Value value) {
		Attribute attr = MetadataFactory.eINSTANCE.createAttribute();
		attr.setName(name);
		attr.setValue(value);
		return attr;
	}
	
	public Attribute att(String name) {
		Attribute attr = MetadataFactory.eINSTANCE.createAttribute();
		attr.setName(name);
		return attr;
	}
	
	public PunctuationValue punct(String textTokens) {
		PunctuationType v;
		switch (textTokens.charAt(0)) {
		case '`':
			v = PunctuationType.GRAVE_ACCENT;
			break;
		case '~':
			v = PunctuationType.TILDE;
			break;
		case '!':
			v = PunctuationType.EXCLAMATION;
			break;
		case '@':
			v = PunctuationType.AT;
			break;
		case '#':
			v = PunctuationType.HASH;
			break;
		case '$':
			v = PunctuationType.DOLLAR;
			break;
		case '%':
			v = PunctuationType.PERCENT;
			break;
		case '^':
			v = PunctuationType.CIRCUMFLEX;
			break;
		case '&':
			v = PunctuationType.AMPERSANT;
			break;
		case '*':
			v = PunctuationType.ASTERISK;
			break;
		case '(':
			v = PunctuationType.LEFT_BRACE;
			break;
		case ')':
			v = PunctuationType.RIGHT_BRACE;
			break;
		case '-':
			v = PunctuationType.MINUS;
			break;
		case '+':
			v = PunctuationType.PLUS;
			break;
		case '=':
			v = PunctuationType.EQUALS;
			break;
		case '|':
			v = PunctuationType.BAR;
			break;
		case '\\':
			v = PunctuationType.BACKSLASH;
			break;
		case '[':
			v = PunctuationType.LEFT_BRACKET;
			break;
		case ']':
			v = PunctuationType.RIGHT_BRACKET;
			break;
		case ';':
			v = PunctuationType.SEMICOLON;
			break;
		case ':':
			v = PunctuationType.COLON;
			break;
		case ',':
			v = PunctuationType.COMMA;
			break;
		case '.':
			v = PunctuationType.DOT;
			break;
		case '/':
			v = PunctuationType.SLASH;
			break;
		case '?':
			v = PunctuationType.QUESTION;
			break;
		case '<':
			v = PunctuationType.LESS;
			break;
		case '>':
			v = PunctuationType.GREATER;
			break;
		case '{':
			v = PunctuationType.LEFT_CURLY;
			break;
		case '}':
			v = PunctuationType.RIGHT_CURLY;
			break;
		default:
			throw new IllegalArgumentException();
		}
		PunctuationValue value = MetadataFactory.eINSTANCE.createPunctuationValue();
		value.setType(v);
		return value;
	}

}
