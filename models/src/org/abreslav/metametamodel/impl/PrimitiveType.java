package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IPrimitiveType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class PrimitiveType implements IPrimitiveType {
    private final String type;

    public PrimitiveType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitPrimitiveType(this, data);
    }

    @Override
    public String toString() {
        return type;
    }
}
