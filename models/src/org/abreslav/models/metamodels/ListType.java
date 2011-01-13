package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IListType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.ObjectValue;

/**
 * @author abreslav
 */
public class ListType extends CollectionType implements IListType {
    public ListType(ObjectValue object) {
        super(object);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitListType(this, data);
    }

    @Override
    public String toString() {
        return "[" + getElementType() + (isNonempty() ? "+" : "*") + "]";
    }
}
