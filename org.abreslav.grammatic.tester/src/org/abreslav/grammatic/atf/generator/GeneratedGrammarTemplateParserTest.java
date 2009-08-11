package org.abreslav.grammatic.atf.generator;

import static org.abreslav.grammatic.grammar.template.parser.CharacterRangeUtils.createCharacter;
import static org.abreslav.grammatic.grammar.template.parser.CharacterRangeUtils.createCharacterRange;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.template.grammarTemplate.AlternativeTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.GrammarTemplateFactory;
import org.abreslav.grammatic.grammar.template.grammarTemplate.LexicalExpressionTemplate;
import org.abreslav.grammatic.grammar.template.grammarTemplate.StringExpressionTemplate;
import org.abreslav.grammatic.grammar1.GrammaticGrammarTemplateLexer;
import org.abreslav.grammatic.grammar1.GrammaticGrammarTemplateParser;
import org.abreslav.grammatic.grammar1.IGrammaticGrammarTemplateModuleImplementationProvider;
import org.abreslav.grammatic.grammar1.IImportsModuleImplementationProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.template.TemplateBody;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;


public class GeneratedGrammarTemplateParserTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception {
		Object[][] data = {
				{"'sadas'", str("sadas")},
				{"'c'", createCharacter('c')},
				{"0x00AF", createCharacter('\u00AF')},
				{".", createCharacterRange('\000', Character.MAX_VALUE)},
				{"[0x0000--'z']", createCharacterRange('\000', 'z')},
				{"['a'--'m' 0x0000--'z']", alt(createCharacterRange('a', 'm'), createCharacterRange('\000', 'z'))},
				{"[^ '0'--'9']", alt(
						createCharacterRange('\000', (char) ('0' - 1)), 
						createCharacterRange((char) ('9' + 1), Character.MAX_VALUE))},
				{"[^ '9']", alt(
						createCharacterRange('\000', (char) ('9' - 1)), 
						createCharacterRange((char) ('9' + 1), Character.MAX_VALUE))},
				{"[^ '0'--'9' 'a'--'z']", alt(
						createCharacterRange('\000', (char) ('0' - 1)), 
						createCharacterRange((char) ('9' + 1), (char) ('a' - 1)), 
						createCharacterRange((char) ('z' + 1), Character.MAX_VALUE))},
		};
		for (Object[] item : data) {
			String string = item[0].toString();
			TemplateBody<? extends Expression> basicLexicalAtom = parseString(string);
			assertTrue(string + ":\n " + item[1] + "\n" + basicLexicalAtom, EMFProxyUtil.equals(basicLexicalAtom, (EObject) item[1]));
		}
	}

	private TemplateBody<? extends Expression> parseString(String data)
			throws IOException, RecognitionException {
		ANTLRReaderStream input = new ANTLRReaderStream(new StringReader(data ));
		GrammaticGrammarTemplateLexer tokenSource = new GrammaticGrammarTemplateLexer(input);
		GrammaticGrammarTemplateParser parser = new GrammaticGrammarTemplateParser(new CommonTokenStream(tokenSource));
		IGrammaticGrammarTemplateModuleImplementationProvider temp = new GrammaticGrammarTemplateModuleImplementationProvider();
		IImportsModuleImplementationProvider imp = new ImportsModuleImplementationProvider();
		IWritableAspect writableAspect = IWritableAspect.NONE;
		parser.setModuleImplementations(
				new GrammaticCharacterModuleImplementationProvider(),
				temp,
				new GrammaticLexicalGrammarModuleImplementationProvider(), 
				new GrammaticMetadataModuleImplementationProvider(writableAspect),
				new BooleanModule(),
				new StringModule(),
				imp);
		TemplateBody<? extends Expression> basicLexicalAtom = parser.basicLexicalAtom();
		return basicLexicalAtom;
	}

	private StringExpressionTemplate str(String str) {
		StringExpressionTemplate result = GrammarTemplateFactory.eINSTANCE.createStringExpressionTemplate();
		result.setValue(str);
		return result;
	}
	
	private LexicalExpressionTemplate alt(TemplateBody<? extends Expression>... templates) {
		AlternativeTemplate res = GrammarTemplateFactory.eINSTANCE.createAlternativeTemplate();
		res.getExpressions().addAll(Arrays.asList(templates));
		LexicalExpressionTemplate template = GrammarTemplateFactory.eINSTANCE.createLexicalExpressionTemplate();
		template.setExpression(res);
		return template;
	}
	
}
