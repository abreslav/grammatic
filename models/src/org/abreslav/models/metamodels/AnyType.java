package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IAnyType;
import org.abreslav.metametamodel.ITypeVisitor;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

/**
 * @author abreslav
 */
public class AnyType extends ObjectWrapper implements IAnyType {
    public AnyType(ObjectValue object) {
        super(object);
    }

    public <R, D> R accept(ITypeVisitor<R, D> visitor, D data) {
        return visitor.visitAnyType(this, data);
    }
}
