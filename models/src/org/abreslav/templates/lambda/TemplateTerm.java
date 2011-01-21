package org.abreslav.templates.lambda;

import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.ITermVisitor;
import org.abreslav.models.*;

/**
 * @author abreslav
 */
public class TemplateTerm implements ITerm {
    private final IValue value;

    /*package*/ TemplateTerm(IValue value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null");
        }
        this.value = value;
    }

    public IValue getValue() {
        return value;
    }

    public <R, D> R accept(final ITermVisitor<R, D> visitor, final D data) {
        return visitor.visitTerm(this, data);
    }

    @Override
    public String toString() {
        return "TemplateTerm{" +
                value +
                '}';
    }
}
