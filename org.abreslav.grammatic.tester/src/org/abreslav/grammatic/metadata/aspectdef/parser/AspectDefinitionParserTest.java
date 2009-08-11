package org.abreslav.grammatic.metadata.aspectdef.parser;

import java.io.File;
import java.io.FileInputStream;

import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.junit.Assert;
import org.junit.Test;


public class AspectDefinitionParserTest {

	@Test
	public void main() throws Exception {
		String dataDir = "testData/aspectDefinitionParser/";
		AspectDefinition aspectDefinition = AspectDefinitionParser.parseAspectDefinition(new FileInputStream(dataDir + "test.aspect"), null);
		new ResourceLoader(dataDir + "/.").save("result.xmi", aspectDefinition);
		
		Grammar grammar = GrammarParserUtils.parseGrammar("test.grammar",
				new FileLocator(new File(dataDir)),
				IWritableAspect.NONE,
				IGrammarLoadHandler.NONE);

		AspectDefinitionInterpreter.runDefinition(aspectDefinition, grammar, IMetadataProvider.EMPTY, IWritableAspect.NONE, new IErrorHandler<IllegalStateException>() {

			@Override
			public void reportError(String string, Object... objects)
					throws IllegalStateException {
				Assert.fail(String.format(string, objects));
			}
			
		});
	}
}
