package org.abreslav.metametamodel;

import java.util.Set;

/**
 * @author abreslav
 */
public interface IClass {
    Set<IClass> getSuperClasses();
    boolean isAbstract();
    Set<IPropertyDescriptor> getPropertyDescriptors();
}
