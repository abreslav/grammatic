package org.abreslav.grammatic.grammar.aspects;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.parser.AspectDefinitionParser;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class GrammarAspectsTest {

	@Test
	public void test() throws Exception {
		File dataDir = new File("testData/grammarAspects/");
		FileLocator fileLocator = new FileLocator(dataDir);
		final Grammar grammar = GrammarParserUtils.parseGrammar("src.grammar", fileLocator, IWritableAspect.NONE, IGrammarLoadHandler.NONE);
		AspectDefinition gaspect = AspectDefinitionParser.parseAspectDefinition(new FileInputStream(new File(dataDir, "change.gaspect")), fileLocator);
		AspectDefinition test = AspectDefinitionParser.parseAspectDefinition(new FileInputStream(new File(dataDir, "test.aspect")), fileLocator);
		
		GrammarAspectInterpreter.applyGrammarAspect(grammar, gaspect);
		
		AspectDefinitionInterpreter.runDefinition(
				test, 
				grammar, 
				IMetadataProvider.EMPTY, 
				IWritableAspect.NONE, new IErrorHandler<RuntimeException>() {

					@Override
					public void reportError(String messageTemplate,
							Object... objects) throws RuntimeException {
						String name = (String) objects[0];
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!ERROR: " + name);
						printSymbol(grammar, name);
						throw new IllegalArgumentException("Unmatched query: " + name);
					}
		
		});

		
	}
	
	private void printSymbol(Grammar grammar, String name) {
		EList<Symbol> symbols = grammar.getSymbols();
		for (Symbol symbol : new ArrayList<Symbol>(symbols)) {
			if (name.equals(symbol.getName())) {
				new ResourceLoader(".").print(symbol);
			}
		}
	}
}
