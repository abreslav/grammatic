package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IClassType;

/**
 * @author abreslav
 */
public abstract class ClassType implements IClassType {
    private final IClass underlyingClass;

    protected ClassType(IClass underlyingClass) {
        this.underlyingClass = underlyingClass;
    }

    public IClass getUnderlyingClass() {
        return underlyingClass;
    }

    @Override
    public String toString() {
        return underlyingClass.toString();
    }
}
