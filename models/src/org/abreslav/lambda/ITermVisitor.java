package org.abreslav.lambda;

/**
 * @author abreslav
 */
public interface ITermVisitor<R, D> {
    R visitApplication(IApplication application, D data);
    R visitVariableUsage(IVariableUsage variableUsage, D data);
    R visitTerm(ITerm term, D data);
}
