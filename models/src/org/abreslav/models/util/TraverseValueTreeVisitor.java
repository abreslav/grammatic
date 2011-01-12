package org.abreslav.models.util;

import org.abreslav.models.*;

import java.util.Map;

/**
 * @author abreslav
 */
public class TraverseValueTreeVisitor<R, D> extends IValueVisitor.Adapter<R, D> {
    @Override
    public R visitCollectionValue(ICollectionValue value, D data) {
        for (IValue v : value.getValue()) {
            v.accept(this, data);
        }
        return super.visitCollectionValue(value, data);
    }

    @Override
    public R visitObject(ObjectValue value, D data) {
        visitClassReference(value.getClassReference(), data);
        visitObjectIdentity(value.getIdentity(), data);
        for (Map.Entry<IValue, IValue> property : value.getProperties()) {
            visitPropertyName(property.getKey(), data);
            property.getValue().accept(this, data);
        }
        return super.visitObject(value, data);
    }

    private void visitObjectIdentity(IValue identity, D data) {
        identity.accept(this, data);
    }

    private void visitClassReference(ReferenceValue classReference, D data) {
        classReference.accept(this, data);
    }

    public void visitPropertyName(IValue name, D data) {
        name.accept(this, data);
    }


}
