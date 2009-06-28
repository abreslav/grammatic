package org.abreslav.grammatic.grammar.template.parser;

import org.abreslav.grammatic.emfutils.EObjectProxy;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.eclipse.emf.common.util.EList;

public class SymbolProxy extends EObjectProxy<Symbol> implements Symbol {

	@Override
	public String getName() {
		return pGetDelegate().getName();
	}

	@Override
	public EList<Production> getProductions() {
		return pGetDelegate().getProductions();
	}

	@Override
	public void setName(String value) {
		pGetDelegate().setName(value);
	}
}
