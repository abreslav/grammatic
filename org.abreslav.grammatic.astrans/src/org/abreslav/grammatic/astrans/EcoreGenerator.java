package org.abreslav.grammatic.astrans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Iteration;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public class EcoreGenerator {

	public static EcoreGenerator create() {
		return new EcoreGenerator();
	}
	
	private final Map<Symbol, EDataType> myDataTypes = new HashMap<Symbol, EDataType>();
	private final Map<Symbol, EClass> myClasses = new HashMap<Symbol, EClass>();
	
	private EcoreGenerator() {
	}
	
	public EPackage generateEcore(Grammar grammar, IMetadataProvider metadataProvider) {
		IMetadataStorage grammarMetadata = MetadataStorage.getMetadataStorage(grammar, metadataProvider);
		EPackage ePackage = createEPackage(new EPackageDescriptor(grammarMetadata));

		for (Symbol symbol : grammar.getSymbols()) {
			EClassifier classifier = null;
			IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, metadataProvider);
			if (symbolMetadata.isPresent("lexical")
					&& !symbolMetadata.isPresent("fragment")) {
				myDataTypes.put(symbol, EcorePackage.eINSTANCE.getEString());
			} else if (symbolMetadata.isPresent("enum")) {
				EEnum eEnum = createEnum(symbol, metadataProvider);
				classifier = eEnum;
				myDataTypes.put(symbol, eEnum);
			} else if (symbolMetadata.isPresent("class")) {
				EClass eClass = createClassForSymbol(symbol, symbolMetadata);
				classifier = eClass;
				myClasses.put(symbol, eClass);
			}
			if (classifier != null) {
				ePackage.getEClassifiers().add(classifier);
			}
		}

		for (Entry<Symbol, EClass> entry : myClasses.entrySet()) {
			Symbol symbol = entry.getKey();
			EClass eClass = entry.getValue();
			if (eClass.isAbstract()) {
				fillInAbstractClass(eClass, symbol);
			} else {
				fillInClass(eClass, symbol, metadataProvider);
			}
		}
		
		return ePackage;
	}

	private void fillInAbstractClass(EClass eClass, Symbol symbol) {
		for (Production production : symbol.getProductions()) {
			Expression expression = production.getExpression();
			if (false == expression instanceof SymbolReference) {
				throw new IllegalArgumentException();
			}
			SymbolReference reference = (SymbolReference) expression;
			EClass subclass = myClasses.get(reference.getSymbol());
			if (subclass == null) {
				throw new IllegalArgumentException();
			}
			subclass.getESuperTypes().add(eClass);
		}
	}

	private EClass fillInClass(EClass eClass, Symbol symbol, IMetadataProvider metadataProvider) {
		List<Production> productions = symbol.getProductions();
		if (productions.size() != 1) {
			throw new IllegalArgumentException();
		}
		Expression expression = productions.get(0).getExpression();
		
		if (expression instanceof Sequence) {
			Sequence sequence = (Sequence) expression;
			for (Expression featureExpression : sequence.getExpressions()) {
				processIteration(eClass, metadataProvider, featureExpression);
			}
		} else {
			processIteration(eClass, metadataProvider, expression);
		}
		
		return eClass;
	}

	private void processIteration(EClass eClass,
			IMetadataProvider metadataProvider, Expression expression) {
		if (false == expression instanceof Iteration) {
			createStructuralFeature(eClass, expression, metadataProvider, 0, 1);
			return;
		}
		Iteration iteration = (Iteration) expression;
		createStructuralFeature(
				eClass, 
				expression, 
				metadataProvider, 
				iteration.getLowerBound(), 
				iteration.getUpperBound());				
	}

	private void createStructuralFeature(EClass eClass, Expression expression,
			IMetadataProvider metadataProvider, int lower, int upper) {
		if (false == expression instanceof SymbolReference) {
			throw new IllegalArgumentException();
		}
		SymbolReference ref = (SymbolReference) expression;
		IMetadataStorage referenceMetadata = MetadataStorage.getMetadataStorage(ref, metadataProvider);
		EStructuralFeatureDescriptor featureDescriptor = new EStructuralFeatureDescriptor(referenceMetadata);
		Symbol targetSymbol = ref.getSymbol();
		EDataType targetDataType = myDataTypes.get(targetSymbol);
		EStructuralFeature feature;
		if (targetDataType != null) {
			EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
			attribute.setEType(targetDataType);
			eClass.getEAttributes().add(attribute);
			feature = attribute;
		} else {
			EClass tragetClass = myClasses.get(targetSymbol);
			EReference reference = EcoreFactory.eINSTANCE.createEReference();
			reference.setEType(tragetClass);
			eClass.getEReferences().add(reference);
			feature = reference;
		}
		initializeStructuralFeature(feature, featureDescriptor);
		feature.setLowerBound(lower);
		feature.setUpperBound(upper);
	}

	private void initializeStructuralFeature(EStructuralFeature attribute,
			EStructuralFeatureDescriptor featureDescriptor) {
		attribute.setName(featureDescriptor.getName());
	}

	private EEnum createEnum(Symbol symbol, IMetadataProvider metadataProvider) {
		EEnum eEnum = EcoreFactory.eINSTANCE.createEEnum();
		eEnum.setName(JavaUtils.applyTypeNameConventions(symbol.getName()));
		int value = 0;
		for (Production production : symbol.getProductions()) {
			Expression expression = production.getExpression();
			if (false == expression instanceof StringExpression) {
				throw new IllegalArgumentException();
			}
			
			StringExpression stringExp = (StringExpression) expression;
			EEnumLiteral literal = EcoreFactory.eINSTANCE.createEEnumLiteral();
			literal.setName(JavaUtils.applyConstNameConventions(stringExp.getValue()));
			literal.setLiteral(stringExp.getValue());
			literal.setValue(value);
			eEnum.getELiterals().add(literal);
			value++;
		}
		return eEnum;
	}

	private EClass createClassForSymbol(Symbol symbol, IMetadataStorage symbolMetadata) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(JavaUtils.applyTypeNameConventions(symbol.getName()));
		boolean isAbstract = symbolMetadata.isPresent("abstract");
		eClass.setAbstract(isAbstract);
		eClass.setInterface(isAbstract);
		return eClass;
	}

	private EPackage createEPackage(EPackageDescriptor ePackageDescriptor) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName(ePackageDescriptor.getName());
		ePackage.setNsURI(ePackageDescriptor.getNsURI());
		ePackage.setNsPrefix(ePackageDescriptor.getNsPrefix());
		return ePackage;
	}
}
