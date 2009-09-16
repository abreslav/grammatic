package org.abreslav.grammatic.astrans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.AtfFactory;
import org.abreslav.grammatic.atf.FunctionCall;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.AttributeValue;
import org.abreslav.grammatic.metadata.CrossReferenceValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

public class ATFAspectGenerator implements IEcoreGeneratorTrace {

	public static ATFAspectGenerator create(IWritableAspect writableAspect) {
		return new ATFAspectGenerator(writableAspect);
	}
	
	private final IWritableAspect myWritableAspect;
	private final Map<EStructuralFeature, FunctionSignature> mySetterFunctions = new HashMap<EStructuralFeature, FunctionSignature>();
	private final Map<EEnumLiteral, FunctionSignature> myLiteralConstructors = new HashMap<EEnumLiteral, FunctionSignature>();
	private final Map<EClassifier, Namespace> myNamespaces = new HashMap<EClassifier, Namespace>();
	private final Map<EClassifier, FunctionSignature> myFunctions = new HashMap<EClassifier, FunctionSignature>();
	private final Map<EClassifier, ATFAttribute> myResults = new HashMap<EClassifier, ATFAttribute>();
	private final Set<Symbol> myTokens = EMFProxyUtil.customHashSet();
	
	private ATFAspectGenerator(IWritableAspect writableAspect) {
		myWritableAspect = writableAspect;
	}

	@Override
	public void grammarToPackage(Grammar grammar, EPackage pack) {
		// Nothing
	}

	@Override
	public void symbolReferenceToFeature(SymbolReference symbolReference,
			EStructuralFeature feature) {
		EClass eClass = feature.getEContainingClass();
		FunctionSignature setter = mySetterFunctions.get(feature);
		Namespace namespace = myNamespaces.get(eClass);
		
		EClassifier featureType = feature.getEType();
		ATFAttribute assignTo = createAttribute(featureType, feature.getName());
		myWritableAspect.setAttribute(symbolReference, namespace, 
				ATFMetadata.ASSIGNED_TO_ATTRIBUTES, createCrossReferenceValue(createAttributeReference(assignTo )));
		if (myTokens.contains(symbolReference.getSymbol())) {
			myWritableAspect.setAttribute(symbolReference, namespace, 
					ATFMetadata.ASSOCIATED_WITH_TOKEN, null);
		} else {
			myWritableAspect.setAttribute(symbolReference, namespace, 
					ATFMetadata.ASSOCIATED_WITH_DEFAULT_FUNCTION, null);
			myWritableAspect.setAttribute(symbolReference, namespace, 
					ATFMetadata.ASSOCIATED_FUNCTION, createCrossReferenceValue(myFunctions.get(featureType)));
			myWritableAspect.setAttribute(symbolReference, namespace, 
					ATFMetadata.ASSOCIATED_NAMESPACE, createCrossReferenceValue(myNamespaces.get(featureType)));
		}
		
		FunctionCall statement = createFunctionCall(setter);
		statement.getArguments().add(createAttributeReference(myResults.get(eClass)));
		statement.getArguments().add(createAttributeReference(assignTo));

		myWritableAspect.setAttribute(symbolReference, namespace, 
				ATFMetadata.AFTER, createCrossReferenceValue(statement));
	}
	
	@Override
	public void symbolReferenceToEnumLiteral(StringExpression stringExpression,
			EEnumLiteral literal) {
		ATFAttributeAssignment statement = AtfFactory.eINSTANCE.createATFAttributeAssignment();

		EEnum eEnum = literal.getEEnum();
		statement.getLeftSide().add(createAttributeReference(myResults.get(eEnum)));
		statement.setRightSide(createFunctionCall(myLiteralConstructors.get(literal)));
		
		myWritableAspect.setAttribute(stringExpression, myNamespaces.get(eEnum), 
				ATFMetadata.AFTER, createCrossReferenceValue(statement));
	}

	@Override
	public void symbolToClass(Symbol symbol, EClass eClass) {
		SemanticModule module = createSetterModule(symbol, eClass);
		putSymbolMetadata(symbol, eClass, module);
	}

	@Override
	public void symbolToEnum(Symbol symbol, EEnum eEnum) {
		SemanticModule module = createEnumModule(symbol, eEnum);
		putSymbolMetadata(symbol, eEnum, module);
	}

	@Override
	public void symbolToString(Symbol symbol) {
		markAsToken(symbol);
	}

	@Override
	public void symbolToFragment(Symbol symbol) {
		markAsToken(symbol);
		addTokenClass(symbol, "fragment");
	}

	@Override
	public void symbolToWhitespace(Symbol symbol) {
		markAsToken(symbol);
		addTokenClass(symbol, "whitespace");
	}

