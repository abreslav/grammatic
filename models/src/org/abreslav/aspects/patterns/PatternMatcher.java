package org.abreslav.aspects.patterns;

import org.abreslav.aspects.patterns.lambda.Wildcard;
import org.abreslav.lambda.*;
import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.models.*;
import org.abreslav.models.metamodels.ConformanceChecker;
import org.abreslav.models.metamodels.ModelClass;
import org.abreslav.models.metamodels.PropertyDescriptor;
import org.abreslav.models.paths.CollectionItemPathEntry;
import org.abreslav.models.paths.ModelPath;
import org.abreslav.models.paths.ModelPathInterpreter;
import org.abreslav.models.paths.PropertyPathEntry;
import org.abreslav.templates.lambda.ITermFactory;
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

    private static final ITermFactory factory = new ITermFactory.TermFactory() {
        @Override
        protected ITerm otherClass(String className, ObjectValue object) {
            if (className.equals("Wildcard")) {
                return new Wildcard(object);
            }
            return super.otherClass(className, object);
        }
    };

    public static Result match(IValue value, ITerm pattern) {
        PatternMatcher matcher = new PatternMatcher(value);
        IExitCode code = matcher.doMatch(new ModelPath(), pattern);
        if (code.ok()) {
            return new Result(matcher.multiEnvironment);
        }
        return new Result(code.message());
    }

    private final MultiEnvironment multiEnvironment = new MultiEnvironment();

    private final Map<IVariable, ITerm> variableBindings = new LinkedHashMap<IVariable, ITerm>();
    // pattern identity -> value identity
    private final Map<IValue, IValue> identityMap = new LinkedHashMap<IValue, IValue>();
    private final IValue modelRoot;
    private PatternMatcher(IValue modelRoot) {
        this.modelRoot = modelRoot;
    }

    public IExitCode doMatch(ModelPath currentPath, ITerm pattern) {
        return pattern.accept(termSwitch, currentPath);
    }

    private final ITermVisitor<IExitCode, ModelPath> termSwitch = new ITermVisitor<IExitCode, ModelPath>() {
        public IExitCode visitApplication(IApplication application, ModelPath path) {
            List<IVariable> parameters = application.getAbstraction().getParameters();
            List<ITerm> arguments = application.getArguments();

            for (int i = 0, argumentsSize = arguments.size(); i < argumentsSize; i++) {
                ITerm argument = arguments.get(i);
                IVariable parameter = parameters.get(i);

                addBinding(parameter, argument);
            }

            return doMatch(path, application.getAbstraction().getBody());
        }

        public IExitCode visitVariableUsage(IVariableUsage variableUsage, ModelPath path) {
            IVariable variable = variableUsage.getVariable();
            Collection<ModelPath> valuePaths = multiEnvironment.getValues(variable);
            if (valuePaths.isEmpty()) {
                ITerm pattern = variableBindings.get(variable);
                if (pattern == null) {
                    throw new IllegalArgumentException("Unbound variable: " + variable);
                }
                IExitCode exitCode = doMatch(path, pattern);
                if (!exitCode.ok()) {
                    return exitCode;
                }
            } else {
                IValue sampleValue = resolvePath(valuePaths.iterator().next());
                IValue value = resolvePath(path);
                if (!sampleValue.equals(value)) {
                    return fail("Model value " + value + " does not match a value " + sampleValue + " of the variable " + variable);
                }
            }
            multiEnvironment.addValue(variable, path);
            return ok();
        }

        public IExitCode visitTerm(ITerm term, ModelPath path) {
            if (term instanceof Wildcard) {
                Wildcard wildcard = (Wildcard) term;

                IValue value = resolvePath(path);
                if (ConformanceChecker.checkType(value, wildcard.getType())) {
                    return ok();
                } else {
                    // TODO
                    return fail("Stub");
                }
            }

            return matchValue(term, path);
        }
    };

    private IValue resolvePath(ModelPath path) {
        return ModelPathInterpreter.INSTANCE.getValueByPath(modelRoot, path);
    }

    private IExitCode matchValue(ITerm pattern, final ModelPath path) {
        IValue value = resolvePath(path);
        return value.accept(new IValueVisitor.Adapter<IExitCode, TemplateTerm>() {
            @Override
            public <T> IExitCode visitPrimitiveValue(IPrimitiveValue<T> value, TemplateTerm pattern) {
                return matchPrimitiveValue(value, pattern);
            }

            @Override
            public IExitCode visitReference(ReferenceValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ReferenceValue) {
                    return fail(value, pattern);
                }
                ReferenceValue patternReference = (ReferenceValue) patternValue;

                IValue patternIdentity = patternReference.getReferredIdentity();
                IValue valueIdentity = value.getReferredIdentity();
                return matchIdentities(patternIdentity, valueIdentity);
            }

            @Override
            public IExitCode visitNull(NullValue value, TemplateTerm pattern) {
                if (pattern.getValue().equals(value)) {
                    return ok();
                }
                return fail(value, pattern);
            }

            @Override
            public IExitCode visitObject(ObjectValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ObjectValue) {
                    return fail(value, pattern);
                }

                ObjectValue patternObject = (ObjectValue) patternValue;

                ReferenceValue patternClassReference = patternObject.getClassReference();
                ReferenceValue valueClassReference = value.getClassReference();
                Map<IPropertyDescriptor, IPropertyDescriptor> propertyMap = matchClasses(patternClassReference, valueClassReference);
                if (propertyMap == null) {
                    return fail("Model value class " + valueClassReference + " does not match " + patternClassReference);
                }

                for (Map.Entry<IPropertyDescriptor, IPropertyDescriptor> entry : propertyMap.entrySet()) {
                    PropertyDescriptor patternPropertyDescriptor = (PropertyDescriptor) entry.getKey();
                    PropertyDescriptor valuePropertyDescriptor = (PropertyDescriptor) entry.getValue();
                    ReferenceValue patternPropertyKey = new ReferenceValue(patternPropertyDescriptor.getObject().getIdentity());
                    IValue patternPropertyValue = patternObject.getPropertyValue(patternPropertyKey);
                    if (patternPropertyValue == null) {
                        return fail("Pattern " + patternObject + " does not have property: " + patternPropertyKey);
                    }

                    IExitCode exitCode = doMatch(path.append(new PropertyPathEntry(new ReferenceValue(valuePropertyDescriptor.getObject().getIdentity()))),
                            factory.createTerm(patternPropertyValue));
                    if (!exitCode.ok()) {
                        return exitCode;
                    }
                }

                return matchIdentities(patternObject.getIdentity(), value.getIdentity());
            }

            @Override
            public IExitCode visitList(ListValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ListValue) {
                    return fail("Value " + value + " does not match " + pattern + " which is not a list pattern");
                }
                return matchList(value.getValue(), ((ListValue) patternValue).getValue());
            }

            @Override
            public IExitCode visitCollectionValue(ICollectionValue value, TemplateTerm pattern) {
                IValue patternValue = pattern.getValue();
                if (false == patternValue instanceof ICollectionValue) {
                    return fail(value, pattern);
                }

                ICollectionValue patternCollection = (ICollectionValue) patternValue;
                Collection<IValue> patternValues = patternCollection.getValue();
                Collection<IValue> modelValues = value.getValue();
                if (patternValues.size() != modelValues.size()) {
                    return fail("Temporary: collection sizes do not match: " + modelValues.size() + " and " + patternValues.size()); // TODO
                }

                // TODO: wildcards, sets
                Iterator<IValue> patternIterator = patternValues.iterator();
                for (int i = 0; i < modelValues.size(); i++) {
                    ITerm patternItem = factory.createTerm(patternIterator.next());
                    IExitCode exitCode = doMatch(path.append(new CollectionItemPathEntry(i)), patternItem);
                    if (!exitCode.ok()) {
                        return exitCode;
                    }
                }

                return ok();
            }

            @Override
            public IExitCode visitValue(IValue value, TemplateTerm pattern) {
                throw new IllegalArgumentException("Unknown value: " + value);
            }
        }, (TemplateTerm) pattern);
    }

    private IExitCode matchList(List<IValue> value, List<IValue> pattern) {
        IValue patternEntry = pattern.get(0);

        return fail("Value " + value + " does not match the pattern " + pattern);
    }

    private IExitCode fail(IValue value, ITerm pattern) {
        return fail("Model value " + value + " does not match the pattern " + pattern);
    }

    // Return pattern property -> value property
    private Map<IPropertyDescriptor, IPropertyDescriptor> matchClasses(ReferenceValue patternClassReference, ReferenceValue valueClassReference) {
        // TODO: Not implemented, use external class matcher

        if (!patternClassReference.getReferredObject().equals(valueClassReference.getReferredObject())) {
            return null;
        }

        if (!matchIdentities(patternClassReference, valueClassReference).ok()) {
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

    private IExitCode matchIdentities(IValue patternIdentity, IValue valueIdentity) {
        IValue mappedIdentity = identityMap.get(patternIdentity);
        if (mappedIdentity == null) {
            identityMap.put(patternIdentity, valueIdentity);
            return ok();
        } else {
            if (mappedIdentity.equals(valueIdentity)) {
                return ok();
            }
            return fail("Model value identity " + valueIdentity + " does not match with " + mappedIdentity);
        }
    }

    private <T> IExitCode matchPrimitiveValue(IPrimitiveValue<T> value, TemplateTerm pattern) {
        if (value.equals(pattern.getValue())) {
            return ok();
        }
        return fail(value, pattern);
    }

    private void addBinding(IVariable parameter, ITerm argument) {
        if (variableBindings.put(parameter, argument) != null) {
            throw new IllegalArgumentException("Rebound variable: " + parameter);
        }
    }

    private static interface IExitCode {
        IExitCode OK = new IExitCode() {
            public boolean ok() {
                return true;
            }

            public String message() {
                throw new UnsupportedOperationException();
            }
        };

        boolean ok();
        String message();
    }

    private static IExitCode ok() {
        return IExitCode.OK;
    }

    private static IExitCode fail(final String message) {
        return new IExitCode() {
            public boolean ok() {
                return false;
            }

            public String message() {
                return message;
            }
        };
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

    public static class Result {
        private final MultiEnvironment multiEnvironment;
        private final String message;

        private Result(MultiEnvironment multiEnvironment, String message) {
            this.multiEnvironment = multiEnvironment;
            this.message = message;
        }

        private Result(MultiEnvironment multiEnvironment) {
            this(multiEnvironment, null);
        }

        private Result(String message) {
            this(null, message);
        }

        public boolean isSuccess() {
            return multiEnvironment != null;
        }

        public String getMessage() {
            return message;
        }

        public IMultiEnvironment getMultiEnvironment() {
            return multiEnvironment;
        }

        @Override
        public String toString() {
            return "Result{\n" +
                    (isSuccess() ? "ok\n" : "error='" + message + "'\n") +
                    "multiEnvironment=" + multiEnvironment + "\n" +
                    '}';
        }
    }
}
