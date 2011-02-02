package org.abreslav.grammar.impl;

import org.abreslav.grammar.IEmpty;
import org.abreslav.grammar.ExpressionVisitor;

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

    public <R, D> R accept(ExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitEmpty(this, data);
    }
}
