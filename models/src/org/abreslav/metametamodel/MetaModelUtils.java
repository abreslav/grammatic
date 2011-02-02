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
        for (IClass superclass : classObject.getSuperclasses()) {
            propertyDescriptors.addAll(getAllPropertyDescriptors(superclass));
        }
        return propertyDescriptors;
    }

    public static Set<IClass> getAllSuperclasesAndMe(IClass valueClass) {
        Set<IClass> result = new LinkedHashSet<IClass>(valueClass.getSuperclasses());
        result.add(valueClass);
        for (IClass aClass : valueClass.getSuperclasses()) {
            result.addAll(getAllSuperclasesAndMe(aClass));
        }
        return result;
    }
}
