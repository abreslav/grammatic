package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.INullableType;
import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class NullableType implements INullableType {
    private IType type;

    public NullableType(IType type) {
        this.type = type;
    }

    public IType getType() {
        return type;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitNullableType(this, data);
    }

    @Override
    public String toString() {
        return "(" + type + ")?";
    }
}
