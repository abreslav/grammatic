package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IPropertyDescriptor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author abreslav
 */
public class ModelClass implements IClass {
    private final boolean isAbstract;
    private final String name;
    private final Set<IClass> superclasses = new LinkedHashSet<IClass>();
    private final Set<IPropertyDescriptor> propertyDescriptors = new LinkedHashSet<IPropertyDescriptor>();

    public ModelClass(boolean anAbstract, String name) {
        isAbstract = anAbstract;
        this.name = name;
    }

    public Set<IClass> getSuperclasses() {
        return superclasses;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public Set<IPropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
