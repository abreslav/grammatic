package org.abreslav.metametamodel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author abreslav
 */
public class MetaModelUtils {
    public static Collection<IPropertyDescriptor> getAllPropertyDescriptors(IClass classObject) {
        Set<IPropertyDescriptor> propertyDescriptors = new LinkedHashSet<IPropertyDescriptor>(classObject.getPropertyDescriptors());
        for (IClass superclass : classObject.getSuperClasses()) {
            propertyDescriptors.addAll(superclass.getPropertyDescriptors());
        }
        return propertyDescriptors;
    }
}
