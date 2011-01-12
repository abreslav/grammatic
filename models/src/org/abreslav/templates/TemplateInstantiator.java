package org.abreslav.templates;

import org.abreslav.lambda.*;
import org.abreslav.models.ICollectionValue;
import org.abreslav.models.IValue;
import org.abreslav.models.IValueVisitor;
import org.abreslav.models.ObjectValue;
import org.abreslav.templates.lambda.TemplateTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class TemplateInstantiator {
    public static final TemplateInstantiator INSTANCE = new TemplateInstantiator();

    // TODO: template validator for recursivity and stuff

    private final IValueVisitor.Adapter<IValue,ITemplateContext> valueVisitor = new IValueVisitor.Adapter<IValue, ITemplateContext>() {
        @Override
        public IValue visitValue(IValue value, ITemplateContext data) {
            return value;
        }

        @Override
        public IValue visitCollectionValue(ICollectionValue value, ITemplateContext context) {
            ArrayList<IValue> newValues = new ArrayList<IValue>();
            for (IValue v : value.getValue()) {
                ITerm newTerm = instantiate(context, new TemplateTerm(v));
                newValues.add(((TemplateTerm) newTerm).getValue());
            }
            return value.createNewFromList(newValues);
        }

        @Override
        public IValue visitObject(ObjectValue value, ITemplateContext context) {
            // TODO: we don't instantiate classes here, although we could...
            ObjectValue newObject = new ObjectValue(value.getClassReference(), value.getIdentity());
            for (Map.Entry<IValue, IValue> property : value.getProperties()) {
                ITerm instantiate = instantiate(context, new TemplateTerm(property.getValue()));
                newObject.setProperty(property.getKey(), ((TemplateTerm) instantiate).getValue());
            }
            return newObject;
        }
    };

    private final ITermVisitor<ITerm,ITemplateContext> termVisitor = new ITermVisitor<ITerm, ITemplateContext>() {
        public ITerm visitApplication(IApplication application, ITemplateContext context) {
            return instantiate(
                    getContextFromApplication(application, context),
                    application.getAbstraction().getBody());
        }

        public ITerm visitVariableUsage(IVariableUsage variableUsage, ITemplateContext context) {
            ITerm value = context.getValue(variableUsage.getVariable());
            if (value == null) {
                throw new IllegalArgumentException("Undefined variable: " + variableUsage.getVariable());
            }
            return value;
        }

        public ITerm visitTerm(ITerm term, ITemplateContext context) {
            TemplateTerm templateTerm = cast(term, TemplateTerm.class, "A non-template term: " + term.getClass().getSimpleName());
            IValue newValue = templateTerm.getValue().accept(valueVisitor, context);
            return new TemplateTerm(newValue);
        }
    };

    private TemplateInstantiator() {}

    private ITemplateContext getContextFromApplication(IApplication application, ITemplateContext context) {
        TemplateContext result = new TemplateContext(context);

        List<ITerm> arguments = application.getArguments();
        List<IVariable> parameters = application.getAbstraction().getParameters();
        if (arguments.size() != parameters.size()) {
            throw new IllegalArgumentException("Argument list size mismatch");
        }
        for (int i = 0, argumentsSize = arguments.size(); i < argumentsSize; i++) {
            ITerm argument = arguments.get(i);
            IVariable variable = parameters.get(i);
            result.putValue(variable, instantiate(context, argument));
        }
        return result;
    }

    public ITerm instantiate(ITemplateContext context, ITerm templateTerm) {
        return templateTerm.accept(termVisitor, context);
    }
}