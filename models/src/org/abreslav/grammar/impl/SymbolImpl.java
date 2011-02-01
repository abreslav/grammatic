package org.abreslav.grammar.impl;

import org.abreslav.grammar.IAlternative;
import org.abreslav.grammar.IExpression;
import org.abreslav.grammar.ISymbol;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author abreslav
 */
public class SymbolImpl implements ISymbol {
    private final IExpression definition;
    private final String name;
    private final Set<String> labels = new LinkedHashSet<String>();

    public SymbolImpl(Iterable<String> labels, String name, IExpression definition) {
        for (String label : labels) {
            this.labels.add(label);
        }
        this.definition = definition;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public IExpression getDefinition() {
        return definition;
    }

    public Set<String> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String label : labels) {
            builder.append("@").append(label).append(" ");
        }
        builder.append(name);
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
