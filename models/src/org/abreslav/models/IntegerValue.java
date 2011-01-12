package org.abreslav.models;

/**
 * @author abreslav
 */
public class IntegerValue implements IValue {
    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitInteger(this, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerValue that = (IntegerValue) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
