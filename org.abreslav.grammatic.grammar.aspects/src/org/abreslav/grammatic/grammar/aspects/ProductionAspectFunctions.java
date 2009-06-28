package org.abreslav.grammatic.grammar.aspects;

import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.eclipse.emf.common.util.EList;

public class ProductionAspectFunctions implements IGrammarAspectFunctions<Production, Production> {

	public static final ProductionAspectFunctions INSTANCE = new ProductionAspectFunctions();
	
	private ProductionAspectFunctions() {
	}

	@Override
	public void after(Production subject, Production addition) {
		addToTheSameList(subject, addition);
	}

	@Override
	public void before(Production subject, Production addition) {
		addToTheSameList(subject, addition);
	}

	private void addToTheSameList(Production subject, Production addition) {
		EList<Production> productions = getProductionList(subject);
		productions.add(addition);
	}

	@Override
	public void instead(Production subject, Production replacement) {
		EList<Production> productions = getProductionList(subject);
		productions.remove(subject);
		productions.add(replacement);
	}

	@Override
	public void remove(Production subject) {
		EList<Production> productions = getProductionList(subject);
		productions.remove(subject);
	}

	/*package*/ static EList<Production> getProductionList(Production subject) {
		Symbol symbol = (Symbol) subject.eContainer();
		EList<Production> productions = symbol.getProductions();
		return productions;
	}

}
