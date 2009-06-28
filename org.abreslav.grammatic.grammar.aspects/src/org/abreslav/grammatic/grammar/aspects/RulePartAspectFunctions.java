package org.abreslav.grammatic.grammar.aspects;

import java.util.List;

import org.abreslav.grammatic.grammar.Production;
import org.eclipse.emf.common.util.EList;

public class RulePartAspectFunctions implements IGrammarAspectFunctions<List<Production>, Production> {

	public static final RulePartAspectFunctions INSTANCE = new RulePartAspectFunctions();
	
	private RulePartAspectFunctions() {
	}

	@Override
	public void after(List<Production> subject, Production addition) {
		ProductionAspectFunctions.INSTANCE.after(subject.get(subject.size() - 1), addition);
	}

	@Override
	public void before(List<Production> subject, Production addition) {
		ProductionAspectFunctions.INSTANCE.before(subject.get(0), addition);
	}

	@Override
	public void instead(List<Production> subject, Production replacement) {
		EList<Production> productions = ProductionAspectFunctions.getProductionList(subject.get(0));
		productions.removeAll(subject);
		productions.add(replacement);
	}

	@Override
	public void remove(List<Production> subject) {
		EList<Production> productions = ProductionAspectFunctions.getProductionList(subject.get(0));
		productions.removeAll(subject);
	}

}
