package org.abreslav.grammar.impl;

import org.abreslav.grammar.IEmpty;
import org.abreslav.grammar.IExpressionVisitor;

/**
 * @author abreslav
 */
public enum EmptyImpl implements IEmpty {
    INSTANCE {
        @Override
        public String toString() {
            return "empty";
        }
    };

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitEmpty(this, data);
    }
}
