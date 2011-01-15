package org.abreslav.aspects.patterns;

import org.abreslav.lambda.*;
import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.models.*;
import org.abreslav.models.metamodels.ModelClass;
import org.abreslav.models.metamodels.PropertyDescriptor;
import org.abreslav.templates.lambda.TemplateTerm;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abreslav
 *
 * TODO: object template, reference template
 *
 * A reference in a model matches a refence in a pattern when the identities agree:
 *    all other reference that match the same identity point to the same object
 */
public class PatternMatcher {
    private final MultiEnvironment multiEnvironment = new MultiEnvironment();
    private final Map<IVariable, ITerm> variableBindings = new LinkedHashMap<IVariable, ITerm>();
    // pattern identity -> value identity
    private final Map<IValue, IValue> identityMap = new LinkedHashMap<IValue, IValue>();

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
            public <T> Boolean visitPrimitiveValue(IPrimitiveValue<T> value, TemplateTerm pattern) {
                return matchPrimitiveValue(value, pattern);
            }

            @Override
            public Boolean visitReference(ReferenceValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ReferenceValue) {
                    return false;
                }
                ReferenceValue patternReference = (ReferenceValue) patternValue;

                IValue patternIdentity = patternReference.getReferredIdentity();
                IValue valueIdentity = value.getReferredIdentity();
                return matchIdentities(patternIdentity, valueIdentity);
            }

            @Override
            public Boolean visitNull(NullValue value, TemplateTerm pattern) {
                return pattern.getValue().equals(value);
            }

            @Override
            public Boolean visitObject(ObjectValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ObjectValue) {
                    return false;
                }

                ObjectValue patternObject = (ObjectValue) patternValue;

                Map<IPropertyDescriptor, IPropertyDescriptor> propertyMap = matchClasses(patternObject.getClassReference(), value.getClassReference());
                if (propertyMap == null) {
                    return false;
                }

                for (Map.Entry<IPropertyDescriptor, IPropertyDescriptor> entry : propertyMap.entrySet()) {
                    PropertyDescriptor patternPropertyDescriptor = (PropertyDescriptor) entry.getKey();
                    PropertyDescriptor valuePropertyDescriptor = (PropertyDescriptor) entry.getValue();
                    IValue valuePropertyValue = value.getPropertyValue(valuePropertyDescriptor.getObject().getIdentity());
                    IValue patternPropertyValue = patternObject.getPropertyValue(patternPropertyDescriptor.getObject().getIdentity());

                    if (!match(valuePropertyValue, new TemplateTerm(patternPropertyValue))) {
                        return false;
                    }
                }

                return matchIdentities(patternObject.getIdentity(), value.getIdentity());
            }

            @Override
            public Boolean visitValue(IValue value, TemplateTerm pattern) {
                throw new IllegalArgumentException("Unknown value: " + value);
            }
        }, (TemplateTerm) pattern);
    }

    // Return pattern property -> value property
    private Map<IPropertyDescriptor, IPropertyDescriptor> matchClasses(ReferenceValue patternClassReference, ReferenceValue valueClassReference) {
        // TODO: Not implemented, use external class matcher

        if (!patternClassReference.getReferredObject().equals(valueClassReference.getReferredObject())) {
            return null;
        }

        if (!matchIdentities(patternClassReference, valueClassReference)) {
            return null;
        }

        // TODO: stub
        ModelClass patternClass = new ModelClass(patternClassReference.getReferredObject());
        LinkedHashMap<IPropertyDescriptor, IPropertyDescriptor> result = new LinkedHashMap<IPropertyDescriptor, IPropertyDescriptor>();
        for (IPropertyDescriptor descriptor : patternClass.getPropertyDescriptors()) {
            result.put(descriptor, descriptor);
        }
        return result;
    }

    private boolean matchIdentities(IValue patternIdentity, IValue valueIdentity) {
        IValue mappedIdentity = identityMap.get(patternIdentity);
        if (mappedIdentity == null) {
            identityMap.put(patternIdentity, valueIdentity);
            return true;
        } else {
            return mappedIdentity.equals(valueIdentity);
        }
    }

    private <T> boolean matchPrimitiveValue(IPrimitiveValue<T> value, TemplateTerm pattern) {
        return value.equals(pattern.getValue());
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