	private void putSymbolMetadata(Symbol symbol, EClassifier eClassifier,
			SemanticModule module) {
		Namespace defaultNamespace = createDefaultNamespace(symbol);
		myNamespaces.put(eClassifier, defaultNamespace);
		ATFAttribute result = createAttribute(eClassifier, "result");
		myResults.put(eClassifier, result);
		
		myWritableAspect.setAttribute(symbol, defaultNamespace, 
				ATFMetadata.SEMANTIC_MODULE, createCrossReferenceValue(module));
		
		FunctionSignature signature = AtfFactory.eINSTANCE.createFunctionSignature();
		signature.setName(symbol.getName());
		signature.getOutputAttributes().add(result);
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION, createCrossReferenceValue(signature));
		myWritableAspect.setAttribute(symbol, defaultNamespace, 
				ATFMetadata.SYNTACTIC_FUNCTION, createCrossReferenceValue(signature));
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.FUNCTION_NAME_TO_FUNCTION, createAttributeValue(
						Collections.singletonMap(signature.getName(), signature)
				));
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.FUNCTION_NAME_TO_NAMESPACE, createAttributeValue(defaultNamespace));
	}

	private SemanticModule createSetterModule(Symbol symbol, EClass eClass) {
		String symbolName = symbol.getName();
		SemanticModule module = createSemanticModule(symbolName);
		for (EStructuralFeature eStructuralFeature : eClass.getEStructuralFeatures()) {
			FunctionSignature signature = AtfFactory.eINSTANCE.createFunctionSignature();
			signature.setName("set" + JavaUtils.applyTypeNameConventions(eStructuralFeature.getName()));
			
			signature.getInputAttributes().add(createAttribute(eClass, symbolName));
			signature.getInputAttributes().add(createAttribute(eStructuralFeature.getEType(), eStructuralFeature.getName()));
			
			module.getFunctions().add(signature);
			
			mySetterFunctions.put(eStructuralFeature, signature);
		}
		return module;
	}

	private SemanticModule createEnumModule(Symbol symbol, EEnum eEnum) {
		SemanticModule module = createSemanticModule(symbol.getName());
		for (EEnumLiteral eEnumLiteral : eEnum.getELiterals()) {
			FunctionSignature signature = AtfFactory.eINSTANCE.createFunctionSignature();
			signature.setName(eEnumLiteral.getLiteral());
			signature.getOutputAttributes().add(createAttribute(eEnum, "result"));
			
			module.getFunctions().add(signature);
			myLiteralConstructors.put(eEnumLiteral, signature);
		}
		return module;
	}

	private Namespace createDefaultNamespace(Symbol symbol) {
		Namespace namespace = MetadataFactory.eINSTANCE.createNamespace();
		namespace.setUri(symbol.getName());
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.DEFAULT_NAMESPACE, createAttributeValue(namespace));
		return namespace;
	}

	private SemanticModule createSemanticModule(String symbolName) {
		SemanticModule module = AtfFactory.eINSTANCE.createSemanticModule();
		module.setName(symbolName);
		return module;
	}

	private CrossReferenceValue createCrossReferenceValue(
			EObject feature) {
		CrossReferenceValue value = MetadataFactory.eINSTANCE.createCrossReferenceValue();
		value.getValues().add(feature);
		return value;
	}
	
	private AttributeValue createAttributeValue(
			Object object) {
		AttributeValue value = MetadataFactory.eINSTANCE.createAttributeValue();
		value.getValues().add(object);
		return value;
	}

	private ATFAttribute createAttribute(EClassifier eClassifier, String name) {
		ATFAttribute attribute = AtfFactory.eINSTANCE.createATFAttribute();
		attribute.setName(name);
		attribute.setType(createType(eClassifier));
		return attribute;
	}

	private EGenericType createType(EClassifier eClassifier) {
		EGenericType type = EcoreFactory.eINSTANCE.createEGenericType();
		type.setEClassifier(eClassifier);
		return type;
	}

	private void markAsToken(Symbol symbol) {
		myTokens.add(symbol);
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, ATFMetadata.TOKEN, null);
	}

	private void addTokenClass(Symbol symbol, String object) {
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, ATFMetadata.TOKEN_CLASSES, createAttributeValue(object));
	}

	private FunctionCall createFunctionCall(FunctionSignature functionSignature) {
		FunctionCall call = AtfFactory.eINSTANCE.createFunctionCall();
		call.setFunction(functionSignature);
		return call;
	}

	private ATFAttributeReference createAttributeReference(
			ATFAttribute atfAttribute) {
		ATFAttributeReference ref = AtfFactory.eINSTANCE.createATFAttributeReference();
		ref.setAttribute(atfAttribute);
		return ref;
	}
}
