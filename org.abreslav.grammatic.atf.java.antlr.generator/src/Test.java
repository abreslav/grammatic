import java.io.File;
import java.io.IOException;

import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.AntlrPackage;
import org.abreslav.grammatic.atf.java.antlr.generator.ANTLRGrammarPrinter;
import org.abreslav.grammatic.atf.java.antlr.generator.TemplateUtils;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsPackage;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.emf.common.util.EList;


public class Test {

	public static void main(String[] args) throws IOException {
		ResourceLoader loader = new ResourceLoader(new File("../org.abreslav.grammatic.tester/testData/atfJava/antlr"));
		ANTLRGrammar grammar = (ANTLRGrammar) loader.loadStaticModel("ANTLRGrammar.xmi", AntlrPackage.eINSTANCE, SemanticsPackage.eINSTANCE);
		ANTLRGrammarPrinter.printGrammar(grammar, System.out);

		ModuleImplementationProvider moduleProvider = (ModuleImplementationProvider) loader.loadStaticModel("AFactory.xmi", AntlrPackage.eINSTANCE, SemanticsPackage.eINSTANCE);
		ModuleImplementation module = moduleProvider.getModuleImplementations().get(0);
		StringTemplateGroup templateImplGroup = TemplateUtils.loadTemplateGroup("ModuleImplementation");
		StringTemplate template = templateImplGroup.getInstanceOf("main");
		template.setAttribute("package", "a.b.c");
		template.setAttribute("imports", grammar.getImports());
		template.setAttribute("module", module);
		System.out.println(template);
	}
}
