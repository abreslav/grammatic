package org.abreslav.templates.lambda;

import org.abreslav.lambda.ITermVisitor;
import org.abreslav.lambda.IVariable;
import org.abreslav.lambda.IVariableUsage;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class VariableUsage extends ObjectWrapper implements IVariableUsage {
    private final IVariable variable;

    public VariableUsage(ObjectValue object) {
        super(object);
        IValue variable = object.getPropertyValue(new StringValue("variable"));
        ReferenceValue variableReference = cast(variable, ReferenceValue.class, "VariableUsage.variable must be a reference");
        this.variable = new Variable(variableReference.getReferredObject());
    }

    public IVariable getVariable() {
        return variable;
    }

    public <R, D> R accept(ITermVisitor<R, D> visitor, D data) {
        return visitor.visitVariableUsage(this, data);
    }
}
