package org.abreslav.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author abreslav
 */
public class ListValue implements ICollectionValue {
    private final List<IValue> value;

    public ListValue(List<IValue> value) {
        this.value = Collections.unmodifiableList(new ArrayList<IValue>(value));
    }

    public ListValue(IValue... values) {
        this.value = Collections.unmodifiableList(Arrays.asList(values));
    }

    public List<IValue> getValue() {
        return value;
    }

    public ICollectionValue createNewFromList(List<IValue> list) {
        return new ListValue(list);
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitList(this, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListValue listValue = (ListValue) o;

        if (value != null ? !value.equals(listValue.value) : listValue.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
