package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.ICollectionType;
import org.abreslav.metametamodel.IType;
import org.abreslav.models.*;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public abstract class CollectionType extends ObjectWrapper implements ICollectionType {

    private final boolean nonempty;
    private final IType elementType;

    public CollectionType(ObjectValue object) {
        super(object);

        IValue nonEmpty = object.getPropertyValue(ref("CollectionType.nonEmpty"));
        BooleanValue nonEmptyBoolean = cast(nonEmpty, BooleanValue.class, "CollectionType.nonEmpty must be boolean");
        this.nonempty = nonEmptyBoolean.getValue();

        IValue elementType = object.getPropertyValue(ref("CollectionType.elementType"));
        ObjectValue elementTypeObject = cast(elementType, ObjectValue.class, "CollectionType.elementType must be an object");
        this.elementType = new Type(elementTypeObject);
    }

    public boolean isNonempty() {
        return nonempty;
    }

    public IType getElementType() {
        return elementType;
    }
}
