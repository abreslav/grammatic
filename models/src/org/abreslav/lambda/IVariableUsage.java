package org.abreslav.lambda;

/**
 * @author abreslav
 */
public interface IVariableUsage extends IPredefinedTerm {
    IVariable getVariable();
    boolean isInlineCollection();
}
