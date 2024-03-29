/**
 * 
 */
package org.abreslav.grammatic.astrans;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.StringExpression;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IEcoreGeneratorTrace {
	public static final IEcoreGeneratorTrace NONE = new IEcoreGeneratorTrace() {

		@Override
		public void symbolReferenceToFeature(
				SymbolReference symbolReference, EStructuralFeature feature) {
		}

		@Override
		public void symbolToClass(Symbol symbol, EClass eClass) {
		}

		@Override
		public void symbolToEnum(Symbol symbol, EEnum eEnum) {
		}
		
		@Override
		public void symbolReferenceToEnumLiteral(
				StringExpression stringExpression, EEnumLiteral literal) {
		}

		@Override
		public void symbolToString(Symbol symbol) {
		}

		@Override
		public void grammarToPackage(Grammar grammar, EPackage pack) {
		}

		@Override
		public void symbolToFragment(Symbol symbol) {
		}

		@Override
		public void symbolToWhitespace(Symbol symbol) {
		}

		@Override
		public void symbolReferenceToSubclass(SymbolReference symbolReference,
				Symbol symbol, EClass superclass, EClass subclass) {
		}
		
	};

	void grammarToPackage(Grammar grammar, EPackage pack);
	// called before any symbolReferenceToFeature of this class, class might be incomplete, "abstract" is set correctly
	void symbolToClass(Symbol symbol, EClass eClass);
	void symbolToString(Symbol symbol);
	void symbolToFragment(Symbol symbol);
	void symbolToWhitespace(Symbol symbol);
	// called before any symbolReferenceToEnumLiteral, enum might be incomplete
	void symbolToEnum(Symbol symbol, EEnum eEnum);
	// called after symbolToClass
	void symbolReferenceToFeature(SymbolReference symbolReference, EStructuralFeature feature);
	// called after symbolToClass
	void symbolReferenceToSubclass(SymbolReference symbolReference, Symbol symbol, EClass superclass, EClass subclass);
	// called after symbolToEnum
	void symbolReferenceToEnumLiteral(StringExpression stringExpression, EEnumLiteral literal);
}