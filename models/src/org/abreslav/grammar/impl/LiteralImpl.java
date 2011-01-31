package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpressionVisitor;
import org.abreslav.grammar.ILiteral;

/**
 * @author abreslav
 */
public class LiteralImpl implements ILiteral {
    private final String value;

    public LiteralImpl(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitLiteral(this, data);
    }
}
