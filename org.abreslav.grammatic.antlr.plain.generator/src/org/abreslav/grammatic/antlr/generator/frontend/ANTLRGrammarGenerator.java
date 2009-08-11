package org.abreslav.grammatic.antlr.generator.frontend;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.antlr.generator.ANTLRBuilderCreator;
import org.abreslav.grammatic.antlr.generator.ANTLRGrammarPrinter;
import org.abreslav.grammatic.antlr.generator.BuilderPrinter;
import org.abreslav.grammatic.antlr.generator.GrammaticToANTLR;
import org.abreslav.grammatic.antlr.generator.antlr.ANTLRGrammar;
import org.abreslav.grammatic.antlr.generator.antlr.Rule;
import org.abreslav.grammatic.antlr.generator.antlr.builders.Builder;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuilderFactory;
import org.abreslav.grammatic.antlr.generator.antlr.builders.BuildersFactory;
import org.abreslav.grammatic.antlr.generator.parsers.ImportParser;
import org.abreslav.grammatic.antlr.generator.utils.TemplateUtils;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.parser.GrammarParserUtils;
import org.abreslav.grammatic.grammar.template.parser.IGrammarLoadHandler;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.interpreter.AspectDefinitionInterpreter;
import org.abreslav.grammatic.metadata.aspectdef.parser.AspectDefinitionParser;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.abreslav.grammatic.metadata.aspects.manager.AspectWriter;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.MetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.metadata.util.ValueStream;
import org.abreslav.grammatic.utils.CustomLinkedHashMap;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.printer.Printer;
import org.antlr.runtime.RecognitionException;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;


public class ANTLRGrammarGenerator {

