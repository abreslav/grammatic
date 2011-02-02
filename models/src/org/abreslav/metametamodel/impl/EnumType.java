package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IEnum;
import org.abreslav.metametamodel.IEnumType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class EnumType implements IEnumType {
    private final IEnum underlyingEnum;

    public EnumType(IEnum underlyingEnum) {
        this.underlyingEnum = underlyingEnum;
    }

    public IEnum getEnum() {
        return underlyingEnum;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitEnumType(this, data);
    }

    @Override
    public String toString() {
        return "enum " + underlyingEnum;
    }
}
