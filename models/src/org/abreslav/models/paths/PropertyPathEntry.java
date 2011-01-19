package org.abreslav.models.paths;

import org.abreslav.models.IValue;

/**
 * @author abreslav
 */
public class PropertyPathEntry implements IModelPathEntry {
    private final IValue property;

    public PropertyPathEntry(IValue property) {
        this.property = property;
    }

    public IValue getPropertyKey() {
        return property;
    }

    public <R, D> R accept(IModelPathVisitor<R, D> visitor, D data) {
        return visitor.visitProperty(this, data);
    }

    @Override
    public String toString() {
        return "PropertyPathEntry{" +
                "property=" + property +
                '}';
    }
}
