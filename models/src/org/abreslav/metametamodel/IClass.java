package org.abreslav.metametamodel;

import java.util.Set;

/**
 * @author abreslav
 */
public interface IClass extends IClassifier {
    Set<IClass> getSuperclasses();
    boolean isAbstract();
    Set<IPropertyDescriptor> getPropertyDescriptors();
}
