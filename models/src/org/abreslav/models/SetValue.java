package org.abreslav.models;

import java.util.*;

/**
 * @author abreslav
 */
public class SetValue implements ICollectionValue {
    private final Set<IValue> values;

    public SetValue(Collection<IValue> values) {
        this.values = Collections.unmodifiableSet(new LinkedHashSet<IValue>(values));
    }

    public Set<IValue> getValue() {
        return values;
    }

    public ICollectionValue createNewFromList(List<IValue> list) {
        return new SetValue(list);
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitSet(this, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetValue setValue = (SetValue) o;

        if (values != null ? !values.equals(setValue.values) : setValue.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (Iterator<IValue> iterator = values.iterator(); iterator.hasNext();) {
            IValue value = iterator.next();
            builder.append(value);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
