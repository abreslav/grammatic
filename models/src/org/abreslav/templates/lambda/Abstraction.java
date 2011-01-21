package org.abreslav.templates.lambda;

import org.abreslav.lambda.IAbstraction;
import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.IVariable;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import java.util.ArrayList;
import java.util.List;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class Abstraction extends ObjectWrapper implements IAbstraction {
    private final List<IVariable> parameters = new ArrayList<IVariable>();
    private final ITerm body;

    public Abstraction(ObjectValue object, ITermFactory factory) {
        super(object);
        IValue parameters = object.getPropertyValue(ref("Abstraction.parameters"));
        ListValue parametersList = cast(parameters, ListValue.class, "Parameters must be a list");
        for (IValue parameter : parametersList.getValue()) {
            ObjectValue parameterObject = cast(parameter, ObjectValue.class, "Parameter must be an object");
            this.parameters.add(new Variable(parameterObject));
        }

        this.body = factory.createTerm(object.getPropertyValue(ref("Abstraction.body")));
    }

    public List<IVariable> getParameters() {
        return parameters;
    }

    public ITerm getBody() {
        return body;
    }

}
