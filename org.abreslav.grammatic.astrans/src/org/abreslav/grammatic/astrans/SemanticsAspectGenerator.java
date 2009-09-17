package org.abreslav.grammatic.astrans;

import java.util.Map;

import org.abreslav.grammatic.astrans.semantics.EnumLiteralAssignment;
import org.abreslav.grammatic.astrans.semantics.ExpressionSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.SemanticalAttribute;
import org.abreslav.grammatic.astrans.semantics.SemanticalAttributeReference;
import org.abreslav.grammatic.astrans.semantics.SymbolReferenceSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.SymbolSemanticalDescriptor;
import org.abreslav.grammatic.astrans.semantics.TokenDescriptor;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

public class SemanticsAspectGenerator implements IEcoreGeneratorTrace {

	public static SemanticsAspectGenerator create(IWritableAspect writableAspect) {
		return new SemanticsAspectGenerator(writableAspect);
	}
	
	private final Map<EClassifier, SemanticalAttribute> myResults = EMFProxyUtil.customHashMap();
	private final IWritableAspect myWritableAspect;
	
	private SemanticsAspectGenerator(IWritableAspect writableAspect) {
		myWritableAspect = writableAspect;
	}

	@Override
	public void grammarToPackage(Grammar grammar, EPackage pack) {
		// write EPackage to some global attribute
	}

	@Override
	public void symbolToClass(Symbol symbol, EClass eClass) {
		processClassifierSymbol(symbol, eClass);
	}

	@Override
	public void symbolReferenceToSubclass(SymbolReference symbolReference,
			Symbol symbol, EClass superclass, EClass subclass) {
		SymbolReferenceSemanticalDescriptor refDescriptor = SymbolReferenceSemanticalDescriptor.create();
		refDescriptor.getAssignedTo().add(new SemanticalAttributeReference(myResults.get(superclass)));
		SymbolReferenceSemanticalDescriptor.write(refDescriptor, symbolReference, myWritableAspect);
	}
	
	@Override
	public void symbolReferenceToFeature(SymbolReference symbolReference,
			EStructuralFeature feature) {
		SymbolReferenceSemanticalDescriptor refDescriptor = SymbolReferenceSemanticalDescriptor.create();
		refDescriptor.getAssignedTo().add(new SemanticalAttributeReference(myResults.get(feature.getEContainingClass())));
		SymbolReferenceSemanticalDescriptor.write(refDescriptor, symbolReference, myWritableAspect);
	}

	@Override
	public void symbolToEnum(Symbol symbol, EEnum eEnum) {
		processClassifierSymbol(symbol, eEnum);
	}

	@Override
	public void symbolReferenceToEnumLiteral(StringExpression stringExpression,
			EEnumLiteral literal) {
		ExpressionSemanticalDescriptor descriptor = ExpressionSemanticalDescriptor.create();
		descriptor.getAssignments().add(new EnumLiteralAssignment(myResults.get(literal.getEEnum()), literal));
		ExpressionSemanticalDescriptor.write(descriptor, stringExpression, myWritableAspect);
	}

	@Override
	public void symbolToString(Symbol symbol) {
		TokenDescriptor.write(symbol, TokenDescriptor.create(), myWritableAspect);
	}

	@Override
	public void symbolToWhitespace(Symbol symbol) {		
		TokenDescriptor.write(symbol, TokenDescriptor.create(false, true), myWritableAspect);
	}
	
	@Override
	public void symbolToFragment(Symbol symbol) {		
		TokenDescriptor.write(symbol, TokenDescriptor.create(true, false), myWritableAspect);
	}

	private void processClassifierSymbol(Symbol symbol, EClassifier eClass) {
		SymbolSemanticalDescriptor descriptor = SymbolSemanticalDescriptor.create();
		SemanticalAttribute result = new SemanticalAttribute(symbol.getName(), eClass);
		descriptor.getOutputAttributes().add(result);
		myResults.put(eClass, result);
		SymbolSemanticalDescriptor.write(descriptor, symbol, myWritableAspect);
	}
}
