package org.abreslav.grammatic.testing;

import java.util.Arrays;
import java.util.Collection;

import org.abreslav.grammatic.grammar.Alternative;
import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Empty;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.GrammarFactory;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Sequence;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;

public class Utils {
	public static <T> Collection<T> $(T... items) {
		return Arrays.asList(items);
	}
	
	public static Empty empty() {
		return GrammarFactory.eINSTANCE.createEmpty();
	}

	public static Sequence sequence(Expression... expressions) {
		Sequence sequence = GrammarFactory.eINSTANCE.createSequence();
		sequence.getExpressions().addAll(Arrays.asList(expressions));
		return sequence;
	}
	
	public static Alternative alternative(Expression... expressions) {
		Alternative alternative = GrammarFactory.eINSTANCE.createAlternative();
		alternative.getExpressions().addAll(Arrays.asList(expressions));
		return alternative;
	}
	
	public static SymbolReference symbolReference(Symbol symbol) {
		SymbolReference symbolReference = GrammarFactory.eINSTANCE.createSymbolReference();
		symbolReference.setSymbol(symbol);
		return symbolReference;
	}

	public static Symbol symbol(String name, Expression expr) {
		Symbol symbol = GrammarFactory.eINSTANCE.createSymbol();
		symbol.setName(name);
		Production production = GrammarFactory.eINSTANCE.createProduction();
		symbol.getProductions().add(production);
		production.setExpression(expr);
		return symbol;
	}

	public static CharacterRange characterRange(int low, int high) {
		CharacterRange range = GrammarFactory.eINSTANCE.createCharacterRange();
		range.setLowerBound(low);
		range.setUpperBound(high);
		return range;
	}

	public static CharacterRange character(char c) {
		return characterRange(c, c);
	}
	
	public static Grammar grammar(Symbol... symbols) {
		Grammar grammar = GrammarFactory.eINSTANCE.createGrammar();
		grammar.getSymbols().addAll($(symbols));
		return grammar;
	}
}
