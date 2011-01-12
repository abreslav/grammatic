package org.abreslav.lambda;

/**
 * @author abreslav
 */
public interface ITerm {
    <R, D> R accept(ITermVisitor<R, D> visitor, D data);
}
