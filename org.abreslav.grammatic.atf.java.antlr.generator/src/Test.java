import java.io.File;
import java.io.IOException;

import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.AntlrPackage;
import org.abreslav.grammatic.atf.java.antlr.generator.ANTLRGrammarPrinter;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsPackage;
import org.abreslav.grammatic.emfutils.ResourceLoader;


public class Test {

	public static void main(String[] args) throws IOException {
		ResourceLoader loader = new ResourceLoader(new File("../org.abreslav.grammatic.tester/testData/atfJava/antlr"));
		ANTLRGrammar grammar = (ANTLRGrammar) loader.loadStaticModel("ANTLRGrammar.xmi", AntlrPackage.eINSTANCE, SemanticsPackage.eINSTANCE);
		ANTLRGrammarPrinter.printGrammar(grammar, System.out);
	}
}
