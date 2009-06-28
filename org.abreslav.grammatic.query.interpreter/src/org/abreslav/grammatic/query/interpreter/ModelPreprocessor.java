package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.LexicalDefinition;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.aspects.manager.IWritableAspect;
import org.abreslav.grammatic.metadata.system.ISystemMetadata;
import org.eclipse.emf.common.util.EList;

public class ModelPreprocessor implements IModelPreprocessor {

	private final IWritableAspect myWritableAspect;
	
	public ModelPreprocessor(IWritableAspect writableAspect) {
		myWritableAspect = writableAspect;
	}

	@Override
	public void attachSystemMetadata(Grammar grammar) {
		EList<Symbol> symbols = grammar.getSymbols();
		if (symbols.size() > 0) {
			attachStringAttribute(symbols.get(0), ISystemMetadata.SYSTEM_NAMESPACE, ISystemMetadata.SYMBOL_STARTING, "");
		}
		for (Symbol symbol : symbols) {
			attachSystemMetadataToASymbol(symbol);
		}
	}

	@Override
	public void attachSystemMetadataToASymbol(Symbol symbol) {
		String value = isTerminal(symbol) 
			? ISystemMetadata.SYMBOL_TYPE_TERMINAL
			: ISystemMetadata.SYMBOL_TYPE_NONTERMINAL;
		attachStringAttribute(symbol, ISystemMetadata.SYSTEM_NAMESPACE, ISystemMetadata.SYMBOL_TYPE, value);
		attachStringAttribute(symbol, ISystemMetadata.SYSTEM_NAMESPACE, ISystemMetadata.SYMBOL_NAME, symbol.getName());
	}

	private void attachStringAttribute(Symbol symbol, Namespace ns,
			String name, String value) {
		StringValue attrValue = MetadataFactory.eINSTANCE.createStringValue();
		attrValue.setValue(value);
		myWritableAspect.setAttribute(symbol, ns, name, attrValue);
	}
	
	private boolean isTerminal(Symbol symbol) {
		EList<Production> productions = symbol.getProductions();
		for (Production production : productions) {
			if (false == production.getExpression() instanceof LexicalDefinition) {
				return false;
			}
		}
		return true;
	}

}
