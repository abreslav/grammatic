package org.abreslav.grammar.impl;

import org.abreslav.grammar.IAlternative;
import org.abreslav.grammar.IExpression;
import org.abreslav.grammar.ExpressionVisitor;

import java.util.Iterator;

/**
 * @author abreslav
 */
public class AlternativeImpl extends CollectionExpressionImpl implements IAlternative {
    public AlternativeImpl(Iterable<IExpression> expressions) {
        super(expressions);
    }

    public AlternativeImpl(IExpression first, Iterable<IExpression> rest) {
        super(first, rest);
    }

    public <R, D> R accept(ExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitAlternative(this, data);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<IExpression> iterator = getExpressions().iterator(); iterator.hasNext();) {
            IExpression expression = iterator.next();
            stringBuilder.append(expression);
            if (iterator.hasNext()) {
                stringBuilder.append(" | ");
            }
        }
        return stringBuilder.toString();
    }
}
