package org.abreslav.lambda;

/**
 * @author abreslav
 */
public interface IVariableUsage extends ITerm {
    IVariable getVariable();
    boolean isInlineCollection();
}
