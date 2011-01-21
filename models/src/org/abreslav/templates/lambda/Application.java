package org.abreslav.templates.lambda;

import org.abreslav.lambda.IAbstraction;
import org.abreslav.lambda.IApplication;
import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.ITermVisitor;
import org.abreslav.models.IValue;
import org.abreslav.models.ListValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.ReferenceValue;

import java.util.ArrayList;
import java.util.List;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class Application extends PredefinedTerm implements IApplication {
    private final IAbstraction abstraction;
    private final List<ITerm> arguments = new ArrayList<ITerm>();

    public Application(ObjectValue object) {
        super(object);
        IValue abstraction = object.getPropertyValue(ref("Application.abstraction"));
        ReferenceValue abstractionReference = cast(abstraction, ReferenceValue.class, "Abstraction must be a reference");
        this.abstraction = new Abstraction(abstractionReference.getReferredObject());

        IValue arguments = object.getPropertyValue(ref("Application.arguments"));
        ListValue argList = cast(arguments, ListValue.class, "Arguments must be a list");
        for (IValue argument : argList.getValue()) {
            this.arguments.add(new TemplateTerm(argument));
        }
    }

    public IAbstraction getAbstraction() {
        return abstraction;
    }

    public List<ITerm> getArguments() {
        return arguments;
    }

    public <R, D> R accept(ITermVisitor<R, D> visitor, D data) {
        return visitor.visitApplication(this, data);
    }
}
