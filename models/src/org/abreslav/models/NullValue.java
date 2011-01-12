package org.abreslav.models;

/**
 * @author abreslav
 */
public enum NullValue implements IValue {
    NULL;

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitNull(this, data);
    }

    @Override
    public String toString() {
        return "null";
    }
}
