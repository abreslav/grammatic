package org.abreslav.grammar;

/**
 * @author abreslav
 */
public interface IExpression {
    <R, D> R accept(IExpressionVisitor<R, D> visitor, D data);
}
