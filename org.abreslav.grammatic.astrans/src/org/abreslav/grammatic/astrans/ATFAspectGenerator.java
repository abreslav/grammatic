package org.abreslav.grammatic.astrans;

import static org.abreslav.grammatic.metadata.util.MetadataUtils.createAttributeValue;
import static org.abreslav.grammatic.metadata.util.MetadataUtils.createCrossReferenceValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.astrans.semantics.EnumLiteralAssignment;
import org.abreslav.grammatic.astrans.semantics.ExpressionSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.SemanticalAssignment;
import org.abreslav.grammatic.astrans.semantics.SemanticalAttribute;
import org.abreslav.grammatic.astrans.semantics.SemanticalAttributeReference;
import org.abreslav.grammatic.astrans.semantics.SemanticalFeatureReference;
import org.abreslav.grammatic.astrans.semantics.SemanticalReference;
import org.abreslav.grammatic.astrans.semantics.SymbolReferenceSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.SymbolSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.TokenDescriptor;
import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.AtfFactory;
import org.abreslav.grammatic.atf.Block;
import org.abreslav.grammatic.atf.FunctionCall;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.atf.Statement;
import org.abreslav.grammatic.atf.interpreter.ExpressionTraverser;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

public class ATFAspectGenerator {

	public static void generate(Grammar grammar, IMetadataProvider metadataProvider, IWritableAspect writableAspect) {
		new ATFAspectGenerator(writableAspect).generate(grammar, metadataProvider);
	}
	
	private final IWritableAspect myWritableAspect;
	private final Map<Symbol, Namespace> myNamespaces = EMFProxyUtil.customHashMap();
	private final Map<Symbol, FunctionSignature> myFunctions = EMFProxyUtil.customHashMap();
	private final Map<SemanticalAttribute, ATFAttribute> myAttributes = new HashMap<SemanticalAttribute, ATFAttribute>();
	private final Set<Symbol> myTokens = EMFProxyUtil.customHashSet();

	private final Map<EClassifier, SemanticModule> myModules = new HashMap<EClassifier, SemanticModule>();
	private final Map<EClassifier, FunctionSignature> myClassConstructors = new HashMap<EClassifier, FunctionSignature>();
	private final Map<EStructuralFeature, FunctionSignature> mySetterFunctions = new HashMap<EStructuralFeature, FunctionSignature>();
	private final Map<EEnumLiteral, FunctionSignature> myLiteralConstructors = new HashMap<EEnumLiteral, FunctionSignature>();

	private ATFAspectGenerator(IWritableAspect writableAspect) {
		myWritableAspect = writableAspect;
	}
	
