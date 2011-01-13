package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IType;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.StringValue;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class TypeUtil {

    public static IType createType(ObjectValue object) {
        IValue referredIdentity = object.getClassReference().getReferredIdentity();
        StringValue referredIdentityString = cast(referredIdentity, StringValue.class, "A class of a type must have a string identity");
        String clazz = referredIdentityString.getValue();

        if ("ReferenceType".equals(clazz)) {
            return new ReferenceType(object);
        } else if ("ObjectType".equals(clazz)) {
            return new ObjectType(object);
        } else if ("ListType".equals(clazz)) {
            return new ListType(object);
        } else if ("SetType".equals(clazz)) {
            return new SetType(object);
        } else if ("PrimitiveType".equals(clazz)) {
            return new PrimitiveType(object);
        } else if ("NullableType".equals(clazz)) {
            return new NullableType(object);
        } else {
            throw new IllegalArgumentException("Unknown type");
        }
    }

    private TypeUtil() {}
}
