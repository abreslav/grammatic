package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.ISetType;
import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class SetType extends CollectionType implements ISetType {
    public SetType(boolean nonempty, IType elementType) {
        super(nonempty, elementType);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitSetType(this, data);
    }

    @Override
    public String toString() {
        return "[" + getElementType() + (isNonempty() ? "+" : "*") + "]";
    }
}
