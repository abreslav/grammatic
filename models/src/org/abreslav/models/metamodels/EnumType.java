package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IEnum;
import org.abreslav.metametamodel.IEnumType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.ReferenceValue;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class EnumType extends ObjectWrapper implements IEnumType {
    private final IEnum underlyingEnum;

    public EnumType(ObjectValue object) {
        super(object);

        IValue enumValue = object.getPropertyValue(ref("EnumType.enum"));
        ReferenceValue enumReference = cast(enumValue, ReferenceValue.class, "EnumType.enum must be a reference");
        this.underlyingEnum = new Enum(enumReference.getReferredObject());
    }

    public IEnum getEnum() {
        return underlyingEnum;
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitEnumType(this, data);
    }

    @Override
    public String toString() {
        return "enum " + ((ObjectWrapper) underlyingEnum).getObject().getIdentity();
    }
}
