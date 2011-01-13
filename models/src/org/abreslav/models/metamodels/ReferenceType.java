package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IReferenceType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

/**
 * @author abreslav
 */
public class ReferenceType extends ClassType implements IReferenceType {
    public ReferenceType(ObjectValue object) {
        super(object);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitReferenceType(this, data);
    }

    @Override
    public String toString() {
        return "ref(" + ((ObjectWrapper) getUnderlyingClass()).getObject().getIdentity() + ")";
    }
}
