package org.abreslav.metametamodel;

/**
 * @author abreslav
 */
public interface ICollectionType extends IType {
    boolean isNonempty();
    IType getElementType();
}
