package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpression;
import org.abreslav.grammar.IExpressionVisitor;
import org.abreslav.grammar.IOption;

/**
 * @author abreslav
 */
public class OptionImpl implements IOption {
    private final IExpression body;

    public OptionImpl(IExpression body) {
        this.body = body;
    }

    public IExpression getBody() {
        return body;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitOption(this, data);
    }
}
