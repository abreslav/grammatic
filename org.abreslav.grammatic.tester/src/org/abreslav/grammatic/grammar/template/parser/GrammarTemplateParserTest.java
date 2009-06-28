package org.abreslav.grammatic.grammar.template.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.parser.AspectDefinitionParser;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.query.tester.MyParameterized;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

@RunWith(MyParameterized.class)
public class GrammarTemplateParserTest {

	@Parameters
	public static Collection<Object[]> parameters() throws FileNotFoundException, IOException, RecognitionException {
		File testDir = new File("testData/grammarTemplateParser");
		File[] listFiles = testDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".aspect");
			}
			
		});
		List<Object[]> result = new ArrayList<Object[]>();
		for (File file : listFiles) {
			String grammarName = file.getName().replace(".aspect", ".grammar");
			FileLocator fileLocator = new FileLocator(testDir);
			MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
			IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
			Grammar grammar = GrammarParser.parseGrammar(
					grammarName, 
					fileLocator, 
					writableAspect,
					IGrammarLoadHandler.NONE);
			AspectDefinition aspectDefinition = AspectDefinitionParser.parseAspectDefinition(new FileInputStream(file), null);
			result.add(new Object[] {
				file.getName(),
				aspectDefinition,
				grammar,
				aspect,
			});
		}
		return result;
	}
	
	private final Grammar myGrammar;
	private final AspectDefinition myAspectDefinition;
	private final MetadataAspect myAspect;

	public GrammarTemplateParserTest(AspectDefinition aspectDefinition,
			Grammar grammar, MetadataAspect aspect) {
		super();
		myAspectDefinition = aspectDefinition;
		myGrammar = grammar;
		myAspect = aspect;
	}

	@Test
	public void test() throws Exception {
		try {
			AspectDefinitionInterpreter.runDefinition(myAspectDefinition, myGrammar, 
					new MetadataProvider(myAspect), 
					IWritableAspect.NONE, 
					IErrorHandler.EXCEPTION);
		} catch (Exception e) {
			new ResourceLoader(".").print(myGrammar);
			System.out.println("--------------------");
			new ResourceLoader(".").print(myAspect);
			System.out.println("--------------------");
			new ResourceLoader(".").print(myAspectDefinition);
			throw e;
		}
	}
}
