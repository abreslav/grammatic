package org.abreslav.grammar.impl;

import org.abreslav.grammar.IAlternative;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);
        if (definition instanceof IAlternative) {
            IAlternative alternative = (IAlternative) definition;
            for (IExpression expression : alternative.getExpressions()) {
                builder.append(" : ").append(expression);
            }
        } else {
            builder.append(" : ").append(definition);
        }
        builder.append(" ;");
        return builder.toString();
    }
}
