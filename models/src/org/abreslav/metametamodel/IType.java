package org.abreslav.metametamodel;

/**
 * @author abreslav
 */
public interface IType {
    // TODO: enums?

    <R, D> R accept(ITypeVisitor<R, D> visitor, D data);
}
