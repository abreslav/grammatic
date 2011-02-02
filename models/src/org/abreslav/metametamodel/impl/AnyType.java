package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IAnyType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class AnyType implements IAnyType {
    public AnyType() {
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitAnyType(this, data);
    }

    @Override
    public String toString() {
        return "_";
    }
}
