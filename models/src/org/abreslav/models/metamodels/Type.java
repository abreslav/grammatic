package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class Type extends ObjectWrapper implements IType {
    private final IType typedRepresentation;

    public Type(ObjectValue object) {
        super(object);

        IValue referredIdentity = object.getClassReference().getReferredIdentity();
        StringValue referredIdentityString = cast(referredIdentity, StringValue.class, "A class of a type must have a string identity");
        String clazz = referredIdentityString.getValue();

        if ("ReferenceType".equals(clazz)) {
            this.typedRepresentation = new ReferenceType(object);
        } else if ("ObjectType".equals(clazz)) {
            this.typedRepresentation = new ObjectType(object);
        } else if ("ListType".equals(clazz)) {
            this.typedRepresentation = new ListType(object);
        } else if ("SetType".equals(clazz)) {
            this.typedRepresentation = new SetType(object);
        } else if ("PrimitiveType".equals(clazz)) {
            this.typedRepresentation = new PrimitiveType(object);
        } else if ("NullableType".equals(clazz)) {
            this.typedRepresentation = new NullableType(object);
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return typedRepresentation.accept(visitor, data);
    }
}
