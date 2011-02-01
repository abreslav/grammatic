package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpression;
import org.abreslav.grammar.IExpressionVisitor;
import org.abreslav.grammar.IRepetition;
import org.abreslav.grammar.RepetitionKind;

/**
 * @author abreslav
 */
public class RepetitionImpl implements IRepetition {
    private final IExpression body;
    private final RepetitionKind kind;

    public RepetitionImpl(IExpression body, RepetitionKind kind) {
        this.body = body;
        this.kind = kind;
    }

    public IExpression getBody() {
        return body;
    }

    public RepetitionKind getKind() {
        return kind;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitRepetition(this, data);
    }

    @Override
    public String toString() {
        return "(" + body + ")" + kind;
    }
}
