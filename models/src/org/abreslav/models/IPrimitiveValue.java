package org.abreslav.models;

/**
 * @author abreslav
 */
public interface IPrimitiveValue<T> extends IValue {
    T getValue();
}
