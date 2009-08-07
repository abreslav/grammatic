package org.abreslav.grammatic.atf.java.antlr.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.atf.java.antlr.semantics.Type;
import org.abreslav.grammatic.parsingutils.ImportManager;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class ImplementationClassGenerator {
	private static final StringTemplateGroup TEMPLATE_IMPL_GROUP = TemplateUtils.loadTemplateGroup("ModuleImplementation");
	private static final StringTemplateGroup POOLS_GROUP = TemplateUtils.loadTemplateGroup("Pools");

	public static void generateModuleImplementationCode(Writer out, ModuleImplementation impl) throws IOException {
		StringTemplate template = TEMPLATE_IMPL_GROUP.getInstanceOf("moduleImplementation");
		template.setAttribute("module", impl);
		writeWithImports(out, impl.getPackage(), template);
	}
	
	public static void generatePoolsImplementationCode(Writer out, ModuleImplementationProvider provider) throws IOException {
		StringTemplate template = POOLS_GROUP.getInstanceOf("main");
		template.setAttribute("provider", provider);
		
		String interfaceFQN = ANTLRGrammarPrinter.getQualifiedName(provider.getProviderInterface());
		List<String> imports = new ArrayList<String>();
		String targetPackage = provider.getPoolsClass().getPackage();
		String interfacePackage = provider.getProviderInterface().getPackage();
		if (!targetPackage.equals(interfacePackage)) {
			imports.add(interfaceFQN);
		}
		List<String> moduleBaseNames = new ArrayList<String>();
		for (ModuleImplementation moduleImplementation : provider.getModuleImplementations()) {
//			imports.add(interfaceFQN + "." + moduleImplementation.getName());
			moduleBaseNames.add(moduleImplementation.getName().substring(1));
		}
		template.setAttribute("imports", imports);
		template.setAttribute("moduleBaseNames", moduleBaseNames);
		out.write(template.toString());
	}
	
	public static void generateImplementationProviderCode(Writer out, ModuleImplementationProvider provider) throws IOException {
		StringTemplate template = TEMPLATE_IMPL_GROUP.getInstanceOf("moduleImplementationProvider");
		template.setAttribute("provider", provider);
		writeWithImports(out, provider.getProviderInterface().getPackage(), template);
	}

	private static void writeWithImports(Writer out, String pack,
			StringTemplate template) throws IOException {
		final ImportManager importManager = new ImportManager(pack);
		template.setAttribute("importMan", new HashMap<Object, Object>() {
			@Override
			public Object get(Object key) {
				Type type = (Type) key;
				return ANTLRGrammarPrinter.getTypeName(type, importManager);
			}
			
			@Override
			public boolean containsKey(Object key) {
				return true;
			}
		});
		
		String string = template.toString();
		out.write("package " + pack + ";\n\n");
		List<String> imports = importManager.getImports();
		for (String imp : imports) {
			out.write("import " + imp + ";\n");
		}
		if (!imports.isEmpty()) {
			out.write("\n");
		}
		out.write(string);
	}

}
