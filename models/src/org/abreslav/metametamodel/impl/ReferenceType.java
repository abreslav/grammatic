package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IReferenceType;
import org.abreslav.metametamodel.ITypeVisitor;

/**
 * @author abreslav
 */
public class ReferenceType extends ClassType implements IReferenceType {
    public ReferenceType(IClass underlyingClass) {
        super(underlyingClass);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitReferenceType(this, data);
    }

    @Override
    public String toString() {
        return "ref(" + getUnderlyingClass() + ")";
    }
}
