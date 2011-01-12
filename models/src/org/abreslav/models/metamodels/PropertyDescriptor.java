package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.metametamodel.IType;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class PropertyDescriptor extends ObjectWrapper implements IPropertyDescriptor {
    private IType type;

    public PropertyDescriptor(ObjectValue object) {
        super(object);
    }

    private Type computeType(ObjectValue object) {
        IValue type = object.getPropertyValue(ref("PropertyDescriptor.type"));
        ObjectValue typeObject = cast(type, ObjectValue.class, "A type must be an object");
        return new Type(typeObject);
    }

    public IType getType() {
        if (type == null) {
            this.type = computeType(object);
        }
        return type;
    }
}
