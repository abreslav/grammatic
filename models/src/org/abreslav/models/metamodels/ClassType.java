package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IClassType;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public abstract class ClassType extends ObjectWrapper implements IClassType {
    private final IClass underlyingClass;

    public ClassType(ObjectValue object) {
        super(object);

        IValue classValue = object.getPropertyValue(new ReferenceValue(new StringValue("ClassType.class")));
        ReferenceValue classReference = cast(classValue, ReferenceValue.class, "ClassType.class must be a reference");
        this.underlyingClass = new ModelClass(classReference.getReferredObject());
    }

    public IClass getUnderlyingClass() {
        return underlyingClass;
    }

    @Override
    public String toString() {
        return ((ObjectWrapper) underlyingClass).getObject().getIdentity().toString();
    }
}
