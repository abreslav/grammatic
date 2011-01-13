package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.ISetType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.ObjectValue;

/**
 * @author abreslav
 */
public class SetType extends CollectionType implements ISetType {
    public SetType(ObjectValue object) {
        super(object);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitSetType(this, data);
    }

    @Override
    public String toString() {
        return "[" + getElementType() + (isNonempty() ? "+" : "*") + "]";
    }
}
