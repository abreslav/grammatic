package org.abreslav.models;

/**
 * @author abreslav
 */
public class StringValue implements IPrimitiveValue<String> {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitString(this, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringValue that = (StringValue) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "\"" +
                value + "\"";
    }
}

