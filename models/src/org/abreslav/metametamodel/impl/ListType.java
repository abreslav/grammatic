package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IListType;
import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class ListType extends CollectionType implements IListType {
    public ListType(boolean nonempty, IType elementType) {
        super(nonempty, elementType);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitListType(this, data);
    }

    @Override
    public String toString() {
        return "[" + getElementType() + (isNonempty() ? "+" : "*") + "]";
    }
}
