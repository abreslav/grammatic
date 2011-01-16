package org.abreslav.templates.lambda;

import org.abreslav.lambda.ITermVisitor;
import org.abreslav.lambda.IVariable;
import org.abreslav.lambda.IVariableUsage;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class VariableUsage extends ObjectWrapper implements IVariableUsage {
    private final IVariable variable;
    private final boolean inlineCollection;

    public VariableUsage(ObjectValue object) {
        super(object);
        IValue variable = object.getPropertyValue(ref("VariableUsage.variable"));
        ReferenceValue variableReference = cast(variable, ReferenceValue.class, "VariableUsage.variable must be a reference");
        this.variable = new Variable(variableReference.getReferredObject());

        IValue inlineCollectionValue = object.getPropertyValue(ref("VariableUsage.inlineCollection"));
        BooleanValue inlineCollectionBoolean = cast(inlineCollectionValue, BooleanValue.class, "VariableUsage.inlineCollection must be boolean");
        this.inlineCollection = inlineCollectionBoolean.getValue();
    }

    public IVariable getVariable() {
        return variable;
    }

    public boolean isInlineCollection() {
        return inlineCollection;
    }

    public <R, D> R accept(ITermVisitor<R, D> visitor, D data) {
        return visitor.visitVariableUsage(this, data);
    }
}
