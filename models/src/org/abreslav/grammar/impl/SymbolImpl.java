package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpression;
import org.abreslav.grammar.ISymbol;

/**
 * @author abreslav
 */
public class SymbolImpl implements ISymbol {
    private final IExpression definition;
    private final String name;

    public SymbolImpl(String name, IExpression definition) {
        this.definition = definition;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public IExpression getDefinition() {
        return definition;
    }

}