	public void generate(Grammar grammar, IMetadataProvider metadataProvider) {
		List<Symbol> symbols = grammar.getSymbols();
		for (Symbol symbol : symbols) {
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, metadataProvider);
			TokenDescriptor tokenDescriptor = TokenDescriptor.read(symbolMetadata);
			if (tokenDescriptor != null) {
				if (tokenDescriptor.isFragment()) {
					symbolToFragment(symbol);
				} else if (tokenDescriptor.isWhitespace()) {
					symbolToWhitespace(symbol);
				} else {
					symbolToToken(symbol);
				}
			} else {
				SymbolSemanticalDescriptor descriptor = SymbolSemanticalDescriptor.read(symbolMetadata);
				createSyntacticalFunction(symbol, descriptor);
			}
		}
		for (Symbol symbol : symbols) {
			processProductions(symbol, metadataProvider);
		}
		myWritableAspect.setAttribute(grammar, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.USED_SEMANTIC_MODULES, createCrossReferenceValue(myModules.values()));
	}

	private void processProductions(Symbol symbol, IMetadataProvider metadataProvider) {
		Namespace namespace = myNamespaces.get(symbol);
		for (Production production : symbol.getProductions()) {
			// TODO Constructors :: declaredAttributes
			Expression expression = production.getExpression();
			processExpression(expression, namespace, metadataProvider);
		}
	}

	private void processExpression(Expression expression, final Namespace namespace,
			final IMetadataProvider metadataProvider) {
		new ExpressionTraverser() {
			public INull caseSymbolReference(SymbolReference object) {
				Symbol symbol = object.getSymbol();
				if (myTokens.contains(symbol)) {
					myWritableAspect.setAttribute(object, namespace, 
							ATFMetadata.ASSOCIATED_WITH_TOKEN, null);
					return null; // fall trough
				} 
				
				IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, metadataProvider);
				SymbolReferenceSemanticalDescriptor descriptor = SymbolReferenceSemanticalDescriptor.read(metadata);
				List<Statement> featureAssignments = new ArrayList<Statement>();
				List<ATFAttributeReference> atfAssignedTo = new ArrayList<ATFAttributeReference>();
				for (SemanticalReference semanticalReference : descriptor.getAssignedTo()) {
					if (semanticalReference instanceof SemanticalAttributeReference) {
						SemanticalAttributeReference reference = (SemanticalAttributeReference) semanticalReference;
						atfAssignedTo.add(createAttributeReference(transformAttribute(reference.getVariable())));
					} else if (semanticalReference instanceof SemanticalFeatureReference) {
						SemanticalFeatureReference reference = (SemanticalFeatureReference) semanticalReference;
						EStructuralFeature feature = reference.getFeature();
						ATFAttribute attribute = createAttribute(feature.getEType(), feature.getName());
						atfAssignedTo.add(createAttributeReference(attribute));
						featureAssignments.add(createSetterCall(reference, attribute));
					} else {
						throw new IllegalArgumentException();
					}
				}
				
				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.AFTER, createCrossReferenceValue(createBlock(featureAssignments)));

				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.ASSIGNED_TO_ATTRIBUTES, createCrossReferenceValue(atfAssignedTo));
				
				List<ATFAttributeReference> arguments = new ArrayList<ATFAttributeReference>();
				for (SemanticalAttribute semanticalAttribute : descriptor.getArguments()) {
					arguments.add(createAttributeReference(transformAttribute(semanticalAttribute)));
				}
				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.ASSOCIATED_CALL_ARGUMENTS, createCrossReferenceValue(arguments));

				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.ASSOCIATED_WITH_DEFAULT_FUNCTION, null);
				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.ASSOCIATED_FUNCTION, createCrossReferenceValue(myFunctions.get(symbol)));
				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.ASSOCIATED_NAMESPACE, createCrossReferenceValue(myNamespaces.get(symbol)));
				return null; // fall through
			}

			public INull caseExpression(Expression object) {
				// definedAttributes
				IMetadataStorage metadataStorage = MetadataStorage.getMetadataStorage(object, metadataProvider);
				ExpressionSemanticalDescriptor descriptor = ExpressionSemanticalDescriptor.read(metadataStorage);
				List<Statement> afterStatements = new ArrayList<Statement>();
				for (SemanticalAssignment semanticalAssignment : descriptor.getAssignments()) {
					if (semanticalAssignment instanceof EnumLiteralAssignment) {
						EnumLiteralAssignment assignment = (EnumLiteralAssignment) semanticalAssignment;
						afterStatements.add(createLiteralAssignment(assignment));
					} else {
						throw new IllegalArgumentException();
					}
				}
				
				myWritableAspect.setAttribute(object, namespace, 
						ATFMetadata.AFTER, createCrossReferenceValue(createBlock(afterStatements)));
				return INull.NULL;
			}
			
		}.doSwitch(expression);
	}

	private void createSyntacticalFunction(Symbol symbol,
			SymbolSemanticalDescriptor descriptor) {
		Namespace defaultNamespace = createDefaultNamespace(symbol);
		myNamespaces.put(symbol, defaultNamespace);
		
		FunctionSignature signature = AtfFactory.eINSTANCE.createFunctionSignature();
		myFunctions.put(symbol, signature);
		signature.setName(symbol.getName());
		transformAttributeCollection(descriptor.getInputAttributes(),
				signature.getInputAttributes());
		transformAttributeCollection(descriptor.getOutputAttributes(),
				signature.getOutputAttributes());

		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.DEFAULT_SYNTACTIC_FUNCTION, createCrossReferenceValue(signature));
		myWritableAspect.setAttribute(symbol, defaultNamespace, 
				ATFMetadata.SYNTACTIC_FUNCTION, createCrossReferenceValue(signature));
		String functionName = signature.getName();
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.FUNCTION_NAME_TO_FUNCTION, createAttributeValue(
						createMap(functionName, signature)
				));
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.FUNCTION_NAME_TO_NAMESPACE, createAttributeValue(
						createMap(functionName, defaultNamespace)
				));
	}

	private void transformAttributeCollection(
			List<SemanticalAttribute> inputSemanticalAttributes,
			List<ATFAttribute> inputATFAttributes) {
		for (SemanticalAttribute semanticalAttribute : inputSemanticalAttributes) {
			inputATFAttributes.add(transformAttribute(semanticalAttribute));
		}
	}

	private ATFAttribute transformAttribute(
			SemanticalAttribute semanticalAttribute) {
		ATFAttribute attribute = myAttributes.get(semanticalAttribute);
		if (attribute == null) {
			attribute = AtfFactory.eINSTANCE.createATFAttribute();
			attribute.setName(semanticalAttribute.getName());
			attribute.setType(createType(semanticalAttribute.getType()));
			myAttributes.put(semanticalAttribute, attribute);
		}
		return attribute;
	}

	private void symbolToToken(Symbol symbol) {
		markAsToken(symbol);
	}

	private void symbolToFragment(Symbol symbol) {
		markAsToken(symbol);
		addTokenClass(symbol, "fragment");
	}

	private void symbolToWhitespace(Symbol symbol) {
		markAsToken(symbol);
		addTokenClass(symbol, "whitespace");
	}

	private FunctionSignature getSetter(EStructuralFeature eStructuralFeature) {
		FunctionSignature signature = mySetterFunctions.get(eStructuralFeature);
		if (signature == null) {
			signature = AtfFactory.eINSTANCE.createFunctionSignature();
			String prefix = eStructuralFeature.isMany() ? "add" : "set";
			signature.setName(prefix + JavaUtils.applyTypeNameConventions(eStructuralFeature.getName()));
			
			signature.getInputAttributes().add(createAttribute(eStructuralFeature.getEContainingClass(), "value"));
			signature.getInputAttributes().add(createAttribute(eStructuralFeature.getEType(), eStructuralFeature.getName()));
			
			SemanticModule module = getSemanticModule(eStructuralFeature.getEContainingClass());
			module.getFunctions().add(signature);
			
			mySetterFunctions.put(eStructuralFeature, signature);
		}
		return signature;
	}

	private FunctionSignature getLiteralConstructor(EEnumLiteral eEnumLiteral) {
		FunctionSignature signature = myLiteralConstructors.get(eEnumLiteral);
		if (signature == null) {
			signature = AtfFactory.eINSTANCE.createFunctionSignature();
			signature.setName(eEnumLiteral.getLiteral());
			EEnum eEnum = eEnumLiteral.getEEnum();
			signature.getOutputAttributes().add(createAttribute(eEnum, "result"));
			
			SemanticModule module = getSemanticModule(eEnum);
			module.getFunctions().add(signature);
			myLiteralConstructors.put(eEnumLiteral, signature);
		}
		return signature;
	}
	
	private FunctionSignature getClassConstructor(EClass eClass) {
		FunctionSignature signature = myClassConstructors.get(eClass);
		if (signature == null) {
			signature = AtfFactory.eINSTANCE.createFunctionSignature();
			signature.setName("create" + JavaUtils.applyTypeNameConventions(eClass.getName()));
			signature.getOutputAttributes().add(createAttribute(eClass, "result"));
			getSemanticModule(eClass).getFunctions().add(signature);
			myClassConstructors.put(eClass, signature);
		}
		return signature;
	}

	private SemanticModule getSemanticModule(EClassifier classifier) {
		SemanticModule module = myModules.get(classifier);
		if (module == null) {
			module = AtfFactory.eINSTANCE.createSemanticModule();
			module.setName(classifier.getName() + "Module");
			myModules.put(classifier, module);
		}
		return module;
	}
	
	private Namespace createDefaultNamespace(Symbol symbol) {
		Namespace namespace = MetadataFactory.eINSTANCE.createNamespace();
		namespace.setUri(symbol.getName());
		myWritableAspect.setAttribute(symbol, ATFMetadata.ATF_NAMESPACE, 
				ATFMetadata.DEFAULT_NAMESPACE, createCrossReferenceValue(namespace));
		return namespace;
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

	private Statement createSetterCall(
			SemanticalFeatureReference reference, ATFAttribute attribute) {
		FunctionSignature signature = getSetter(reference.getFeature());
		FunctionCall call = createFunctionCall(signature);
		call.getArguments().add(createAttributeReference(transformAttribute(reference.getVariable())));
		call.getArguments().add(createAttributeReference(attribute));
		return call;
	}

	private Statement createLiteralAssignment(
			EnumLiteralAssignment assignment) {
		ATFAttributeAssignment result = AtfFactory.eINSTANCE.createATFAttributeAssignment();
		result.getLeftSide().add(createAttributeReference(transformAttribute(assignment.getAttribute())));
		result.setRightSide(createFunctionCall(getLiteralConstructor(assignment.getLiteral())));
		return result;
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
	
	private Block createBlock(List<? extends Statement> statements) {
		Block block = AtfFactory.eINSTANCE.createBlock();
		block.getStatements().addAll(statements);
		return block;
	}
	
	private <K, V> Map<K, V> createMap(K k, V v) {
		HashMap<K, V> map = new HashMap<K, V>();
		map.put(k, v);
		return map;
	}
}
