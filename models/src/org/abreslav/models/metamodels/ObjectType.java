package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IObjectType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.ObjectValue;

/**
 * @author abreslav
 */
public class ObjectType extends ClassType implements IObjectType {
    public ObjectType(ObjectValue object) {
        super(object);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitObjectType(this, data);
    }
}
