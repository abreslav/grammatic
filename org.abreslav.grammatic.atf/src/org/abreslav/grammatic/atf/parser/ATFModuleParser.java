package org.abreslav.grammatic.atf.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.abreslav.grammatic.metadata.aspectdef.GrammarAssignment;
import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.antlr.runtime.RecognitionException;

public class ATFModuleParser implements IATFParser {

	private final FileLocator myFileLocator;
	private final ITypeSystemBuilder<?> myTypeSystemBuilder;
	private final IATFParserImplementationFactory myParserImplementationFactory;
	private final IErrorHandler<RuntimeException> myErrorHandler = IErrorHandler.EXCEPTION;
	private final Map<String, SemanticModule> mySemanticModules = new HashMap<String, SemanticModule>();
	private final Set<String> myTypeSystemModules = new HashSet<String>();
	private final Map<SemanticModule, SemanticModuleDescriptor> mySemanticModuleDescriptors = new HashMap<SemanticModule, SemanticModuleDescriptor>();
	
	public ATFModuleParser(ITypeSystemBuilder<?> typeSystemBuilder,
			IATFParserImplementationFactory parserImplementationFactory,
			FileLocator fileLocator) {
		myTypeSystemBuilder = typeSystemBuilder;
		myParserImplementationFactory = parserImplementationFactory;
		myFileLocator = fileLocator;
	}

	private IATFParserImplementation configureParser(String moduleName) throws IOException {
		File file = myFileLocator.findFile(moduleName);
		if (file == null) {
			myErrorHandler.reportError("Module not found: %s", moduleName);
		}
		return myParserImplementationFactory.createParserImplementation(this, file, myTypeSystemBuilder, myFileLocator, myErrorHandler);
	}
	
	@Override
	public AspectDefinition loadATFModule(String moduleName) {
		IATFParserImplementation parser = null;
		try {
			parser = configureParser(moduleName);
			IOptions options = new Options();
			myTypeSystemBuilder.openModule(moduleName, options);
			AspectDefinition atfModule = parser.atfModule();
			addOptions(atfModule, options.getOptions());
			myTypeSystemBuilder.closeModule(moduleName);
			return atfModule;
		} catch (IOException e) {
			myErrorHandler.reportError("Loading ATF module %s: %s", moduleName, e.getMessage());
		} catch (RecognitionException e) {
			myErrorHandler.reportError("Parsing ATF module %s: %s", moduleName, e.getMessage());
		} catch (RuntimeException e) {
			String position = parser.getPositionString();
			throw new IllegalArgumentException(String.format("%s: %s", position, e.getMessage()), e);
		}
		throw new IllegalStateException();
	}

	@Override
	public SemanticModule loadSemanticModule(String moduleName) {
		SemanticModule semanticModule = mySemanticModules.get(moduleName);
		if (semanticModule != null) {
			return semanticModule;
		}
		IATFParserImplementation parser = null;
		try {
			parser = configureParser(moduleName);
			IOptions options = new Options();
			myTypeSystemBuilder.openModule(moduleName, options);
			SemanticModule semanticModuleDeclaration = parser.semanticModule();
			myTypeSystemBuilder.closeModule(moduleName);
			mySemanticModules.put(moduleName, semanticModuleDeclaration);
			SemanticModuleDescriptor descriptor = new SemanticModuleDescriptor(moduleName, semanticModuleDeclaration, options.getOptions());
			mySemanticModuleDescriptors.put(semanticModuleDeclaration, descriptor);
			return semanticModuleDeclaration;
		} catch (IOException e) {
			myErrorHandler.reportError("Loading Semantic module %s: %s", moduleName, e.getMessage());
		} catch (RecognitionException e) {
			myErrorHandler.reportError("Parsing Semantic module %s: %s", moduleName, e.getMessage());
		} catch (RuntimeException e) {
			String position = parser.getPositionString();
			throw new IllegalArgumentException(String.format("%s: %s", position, e.getMessage()), e);
		}
		throw new IllegalStateException();
	}

	@Override
	public void loadTypeSystemModule(String moduleName) {
		if (myTypeSystemModules.contains(moduleName)) {
			myTypeSystemBuilder.loadTypeSystemModule(moduleName);
			return;
		}
		myTypeSystemModules.add(moduleName);
		IATFParserImplementation parser = null;
		try {
			parser = configureParser(moduleName);
			IOptions options = new Options();
			myTypeSystemBuilder.openModule(moduleName, options);
			parser.typeSystemModule();
			myTypeSystemBuilder.closeModule(moduleName);
			myTypeSystemBuilder.loadTypeSystemModule(moduleName);
		} catch (IOException e) {
			myErrorHandler.reportError("Loading Semantic module %s: %s", moduleName, e.getMessage());
		} catch (RecognitionException e) {
			myErrorHandler.reportError("Parsing Semantic module %s: %s", moduleName, e.getMessage());
		} catch (RuntimeException e) {
			String position = parser.getPositionString();
			throw new IllegalArgumentException(String.format("%s: %s", position, e.getMessage()), e);
		}
	}
	
	private void addOptions(AspectDefinition atfModule, Map<String, String> options) {
		GrammarAssignment grammarAssignment = AspectDefinitionUtils.getGrammarAssignment(atfModule);
		Attribute attribute = AspectDefinitionUtils.getAttribute(grammarAssignment, ATFMetadata.ATF_NAMESPACE, "typeSystemOptions");
		TupleValue value = (TupleValue) attribute.getValue();
		if (value == null) {
			value = MetadataFactory.eINSTANCE.createTupleValue();
		}
		for (Entry<String, String> entry : options.entrySet()) {
			Attribute option = AspectDefinitionUtils.getAttribute(value, ATFMetadata.ATF_NAMESPACE, entry.getKey());
			StringValue string = MetadataFactory.eINSTANCE.createStringValue();
			string.setValue(entry.getValue());
			option.setValue(string);
		}
	}
}
