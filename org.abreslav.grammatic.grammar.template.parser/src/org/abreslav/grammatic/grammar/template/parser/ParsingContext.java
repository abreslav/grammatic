package org.abreslav.grammatic.grammar.template.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.emfutils.EMFCopier;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.template.GrammarTemplateInstantiator;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.aspects.manager.WritableAspect;
import org.abreslav.grammatic.parsingutils.resolve.IProxy;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain.ISubjectStubFactory;
import org.abreslav.grammatic.template.Template;
import org.abreslav.grammatic.template.TemplateBody;
import org.abreslav.grammatic.template.instantiator.IInstantiationHandler;
import org.abreslav.grammatic.template.instantiator.TemplateInstantiatorInterpreter;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.RecognitionException;
import org.eclipse.emf.ecore.EObject;

public class ParsingContext implements IParsingContext {
	
	public static final ISubjectStubFactory<IKey, Symbol> SYMBOL_STUB_FACTORY = new ISubjectStubFactory<IKey, Symbol>() {
		@Override
		public IProxy<Symbol> getSubjectStub(IKey key) {
			return new SymbolProxy();
		}
	};
	
	public static final ISubjectStubFactory<IKey, Template<?>> TEMPLATE_STUB_FACTORY = new ISubjectStubFactory<IKey, Template<?>>() {
		@Override
		@SuppressWarnings("unchecked")
		public IProxy<Template<?>> getSubjectStub(IKey key) {
			return new TemplateProxy();
		}
	};
	
	private final FileLocator myFileLocator;
	private final ResolvingDomain<IKey, Symbol> mySymbolDomain;
	private final IUnresolvedKeysHandler<IKey> myUnresolvedSymbolsHandler;
	private final ResolvingDomain<IKey, Template<?>> myTemplateDomain; 
	private final IUnresolvedKeysHandler<IKey> myUnresolvedTemplatesHandler;
	private final IWritableAspect myWritableAspect; 
	private final WritableAspect myTemplatesMetadata;
	private final Set<String> myLoadedTemlpates = new HashSet<String>();
	private final Map<String, Grammar> myLoadedGrammars = new HashMap<String, Grammar>();
	
	private final IGrammarLoadHandler myLoadHandler;

	public ParsingContext(FileLocator fileLocator, IWritableAspect writableAspect, IGrammarLoadHandler loadHandler) {
		this(fileLocator, writableAspect, THROW_EXCEPTION, THROW_EXCEPTION, loadHandler);
	}
	
	public ParsingContext(
			FileLocator fileLocator, 
			final IWritableAspect writableAspect, 
			IUnresolvedKeysHandler<IKey> unresolvedSymbolsHandler, 
			IUnresolvedKeysHandler<IKey> unresolvedTemplatesHandler,
			IGrammarLoadHandler loadHandler) {
		myFileLocator = fileLocator;
		mySymbolDomain = ResolvingDomain.create(SYMBOL_STUB_FACTORY);
		myTemplateDomain = ResolvingDomain.create(TEMPLATE_STUB_FACTORY);
		myWritableAspect = writableAspect;
		myTemplatesMetadata = new WritableAspect(WritableAspect.EMPTY_WRITER);
		myUnresolvedSymbolsHandler = unresolvedSymbolsHandler;
		myUnresolvedTemplatesHandler = unresolvedTemplatesHandler;

		myLoadHandler = loadHandler;
	}
	
	@Override
	public Symbol getSymbol(IKey name) {
		return mySymbolDomain.getSubjectStub(name);
	}

	@Override
	public void registerSymbol(String moduleName, Symbol symbol) {
		mySymbolDomain.markKeyResolved(DoubleKey.createKey(moduleName, symbol.getName()), symbol);
	}
	
