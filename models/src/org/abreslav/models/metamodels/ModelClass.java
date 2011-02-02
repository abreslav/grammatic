package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class ModelClass extends ObjectWrapper implements IClass {
    private final Set<IClass> superclasses;
    private final boolean isAbstract;
    private final Set<IPropertyDescriptor> propertyDescriptors;

    public ModelClass(ObjectValue object) {
        super(object);

        // TODO: no loops in inheritance...
        this.superclasses = computeSuperclasses(object);

        this.isAbstract = computeIsAbstract(object);

        // TODO: let's say property (type) overriding is prohibited for now
        this.propertyDescriptors = computePropertyDescriptors(object);
    }

    private LinkedHashSet<IPropertyDescriptor> computePropertyDescriptors(ObjectValue object) {
        IValue propertyDescriptors = object.getPropertyValue(ref("Class.propertyDescriptors"));
        SetValue descriptorSet = cast(propertyDescriptors, SetValue.class, "Property descriptors must be a set");
        LinkedHashSet<IPropertyDescriptor> descriptors = new LinkedHashSet<IPropertyDescriptor>();
        for (IValue descriptor : descriptorSet.getValue()) {
            ObjectValue descriptorObject = cast(descriptor, ObjectValue.class, "A property descriptor must be a value");
            descriptors.add(new PropertyDescriptor(descriptorObject));
        }
        return descriptors;
    }

    private boolean computeIsAbstract(ObjectValue object) {
        IValue isAbstract = object.getPropertyValue(ref("Class.abstract"));
        BooleanValue booleanAbstract = cast(isAbstract, BooleanValue.class, "'Abstract' property must be boolean");
        return booleanAbstract.getValue();
    }

    private LinkedHashSet<IClass> computeSuperclasses(ObjectValue object) {
        IValue superclasses = object.getPropertyValue(ref("Class.superclasses"));
        SetValue superclassSet = cast(superclasses, SetValue.class, "Superclasses must be a set: " + object.getIdentity());
        LinkedHashSet<IClass> result = new LinkedHashSet<IClass>();
        for (IValue superclass : superclassSet.getValue()) {
            ReferenceValue superclassReference = cast(superclass, ReferenceValue.class,"A superclass must be a reference");
            result.add(new ModelClass(superclassReference.getReferredObject()));
        }
        return result;
    }

    public Set<IClass> getSuperclasses() {
        return superclasses;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public Set<IPropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
