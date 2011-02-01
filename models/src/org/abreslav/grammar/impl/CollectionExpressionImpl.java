package org.abreslav.grammar.impl;

import org.abreslav.grammar.ICollectionExpression;
import org.abreslav.grammar.IExpression;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author abreslav
 */
public abstract class CollectionExpressionImpl implements ICollectionExpression {
    private final Collection<IExpression> expressions = new ArrayList<IExpression>();

    protected CollectionExpressionImpl(Iterable<IExpression> expressions) {
        for (IExpression expression : expressions) {
            this.expressions.add(expression);
        }
    }

    protected CollectionExpressionImpl(IExpression first, Iterable<IExpression> rest) {
        this.expressions.add(first);
        for (IExpression expression : rest) {
            this.expressions.add(expression);
        }
    }

    public Collection<IExpression> getExpressions() {
        return expressions;
    }
}