	public static void generate(String grammarName,
			String grammarDir, String generatedGrammarDir,
			String targetPackageDir, Collection<File> grammarImportsPath,
			List<AspectApplication> aspectApplications)
			throws FileNotFoundException, IOException, RecognitionException {
		new File(generatedGrammarDir).mkdirs();
		new File(targetPackageDir).mkdirs();
//		FileInputStream in = new FileInputStream(grammarDir + grammarName + ".grammar");
		MetadataAspect aspect = AspectsFactory.eINSTANCE.createMetadataAspect();
		final IWritableAspect writableAspect = AspectWriter.createWritableAspect(aspect);
		ArrayList<File> importsPath = new ArrayList<File>(grammarImportsPath);
		importsPath.add(new File(grammarDir));
		FileLocator fileLocator = new FileLocator(importsPath);
		final Map<String, Grammar> loadedGrammars = new HashMap<String, Grammar>();;
		Grammar grammar = GrammarParserUtils.parseGrammar(grammarName + ".grammar", fileLocator, writableAspect, new IGrammarLoadHandler() {

			@Override
			public void grammarLoaded(String fileName, Grammar grammar) {
				String grammarName = fileName.substring(0, fileName.length() - ".grammar".length());
				loadedGrammars.put(grammarName, grammar);
			}
			
		});
		System.out.println("Grammar is parsed successfully");

		applyAspects(grammarName, aspectApplications, aspect, writableAspect,
				fileLocator, loadedGrammars, grammar);

		System.out.println("Aspects applied successfully");

		try {
			generateFromLoadedGrammar(grammarName, generatedGrammarDir,
					targetPackageDir, aspect, loadedGrammars.values(), grammar);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private static void applyAspects(String grammarName,
			List<AspectApplication> aspectApplications, MetadataAspect aspect,
			IWritableAspect writableAspect, FileLocator fileLocator, 
			Map<String, Grammar> loadedGrammars, Grammar grammar) throws IOException, RecognitionException,
			FileNotFoundException {
		loadedGrammars.put(grammarName, grammar);
		for (AspectApplication application : aspectApplications) {
			String name = application.getGrammarName();
			System.out.println(name);
			Grammar gr = loadedGrammars.get(name);
			if (gr == null) {
				System.err.println("No grammar " + name + " found!");
			} else {
				File aspectFile = fileLocator.findFile(application.getAspectName() + ".aspect");
				if (aspectFile == null) {
					System.err.println("No aspect " + name + " found!");
				}
				AspectDefinition aspectDefinition = AspectDefinitionParser.parseAspectDefinition(new FileInputStream(aspectFile), fileLocator);
				AspectDefinitionInterpreter.runDefinition(aspectDefinition, gr, new MetadataProvider(aspect), writableAspect, AspectDefinitionInterpreter.SYSERR);
			}
		}
	}

	public static void generateFromLoadedGrammar(String grammarName,
			String generatedGrammarDir, String targetPackageDir,
			MetadataAspect aspect, Collection<Grammar> loadedGrammars, Grammar grammar)
			throws FileNotFoundException, IOException {
		MetadataProvider metadataProvider = new MetadataProvider(aspect);
		CustomLinkedHashMap<Symbol, BuilderFactory> symbolOrigins = getSymbolOrigins(
				loadedGrammars, grammar, metadataProvider);
		
		GrammaticToANTLR grammaticToANTLR = new GrammaticToANTLR(grammar, metadataProvider, symbolOrigins);
		ANTLRGrammar antlrGrammar = grammaticToANTLR.generate();
		
		System.out.println("Grammar is generated successfully");

		Map<Rule, BuilderFactory> ruleOrigins = grammaticToANTLR.getRuleOrigins();
		
		BuilderFactory frontFactory = getBuilderFactory(grammar, metadataProvider);
		Collection<BuilderFactory> builderFactories = new ANTLRBuilderCreator().createBuilders(antlrGrammar, frontFactory , ruleOrigins);

		System.out.println("Builder factories are generated successfully");
		
		StringTemplateGroup group = TemplateUtils.loadTemplateGroup("BuilderPools");

		File dir = new File(targetPackageDir);
		dir.mkdirs();
		for (BuilderFactory builderFactory : builderFactories) {
			writeBuilderFactory(dir, builderFactory);

			writeBuilderPools(group, dir, builderFactory);
		}
		
		System.out.println("Builder factories are printed successfully");

		FileOutputStream grammarFile = new FileOutputStream(generatedGrammarDir + antlrGrammar.getName() + ".g");
		new ANTLRGrammarPrinter(new PrintStream(grammarFile)).printGrammar(antlrGrammar);
		
		System.out.println("Grammar is printed successfully");
	}

	private static void writeBuilderPools(StringTemplateGroup group, File dir,
			BuilderFactory builderFactory) throws FileNotFoundException {
		String factoryName = builderFactory.getFactoryInterfaceName();
		String poolsClassName = builderFactory.getPoolsClassName();
		File filePath = getFilePath(dir, builderFactory, poolsClassName);
		PrintStream printStream = new PrintStream(filePath);
		StringTemplate builderPoolsTemplate = group.getInstanceOf("main");
		builderPoolsTemplate.setAttribute("package", builderFactory.getPackage());
		List<String> imports = new ArrayList<String>();
		String pack = builderFactory.getPackage();
		imports.add((pack == null ? "" : pack + ".") + factoryName + ".*");
		builderPoolsTemplate.setAttribute("imports", imports);
		builderPoolsTemplate.setAttribute("name", poolsClassName);
		builderPoolsTemplate.setAttribute("builderFactory", factoryName);
		List<String> builderTypes = new ArrayList<String>();
		for (Builder builder : builderFactory.getBuilders()) {
			builderTypes.add(builder.getName().substring(1));
		}
		builderPoolsTemplate.setAttribute("builderTypes", builderTypes);
		printStream.print(builderPoolsTemplate.toString());
		printStream.close();
	}

	private static void writeBuilderFactory(File dir,
			BuilderFactory builderFactory) throws FileNotFoundException,
			IOException {
		File filePath = getFactoryFilePath(dir, builderFactory);
		filePath.getParentFile().mkdirs();
		FileOutputStream factoryFile = new FileOutputStream(filePath);
		
		BuilderPrinter builderPrinter = new BuilderPrinter();
		builderPrinter.printBuilderFactoryInterface(builderFactory, new Printer(new PrintStream(factoryFile), "    "));
		
		factoryFile.close();
	}

	private static File getFactoryFilePath(File dir, BuilderFactory builderFactory) {
		return getFilePath(dir, builderFactory, builderFactory.getFactoryInterfaceName());
	}

	private static File getFilePath(File dir, BuilderFactory builderFactory,
			String name) {
		String pack = builderFactory.getPackage();
		if (pack != null) {
			pack = pack.replace('.', '/') + "/";			
		} else {
			pack = "";
		}
		return new File(dir, pack + name + ".java");
	}

	private static CustomLinkedHashMap<Symbol, BuilderFactory> getSymbolOrigins(
			Collection<Grammar> loadedGrammars, Grammar grammar, IMetadataProvider metadataProvider) {
		CustomLinkedHashMap<Symbol, BuilderFactory> symbolOrigins = EMFProxyUtil.customLinkedHashMap();
		for (Grammar imported : loadedGrammars) {
			if (imported == grammar) {
				continue;
			}
			BuilderFactory builderFactory = getBuilderFactory(imported, metadataProvider);
			for (Symbol symbol : imported.getSymbols()) {
				symbolOrigins.put(symbol, builderFactory);
			}
		}
		return symbolOrigins;
	}

	private static BuilderFactory getBuilderFactory(
			Grammar imported, IMetadataProvider metadataProvider) {
		IMetadataStorage metadata = MetadataStorage.getMetadataStorage(imported, metadataProvider);
		List<Value> imports = metadata.readMulti("imports");
		String pack = metadata.readString("package");
		String name = GrammaticToANTLR.getImplicitNameForGrammar(metadata);
		return createBuilderFactory(imports, pack, name);
	}

	private static BuilderFactory createBuilderFactory(List<Value> imports,
			String pack, String name) {
		BuilderFactory factory = BuildersFactory.eINSTANCE.createBuilderFactory();
		factory.setPackage(pack);
		factory.setFactoryInterfaceName(ANTLRBuilderCreator.getFactoryName(name));
		factory.setPoolsClassName(ANTLRBuilderCreator.getPoolsClassName(name));
		if (imports != null) {
			new ImportParser(new ValueStream(imports)).imports(factory.getImports());
		}
		return factory;
	}
	
}
