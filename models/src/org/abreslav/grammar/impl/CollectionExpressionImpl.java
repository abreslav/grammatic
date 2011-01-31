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

    public Collection<IExpression> getExpressions() {
        return expressions;
    }
}
