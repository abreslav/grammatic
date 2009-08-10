package org.abreslav.grammatic.atf.generator;

import static org.abreslav.grammatic.grammar.template.parser.CharacterRangeUtils.createCharacter;
import static org.abreslav.grammatic.grammar.template.parser.CharacterRangeUtils.createCharacterRange;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
		fail("Do it!");
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
		IGrammaticGrammarTemplateModuleImplementationProvider temp = new IGrammaticGrammarTemplateModuleImplementationProvider() {
			
			@Override
			public ITypeFunctions getTypeFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateParametersFunctions getTemplateParametersFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateParameterReferenceFunctions getTemplateParameterReferenceFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateParameterFunctions getTemplateParameterFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateLibraryFunctions getTemplateLibraryFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateDeclarationFunctions getTemplateDeclarationFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateBodyFunctions getTemplateBodyFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateArgumentsFunctions getTemplateArgumentsFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateArgumentFunctions getTemplateArgumentFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplateApplicationFunctions getTemplateApplicationFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ISequenceFunctions getSequenceFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IRuleFunctions getRuleFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IProductionTemplateFunctions getProductionTemplateFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IProductionFunctions getProductionFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IOperationFunctions getOperationFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ILexicalAtomIndependentFunctions getLexicalAtomIndependentFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IIterationFunctions getIterationFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IGrammarFunctions getGrammarFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IExpressionFunctions getExpressionFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IDefaultTemplateFunctions getDefaultTemplateFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAtomFunctions getAtomFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAlternativeFunctions getAlternativeFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		IImportsModuleImplementationProvider imp = new IImportsModuleImplementationProvider() {
			
			@Override
			public IRenamingFunctions getRenamingFunctions() {
				// TODO Auto-generated method stub
				return null;
			}
		};
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