	@Override
	public TemplateInstantiatorInterpreter getInstantiator(String moduleName) {
		SymbolInstantiatorKeyConvertor keyConvertor = new SymbolInstantiatorKeyConvertor(moduleName);
		return GrammarTemplateInstantiator.getInstantiator(
				createInstantiatorHandler(moduleName), 
				LazySymbolReference.createInstantiator(keyConvertor, mySymbolDomain), 
				new EMFCopier.ICopyHandler() {

			@Override
			public void handleCopy(EObject original, EObject copy) {
				for (Attribute attribute : myWritableAspect.getAttributes(original)) {
					// TODO: May be copying would be more efficient
					myWritableAspect.setAttribute(copy, attribute.getNamespace(), attribute.getName(), EMFProxyUtil.copy(attribute.getValue()));
				}
			}
			
		});
	}
	
	private IInstantiationHandler createInstantiatorHandler(final String moduleName) {
		return new IInstantiationHandler() {
			
			@Override
			public <T extends EObject> void handleCreatedObject(
					TemplateBody<? extends T> template, T object) {
				if (object instanceof Symbol) {
					Symbol symbol = (Symbol) object;
					IKey key = DoubleKey.createKey(moduleName, symbol.getName());
					mySymbolDomain.markKeyResolved(key, symbol);
				}
				Collection<Attribute> attributes = myTemplatesMetadata.getAttributes(template);
				for (Attribute attribute : attributes) {
					// TODO: Duplicating object: it will create another attribute :(
					myWritableAspect.setAttribute(object, attribute.getNamespace(), attribute.getName(), EMFProxyUtil.copy(attribute.getValue()));
				}
			}
			
		};
	}
	
	@Override
	public Grammar loadGrammar(String fileName) throws IOException,
			RecognitionException {
		Grammar grammar = myLoadedGrammars.get(fileName);
		// HACK: To avoid further tries of loading (on circular dependency case)
		if (!myLoadedGrammars.containsKey(fileName)) {
			// We may use IProxy, but it's too heavy
			myLoadedGrammars.put(fileName, null);
			grammar = GrammarParser.parseGrammarWithGivenContext(fileName, 
					getFile(fileName), this);
			myLoadedGrammars.put(fileName, grammar);
			myLoadHandler.grammarLoaded(fileName, grammar);
		}
		return grammar; 
	}
	
	@Override
	public void loadTemplates(String fileName) throws IOException,
			RecognitionException {
		if (myLoadedTemlpates.contains(fileName)) {
			return;
		}
		GrammarParser.parseTemplatesWithGivenContext(fileName, 
				getFile(fileName), this);
		myLoadedTemlpates.add(fileName);
	}
	
	private InputStream getFile(String fileName) throws IOException, RecognitionException {
		File file = myFileLocator.findFile(fileName);
		if (file == null) {
			throw new FileNotFoundException("Import not found: " + fileName);
		}
		return new FileInputStream(file.getAbsoluteFile());
	}

	@Override
	public Template<?> getTemplate(IKey name) {
		return myTemplateDomain.getSubjectStub(name);
	}

	@Override
	public void registerTemplate(String moduleName, Template<?> template) {
		myTemplateDomain.markKeyResolved(DoubleKey.createKey(moduleName, template.getName()), template);
	}

	@Override
	public IWritableAspect getWritableAspectForTemplates() {
		return myTemplatesMetadata;
	}
	
	@Override
	public void handleUnresolvedKeys() {
		handleForDomain(mySymbolDomain, myUnresolvedSymbolsHandler, "symbols");
		handleForDomain(myTemplateDomain, myUnresolvedTemplatesHandler, "templates");
	}

	private void handleForDomain(ResolvingDomain<IKey, ?> domain,
			IUnresolvedKeysHandler<IKey> unresolvedKeysHandler,
			String additionalText) {
		Set<IKey> unresolvedKeys = domain.getUnresolvedKeys();
		if (!unresolvedKeys.isEmpty()) {
			unresolvedKeysHandler.handleUnresolvedKeys(unresolvedKeys, additionalText);
		}
	}
	
	/*package*/ ResolvingDomain<IKey, Symbol> getSymbolDomain() {
		return mySymbolDomain;
	}
}
