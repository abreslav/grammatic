package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IPrimitiveType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class PrimitiveType extends ObjectWrapper implements IPrimitiveType {
    private final String type;

    public PrimitiveType(ObjectValue object) {
        super(object);

        IValue type = object.getPropertyValue(ref("PrimitiveType.type"));
        StringValue typeString = cast(type, StringValue.class, "PrimitiveType.type must be a string");
        this.type = typeString.getValue();
    }

    public String getType() {
        return type;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitPrimitiveType(this, data);
    }
}
