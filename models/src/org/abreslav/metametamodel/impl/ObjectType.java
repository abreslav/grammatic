package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IObjectType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class ObjectType extends ClassType implements IObjectType {
    public ObjectType(IClass underlyingClass) {
        super(underlyingClass);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitObjectType(this, data);
    }

    @Override
    public String toString() {
        return "val(" + getUnderlyingClass() + ")";
    }
}
