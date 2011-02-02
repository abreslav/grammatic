package org.abreslav.grammar;

/**
 * @author abreslav
 */
public interface IExpression {
    <R, D> R accept(ExpressionVisitor<R, D> visitor, D data);
}
