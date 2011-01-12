package org.abreslav.models;

/**
 * @author abreslav
 */
public enum BooleanValue implements  IValue {
    TRUE(true), FALSE(false);

    private final boolean value;

    private BooleanValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitBoolean(this, data);
    }


    @Override
    public String toString() {
        return "" + value;
    }
}
