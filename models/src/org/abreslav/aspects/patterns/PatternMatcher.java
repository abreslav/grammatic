package org.abreslav.aspects.patterns;

import org.abreslav.lambda.*;
import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.models.*;
import org.abreslav.models.metamodels.ModelClass;
import org.abreslav.models.metamodels.PropertyDescriptor;
import org.abreslav.models.paths.CollectionItemPathEntry;
import org.abreslav.models.paths.ModelPath;
import org.abreslav.models.paths.ModelPathInterpreter;
import org.abreslav.models.paths.PropertyPathEntry;
import org.abreslav.templates.lambda.TemplateTerm;

import java.util.*;

/**
 * @author abreslav
 *
 * TODO: object template, reference template
 *
 * A reference in a model matches a refence in a pattern when the identities agree:
 *    all other reference that doMatch the same identity point to the same object
 */
public class PatternMatcher {

    public static IMultiEnvironment match(IValue value, ITerm pattern) {
        PatternMatcher matcher = new PatternMatcher(value);
        if (matcher.doMatch(new ModelPath(), pattern)) {
            return matcher.multiEnvironment;
        }
        return null;
    }

    private final MultiEnvironment multiEnvironment = new MultiEnvironment();
    private final Map<IVariable, ITerm> variableBindings = new LinkedHashMap<IVariable, ITerm>();
    // pattern identity -> value identity
    private final Map<IValue, IValue> identityMap = new LinkedHashMap<IValue, IValue>();
    private final IValue modelRoot;

    private PatternMatcher(IValue modelRoot) {
        this.modelRoot = modelRoot;
    }

    public boolean doMatch(ModelPath currentPath, ITerm pattern) {
        return pattern.accept(termSwitch, currentPath);
    }

    private final ITermVisitor<Boolean, ModelPath> termSwitch = new ITermVisitor<Boolean, ModelPath>() {
        public Boolean visitApplication(IApplication application, ModelPath path) {
            List<IVariable> parameters = application.getAbstraction().getParameters();
            List<ITerm> arguments = application.getArguments();

            for (int i = 0, argumentsSize = arguments.size(); i < argumentsSize; i++) {
                ITerm argument = arguments.get(i);
                IVariable parameter = parameters.get(i);

                addBinding(parameter, argument);
            }

            return doMatch(path, application.getAbstraction().getBody());
        }

        public Boolean visitVariableUsage(IVariableUsage variableUsage, ModelPath path) {
            IVariable variable = variableUsage.getVariable();
            Collection<ModelPath> valuePaths = multiEnvironment.getValues(variable);
            if (valuePaths.isEmpty()) {
                ITerm pattern = variableBindings.get(variable);
                if (pattern == null) {
                    throw new IllegalArgumentException("Unbound variable: " + variable);
                }
                if (!doMatch(path, pattern)) {
                    return false;
                }
            } else {
                IValue sampleValue = resolvePath(valuePaths.iterator().next());
                IValue value = resolvePath(path);
                if (!sampleValue.equals(value)) {
                    return false;
                }
            }
            multiEnvironment.addValue(variable, path);
            return true;
        }

        public Boolean visitTerm(ITerm term, ModelPath path) {
            return matchValue(term, path);
        }
    };

    private IValue resolvePath(ModelPath path) {
        return ModelPathInterpreter.INSTANCE.getValueByPath(modelRoot, path);
    }

    private boolean matchValue(ITerm pattern, final ModelPath path) {
        IValue value = resolvePath(path);
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
                    ReferenceValue patternPropertyKey = new ReferenceValue(patternPropertyDescriptor.getObject().getIdentity());
                    IValue patternPropertyValue = patternObject.getPropertyValue(patternPropertyKey);
                    if (patternPropertyValue == null) {
                        System.out.println("Pattern " + patternObject + " does not have property: " + patternPropertyKey);
                        return false;
                    }

                    if (!doMatch(path.append(new PropertyPathEntry(new ReferenceValue(valuePropertyDescriptor.getObject().getIdentity()))),
                            new TemplateTerm(patternPropertyValue))) {
                        return false;
                    }
                }

                return matchIdentities(patternObject.getIdentity(), value.getIdentity());
            }

            @Override
            public Boolean visitCollectionValue(ICollectionValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ICollectionValue) {
                    return false;
                }

                ICollectionValue patternCollection = (ICollectionValue) patternValue;
                if (patternCollection.getValue().size() != value.getValue().size()) {
                    return false; // TODO
                }

                // TODO: wildcards, sets
                Iterator<IValue> patternIterator = patternCollection.getValue().iterator();
                for (int i = 0; i < value.getValue().size(); i++) {
                    ITerm patternItem = new TemplateTerm(patternIterator.next());
                    if (!doMatch(path.append(new CollectionItemPathEntry(i)), patternItem)) {
                        return false;
                    }
                }

                return true;
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
        private final Map<IVariable, Set<ModelPath>> data = new LinkedHashMap<IVariable, Set<ModelPath>>();

        public Collection<ModelPath> getValues(IVariable variable) {
            return Collections.unmodifiableCollection(getModelPaths(variable));
        }

        private Set<ModelPath> getModelPaths(IVariable variable) {
            Set<ModelPath> modelPaths = data.get(variable);
            if (modelPaths == null) {
                modelPaths = new LinkedHashSet<ModelPath>();
                data.put(variable, modelPaths);
            }
            return modelPaths;
        }

        public void addValue(IVariable variable, ModelPath value) {
            getModelPaths(variable).add(value);
        }

        @Override
        public String toString() {
            return "MultiEnvironment{" +
                    "data=" + data +
                    '}';
        }
    }
}
