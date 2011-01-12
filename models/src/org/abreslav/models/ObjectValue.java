package org.abreslav.models;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author abreslav
 */
public class ObjectValue implements  IValue {
    private final ReferenceValue classReference;
    private final IValue identity;
    private final Map<IValue, IValue> properties = new LinkedHashMap<IValue, IValue>();

    public ObjectValue(ReferenceValue classReference, IValue identity) {
        this.classReference = classReference;
        this.identity = identity;
    }

    public ReferenceValue getClassReference() {
        return classReference;
    }

    public IValue getIdentity() {
        return identity;
    }

    public IValue getPropertyValue(IValue key) {
        return properties.get(key);
    }

    public void setProperty(IValue key, IValue value) {
        properties.put(key, value);
    }

    public Set<Map.Entry<IValue, IValue>> getProperties() {
        return Collections.unmodifiableSet(properties.entrySet());
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitObject(this, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectValue that = (ObjectValue) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (classReference != null ? !classReference.equals(that.classReference) : that.classReference != null) return false;
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classReference != null ? classReference.hashCode() : 0;
        result = 31 * result + (identity != null ? identity.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return classReference + " " +
                identity +
                properties;
    }
}
