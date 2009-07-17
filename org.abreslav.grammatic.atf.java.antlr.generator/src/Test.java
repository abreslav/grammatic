import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.java.antlr.ANTLRGrammar;
import org.abreslav.grammatic.atf.java.antlr.AntlrPackage;
import org.abreslav.grammatic.atf.java.antlr.generator.ANTLRGrammarPrinter;
import org.abreslav.grammatic.atf.java.antlr.generator.ModuleImplementationBuilder;
import org.abreslav.grammatic.atf.java.antlr.generator.TemplateUtils;
import org.abreslav.grammatic.atf.java.antlr.semantics.Import;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.SemanticsPackage;
import org.abreslav.grammatic.atf.java.parser.ATFJavaModuleLoader;
import org.abreslav.grammatic.atf.parser.SemanticModuleDescriptor;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.emf.common.util.EList;


public class Test {

	public static void main(String[] args) throws IOException {
		ResourceLoader loader = new ResourceLoader(new File("../org.abreslav.grammatic.tester/testData/atfJava/antlr"));
		ANTLRGrammar grammar = (ANTLRGrammar) loader.loadStaticModel("ANTLRGrammar.xmi", AntlrPackage.eINSTANCE, SemanticsPackage.eINSTANCE);
		ANTLRGrammarPrinter.printGrammar(grammar, System.out);

		String path = "../org.abreslav.grammatic.tester/testData/atf/interpreter/java";
		File baseDir = new File(path);
		ATFJavaModuleLoader moduleLoader = new ATFJavaModuleLoader(new FileLocator(baseDir));
		File[] listFiles = baseDir.listFiles();
		for (File file : listFiles) {
			String name = file.getName();
			if (name.endsWith(".atf")) {
				long time = System.currentTimeMillis();
				moduleLoader.loadATFModule(name);
				time = System.currentTimeMillis() - time;
				System.out.println(time);
			}
		}
		Collection<SemanticModuleDescriptor> semanticModuleDescriptors = moduleLoader.getSemanticModuleDescriptors();
		StringTemplateGroup templateImplGroup = TemplateUtils.loadTemplateGroup("ModuleImplementation");
		for (SemanticModuleDescriptor semanticModuleDescriptor : semanticModuleDescriptors) {
			Map<String, Object> options = semanticModuleDescriptor.getOptions();
			SemanticModule module = semanticModuleDescriptor.getSemanticModule();
			String code = generateCodeForSemanticModule(
					templateImplGroup, 
					(String) options.get("package"), 
					Collections.<Import>emptySet(), 
					ModuleImplementationBuilder.INSTANCE.buildModuleImplementation(module));
			System.out.println(code);
		}
		
		System.out.println("Over");
		
		ModuleImplementationProvider moduleProvider = (ModuleImplementationProvider) loader.loadStaticModel("AFactory.xmi", AntlrPackage.eINSTANCE, SemanticsPackage.eINSTANCE);
		ModuleImplementation module = moduleProvider.getModuleImplementations().get(0);
		String pack = "a.b.c";
		EList<Import> imports = grammar.getImports();
		String code = generateCodeForSemanticModule(templateImplGroup, pack,
				imports, module);
		System.out.println(code);
	}

	private static String generateCodeForSemanticModule(
			StringTemplateGroup templateImplGroup, String pack,
			Collection<Import> imports, ModuleImplementation module) {
		StringTemplate template = templateImplGroup.getInstanceOf("main");
		template.setAttribute("package", pack);
		template.setAttribute("imports", imports);
		template.setAttribute("module", module);
		String code = template.toString();
		return code;
	}
}
