package org.abreslav.aspects.patterns;

import org.abreslav.lambda.*;
import org.abreslav.models.*;
import org.abreslav.models.StringValue;
import org.abreslav.templates.lambda.TemplateTerm;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abreslav
 */
public class PatternMatcher {
    private final MultiEnvironment multiEnvironment = new MultiEnvironment();
    private final Map<IVariable, ITerm> variableBindings = new LinkedHashMap<IVariable, ITerm>();

    public boolean match(IValue value, ITerm pattern) {
        return pattern.accept(termSwitch, value);
    }

    private final ITermVisitor<Boolean, IValue> termSwitch = new ITermVisitor<Boolean, IValue>() {
        public Boolean visitApplication(IApplication application, IValue value) {
            List<IVariable> parameters = application.getAbstraction().getParameters();
            List<ITerm> arguments = application.getArguments();

            for (int i = 0, argumentsSize = arguments.size(); i < argumentsSize; i++) {
                ITerm argument = arguments.get(i);
                IVariable parameter = parameters.get(i);

                addBinding(parameter, argument);
            }

            return match(value, application.getAbstraction().getBody());
        }

        public Boolean visitVariableUsage(IVariableUsage variableUsage, IValue value) {
            IVariable variable = variableUsage.getVariable();
            Collection<IValue> values = multiEnvironment.getValues(variable);
            if (values.isEmpty()) {
                ITerm pattern = variableBindings.get(variable);
                if (pattern == null) {
                    throw new IllegalArgumentException("Unbound variable: " + variable);
                }
                if (!match(value, pattern)) {
                    return false;
                }
            } else {
                IValue sampleValue = values.iterator().next();
                if (!sampleValue.equals(value)) {
                    return false;
                }
            }
            multiEnvironment.addValue(variable, value);
            return true;
        }

        public Boolean visitTerm(ITerm term, IValue value) {
            return matchValue(term, value);
        }
    };

    private boolean matchValue(ITerm pattern, IValue value) {
        return value.accept(new IValueVisitor.Adapter<Boolean, TemplateTerm>() {
            @Override
            public Boolean visitBoolean(BooleanValue value, TemplateTerm pattern) {
                return value.equals(pattern.getValue());
            }

            @Override
            public Boolean visitInteger(IntegerValue value, TemplateTerm pattern) {
                return value.equals(pattern.getValue());
            }

            @Override
            public Boolean visitString(StringValue value, TemplateTerm pattern) {
                return value.equals(pattern.getValue());
            }

            @Override
            public Boolean visitReference(ReferenceValue value, TemplateTerm data) {
                // TODO: Not implemented
                return super.visitReference(value, data);
            }

            @Override
            public Boolean visitValue(IValue value, TemplateTerm pattern) {
                throw new IllegalArgumentException("Unknown value: " + value);
            }
        }, (TemplateTerm) pattern);
    }

    private void addBinding(IVariable parameter, ITerm argument) {
        if (variableBindings.put(parameter, argument) != null) {
            throw new IllegalArgumentException("Rebound variable: " + parameter);
        }
    }

    private class MultiEnvironment implements  IMultiEnvironment {
        public Collection<IValue> getValues(IVariable variable) {
            // TODO: not implemented
            return null;
        }

        public void addValue(IVariable variable, IValue value) {
            // TODO: Not implemented
        }
    }
}
