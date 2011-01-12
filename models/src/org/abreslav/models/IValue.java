package org.abreslav.models;

/**
 * @author abreslav
 */
public interface IValue {
    <R, D> R accept(IValueVisitor<R, D> visitor, D data);
}
