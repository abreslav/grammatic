package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.INullableType;
import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class NullableType extends ObjectWrapper implements INullableType {
    private IType type;

    public NullableType(ObjectValue object) {
        super(object);

        IValue elementType = object.getPropertyValue(ref("NullableType.type"));
        ObjectValue elementTypeObject = cast(elementType, ObjectValue.class, "Type must be an object");
        this.type = new Type(elementTypeObject);
    }

    public IType getType() {
        return type;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitNullableType(this, data);
    }
}
