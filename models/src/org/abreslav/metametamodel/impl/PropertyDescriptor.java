package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.metametamodel.IType;

/**
 * @author abreslav
 */
public class PropertyDescriptor implements IPropertyDescriptor {
    private final String name;
    private final IType type;

    public PropertyDescriptor(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    public IType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
