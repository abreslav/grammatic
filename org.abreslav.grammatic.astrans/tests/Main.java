import java.io.File;
import java.io.IOException;

import org.abreslav.grammatic.astrans.EcoreGenerator;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.ecore.EPackage;


public class Main {

	public static void main(String[] args) throws IOException, RecognitionException {
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		Grammar grammar = GrammarParserUtils.parseGrammar(
				"jess.as.grammar", 
				new FileLocator(new File("examples")), 
				AspectWriter.createWritableAspect(aspect), 
				IGrammarLoadHandler.NONE);
		
		EcoreGenerator generator = EcoreGenerator.create();
		EPackage ePackage = generator.generateEcore(grammar, new MetadataProvider(aspect));
		ResourceLoader resourceLoader = new ResourceLoader(".");
		resourceLoader.save("jess.as.ecore", ePackage);
	}
}
