package org.abreslav.grammatic.grammar.template.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.parsingutils.resolve.ResolvingDomain;
import org.abreslav.grammatic.utils.FileLocator;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class GrammarParser {
	public static Grammar parseGrammar(String mainGrammar,
			FileLocator fileLocator, IWritableAspect writableAspect, IGrammarLoadHandler loadHandler) throws IOException,
			RecognitionException {
		IParsingContext parsingContext = new ParsingContext(fileLocator, writableAspect, loadHandler);
		
		Grammar grammar = parsingContext.loadGrammar(mainGrammar);
		
		parsingContext.handleUnresolvedKeys();
		
		return grammar;
	}
	
	public static Expression parseExpressionFromString(String string, Map<String, Symbol> symbols, IWritableAspect writableAspect) throws RecognitionException {
		ParsingContext parsingContext = new ParsingContext(null, writableAspect, IGrammarLoadHandler.NONE);
		try {
			GrammaticGrammarTemplateParser parser = configureParser("", new ByteArrayInputStream(string.getBytes()), 
					parsingContext);
			Expression expression = parser.expression();
			ResolvingDomain<IKey, Symbol> symbolDomain = parsingContext.getSymbolDomain();
			Set<IKey> unresolvedKeys = symbolDomain.getUnresolvedKeys();
			for (IKey key : unresolvedKeys) {
				Symbol s = GrammarFactory.eINSTANCE.createSymbol();
				s.setName(key.getSimpleName());
				symbols.put(key.getSimpleName(), s);
				symbolDomain.markKeyResolved(key, s);
			}
			
			return expression;			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
	}

	/*package*/ static Grammar parseGrammarWithGivenContext(String moduleName, InputStream input,
			IParsingContext parsingContext) throws IOException,
			RecognitionException {
		GrammaticGrammarTemplateParser parser = configureParser(moduleName, input,
				parsingContext);
		Grammar grammar = parser.grammar_();
		return grammar;
	}

	/*package*/ static void parseTemplatesWithGivenContext(String moduleName, InputStream input, IParsingContext parsingContext) throws IOException,
			RecognitionException {
		GrammaticGrammarTemplateParser parser = configureParser(moduleName, input,
				parsingContext);
		parser.templateLibrary();
	}
	
	private static GrammaticGrammarTemplateParser configureParser(
			String moduleName,
			InputStream input, 
			IParsingContext parsingContext)
			throws IOException {
		ANTLRInputStream in = new ANTLRInputStream(input);
		GrammaticGrammarTemplateLexer lexer = new GrammaticGrammarTemplateLexer(
				in);
		GrammaticGrammarTemplateParser parser = new GrammaticGrammarTemplateParser(
				new CommonTokenStream(lexer));

		GrammaticGrammarTemplateBuilders grammarBuilders = new GrammaticGrammarTemplateBuilders(moduleName, parsingContext);
		IGrammaticCharacterBuilders characterBuilders = new GrammaticCharacterBuilders();
		GrammaticLexicalGrammarBuilders lexicalGrammarBuilders = new GrammaticLexicalGrammarBuilders();
		IGrammaticMetadataBuilders metadataBuilders = new GrammaticMetadataBuilders(parsingContext);
		parser.setBuilders(
				wrapByCounter(characterBuilders),
				wrapByCounter(grammarBuilders),
				wrapByCounter(lexicalGrammarBuilders),
				metadataBuilders,
				grammarBuilders.getImportsBuilders()
		);
		return parser;
	}

//	@SuppressWarnings("unchecked")
	private static <T> T wrapByCounter(final T object) {
		return object;
/*		
		Class<? extends Object> clazz = object.getClass();
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				String name = method.getName();
				Integer counter = myCounters.get(name);
				if (counter == null) {
					counter = 0;
				}
				counter++;
				myCounters.put(name, counter);
				return method.invoke(object, args);
			}
			
		});
//*/
	}
	
}
