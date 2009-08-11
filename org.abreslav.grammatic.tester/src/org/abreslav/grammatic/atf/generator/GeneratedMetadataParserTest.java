package org.abreslav.grammatic.atf.generator;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Arrays;

import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.grammar.template.parser.IParsingContext;
import org.abreslav.grammatic.grammar.template.parser.ParsingContext;
import org.abreslav.grammatic.grammar1.GrammaticMetadataLexer;
import org.abreslav.grammatic.grammar1.GrammaticMetadataParser;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;


public class GeneratedMetadataParserTest {

	@Test
	public void test() throws Exception {
		String data = "{a = b; b = 0x000A; c = 5; d = {a; b; c;}; e = {{ d - 6 }}; f = 'sdfsd' }";
		ANTLRReaderStream input = new ANTLRReaderStream(new StringReader(data ));
		GrammaticMetadataLexer tokenSource = new GrammaticMetadataLexer(input);
		GrammaticMetadataParser parser = new GrammaticMetadataParser(new CommonTokenStream(tokenSource));
		IParsingContext parsingContext = new ParsingContext(GrammarParserATF.INSTANCE, null, 
				IWritableAspect.ERROR, IGrammarLoadHandler.NONE);
		parser.setModuleImplementations(new GrammaticCharacterModuleImplementationProvider(),
				new GrammaticGrammarTemplateModuleImplementationProvider("<none>", parsingContext),
				new GrammaticLexicalGrammarModuleImplementationProvider(),
				new GrammaticMetadataModuleImplementationProvider(parsingContext),
				new BooleanModule(),
				new StringModule());
		TupleValue attributeList = parser.attributeList();
		assertTrue(EcoreUtil.equals(attributeList,
				tuple(
						att("a", id("b")),
						att("b", ch((char) 10)),
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
