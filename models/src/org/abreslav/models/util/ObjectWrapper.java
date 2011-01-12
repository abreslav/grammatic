package org.abreslav.models.util;

import org.abreslav.models.ObjectValue;

/**
 * @author abreslav
 */
public abstract class ObjectWrapper {
    protected final ObjectValue object;

    public ObjectWrapper(ObjectValue object) {
        this.object = object;
    }

    public ObjectValue getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectWrapper that = (ObjectWrapper) o;

        if (object != null ? !object.equals(that.object) : that.object != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return object != null ? object.hashCode() : 0;
    }
}
