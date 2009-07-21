package org.abreslav.grammatic.atf.interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Combination;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.LexicalExpression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.AttributeValue;
import org.abreslav.grammatic.metadata.CrossReferenceValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class ATFPostProcessor<E extends RuntimeException> {

	private final Map<Symbol, IMetadataStorage> myStorageCache = EMFProxyUtil.customHashMap();
	private Object myStringType;
	private IMetadataProvider myRootMetadataProvider;
	private IWritableAspect myWritableAspect;
	private IErrorHandler<E> myErrorHandler;

	private final ExpressionTraverser myLexicalityChecker = new ExpressionTraverser() {
		@Override
		public INull caseSymbolReference(SymbolReference object) {
			Symbol symbol = object.getSymbol();
			if (!isToken(symbol)) {
				myErrorHandler.reportError("Must be a token reference: '%s'", symbol.getName());
			}
			markAssociatedWithToken(ATFMetadata.ATF_NAMESPACE, object);
			return INull.NULL;
		}
		
		@Override
		public INull caseLexicalExpression(LexicalExpression object) {
			return doSwitch(object.getExpression());
		}
		
		@Override
		public INull caseLexicalDefinition(LexicalDefinition object) {
			return INull.NULL; // It's OK to be a lexical definition
		}
	};

	public void process(Grammar grammar, 
			Object stringType,
			IMetadataProvider metadataProvider, 
			IWritableAspect writableAspect, 
			IErrorHandler<E> errorHandler) throws E {
		myStringType = stringType;
		myRootMetadataProvider = metadataProvider;
		myWritableAspect = writableAspect;
		myErrorHandler = errorHandler;
		for (Symbol symbol : grammar.getSymbols()) {
			if (isToken(symbol)) {
				processLexicalRule(symbol);
			} else {
				processSyntacticalRule(symbol);
			}			
		}
	}

	private void processSyntacticalRule(Symbol symbol) {
		assertAbsence(symbol, "when no '" + ATFMetadata.TOKEN + "' present", ATFMetadata.TOKEN_CLASSES);
		IMetadataStorage metadata = getSymbolMetadataStorage(symbol);
		String symbolName = symbol.getName();
		if (metadata.isPresent(ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION)) {
			Namespace defaultNamespace = getDefaultNamespace(symbol);
			processSyntacticFunction(symbol, defaultNamespace, symbolName);
			
			// Convenient to process default and ordinary functions uniformly
			Map<String, Namespace> functionNameToNamespace = getOrCreateMap(symbol, metadata, ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
			functionNameToNamespace.put(symbolName, defaultNamespace);
			Map<String, FunctionSignature> functionNameToFunction = getOrCreateMap(symbol, metadata, ATFMetadata.FUNCTION_NAME_TO_FUNCTION);
			functionNameToFunction.put(symbolName, (FunctionSignature) metadata.readEObject(ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION));
		} else {
			assertAbsence(symbol, "when no '" + ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION + "' present", ATFMetadata.DEFAULT_NAMESPACE);
			if (!metadata.isPresent(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE)) {
				myErrorHandler.reportError("No ATF data for symbol '%s'", symbolName);
			}
			Map<String, Namespace> functionNameToNamespace = getFunctionNameToNamespace(metadata);
			for (Namespace namespace : functionNameToNamespace.values()) {
				processSyntacticFunction(symbol, namespace, null);
			}
		}
	}

	private <T> Map<String, T> getOrCreateMap(Symbol symbol,
			IMetadataStorage metadata, String attributeName) {
		@SuppressWarnings("unchecked")
		Map<String, T> map = (Map<String, T>) metadata.readObject(attributeName);
		if (map == null) {
			map = new HashMap<String, T>();
			AttributeValue value = MetadataFactory.eINSTANCE.createAttributeValue();
			value.getValues().add(map);
			myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, attributeName, value);
		}
		return map;
	}

	private void processSyntacticFunction(Symbol symbol, final Namespace namespace, String newFunctionName) {
		final IMetadataProvider projection = myRootMetadataProvider.getProjection(namespace);
		if (newFunctionName != null) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(symbol, projection);
			FunctionSignature defaultFunction = (FunctionSignature) metadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);
			defaultFunction.setName(newFunctionName);
		}
		for (Production production : symbol.getProductions()) {
			new ExpressionTraverser() {

				@Override
				public INull caseExpression(Expression object) {
					IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, projection);
					if (metadata.isPresent(ATFMetadata.ASSIGN_TEXT_TO_ATTRIBUTE)) {
						assertLexical(object);
					}
					return INull.NULL;
				}
				
				@Override
				public INull caseCombination(Combination object) {
					super.caseCombination(object);
					return null; // fall through
				}
				
				@Override
				public INull caseIteration(Iteration object) {
					super.caseIteration(object);
					return null; // fall through
				}
				
				@Override
				public INull caseSymbolReference(SymbolReference object) {
					IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, projection);
					Symbol symbol = object.getSymbol();
					if (isToken(symbol)) {
						assertAbsence(symbol, "when referencing a token", ATFMetadata.ASSOCIATED_FUNCTION_NAME);
						assertAbsence(symbol, "when referencing a token", ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
						List<? extends EObject> attributes = metadata.readEObjects(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
						if (attributes != null && attributes.size() > 1) {
							myErrorHandler.reportError("Calling a token '%s': result cannot be assigned to many attributes", symbol.getName());
						}
						markAssociatedWithToken(namespace, object);
						ATFAttributeReference ref = (ATFAttributeReference) metadata.readEObject(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
						if (ref != null) {
							ref.getAttribute().setType(myStringType);
						}
					} else {
						IMetadataStorage symbolMetadata = getSymbolMetadataStorage(symbol);
						FunctionSignature function;
						Namespace functionNamespace;
						String symbolName = object.getSymbol().getName();
						if (!metadata.isPresent(ATFMetadata.ASSOCIATED_FUNCTION_NAME)) {
							
							function = (FunctionSignature) symbolMetadata
								.readEObject(ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION);
							if (function == null) {
								myErrorHandler.reportError("There's no default semantic function for symbol '%s'", symbolName);
							}
							functionNamespace = (Namespace) symbolMetadata
								.readEObject(ATFMetadata.DEFAULT_NAMESPACE);
						} else {
							String functionName = (String) metadata.readObject(ATFMetadata.ASSOCIATED_FUNCTION_NAME);
							if (!symbolMetadata.isPresent(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE)) {
								myErrorHandler.reportError("Function not found for symbol '%s': '%s'", symbolName, functionName);
							}
							Map<String, Namespace> functionNameToNamespace = getFunctionNameToNamespace(symbolMetadata);
							functionNamespace = assertNotNull(functionNameToNamespace.get(functionName));
							@SuppressWarnings("unchecked")
							Map<String, FunctionSignature> functionNameToFunction = (Map<String, FunctionSignature>) symbolMetadata
								.readObject(ATFMetadata.FUNCTION_NAME_TO_FUNCTION);
							function = functionNameToFunction.get(functionName);
						}
						assertNotNull(function);
						assertNotNull(functionNamespace);
						List<? extends EObject> arguments = metadata.readEObjects(ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
						int argCount = arguments == null ? 0 : arguments.size();
						int inputAttributesCount = function.getInputAttributes().size();
						if (inputAttributesCount != argCount) {
							myErrorHandler.reportError("Incorrect number of arguments is passed to function '%s' called for symbol '%s': %d instead of '%d'",
									function.getName(), symbolName, argCount, inputAttributesCount);
						}
						myWritableAspect.setAttribute(object, namespace, 
								ATFMetadata.ASSOCIATED_FUNCTION, wrapIntoCrossReferenceValue(function));
						myWritableAspect.setAttribute(object, namespace, 
								ATFMetadata.ASSOCIATED_NAMESPACE, wrapIntoCrossReferenceValue(functionNamespace));
					}
					return null; // fall through
				}

			}.doSwitch(production.getExpression());
		}
	}

	private void processLexicalRule(Symbol symbol) throws E {
		String togetherWithToken = "together with '" + ATFMetadata.TOKEN + "'";
		assertAbsence(symbol, togetherWithToken, ATFMetadata.SEMANTIC_FUNCTIONS);
		assertAbsence(symbol, togetherWithToken, ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION);
		assertAbsence(symbol, togetherWithToken, ATFMetadata.DEFAULT_NAMESPACE);
		assertAbsence(symbol, togetherWithToken, ATFMetadata.FUNCTION_NAME_TO_NAMESPACE);
		if (symbol.getProductions().size() > 1) {
			myErrorHandler.reportError("Symbol '%s': Many productions not allowed for tokens", symbol.getName());
		}
		Production production = symbol.getProductions().get(0);
		Expression expression = production.getExpression();
		assertLexical(expression);
	}

	private void markAssociatedWithToken(final Namespace namespace,
			SymbolReference object) {
		myWritableAspect.setAttribute(object, namespace, ATFMetadata.ASSOCIATED_WITH_TOKEN, null);
	}

	private void assertLexical(Expression expression) {
		myLexicalityChecker.doSwitch(expression);
	}

	private void assertAbsence(Symbol symbol,
			String message, String attributeName) throws E {
		IMetadataStorage storage = getSymbolMetadataStorage(symbol);
		if (storage.isPresent(attributeName)) {
			myErrorHandler.reportError("Symbol '%s': Attribute is not allowed %s: '%s'", 
					symbol.getName(), message, attributeName);
		}
	}
	
	private boolean isToken(Symbol symbol) {
		IMetadataStorage metadataStorage = getSymbolMetadataStorage(symbol);
		return metadataStorage.isPresent(ATFMetadata.TOKEN);
	}
	
	private IMetadataStorage getSymbolMetadataStorage(Symbol symbol) {
		IMetadataStorage storage = myStorageCache.get(symbol);
		if (storage == null) {
			storage = MetadataStorage.getMetadataStorage(symbol, myRootMetadataProvider);
			myStorageCache.put(symbol, storage);
		}
		return storage;
	}
	
	private Namespace getDefaultNamespace(Symbol symbol) {
		return assertNotNull((Namespace) getSymbolMetadataStorage(symbol).readEObject(ATFMetadata.DEFAULT_NAMESPACE));
	}

	private final <T> T assertNotNull(T subject) {
		if (subject == null) {
			myErrorHandler.reportError("Not null assertion failed");
		}
		return subject;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Namespace> getFunctionNameToNamespace(
			IMetadataStorage metadata) {
		return assertNotNull((Map<String, Namespace>) metadata.readObject(ATFMetadata.FUNCTION_NAME_TO_NAMESPACE));
	}
	
	private CrossReferenceValue wrapIntoCrossReferenceValue(
			EObject object) {
		CrossReferenceValue value = MetadataFactory.eINSTANCE.createCrossReferenceValue();
		value.getValues().add(object);
		return value;
	}

}
