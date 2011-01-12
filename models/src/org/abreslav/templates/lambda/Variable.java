package org.abreslav.templates.lambda;

import org.abreslav.lambda.IVariable;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

/**
 * @author abreslav
 */
public class Variable extends ObjectWrapper implements IVariable {
    public Variable(ObjectValue object) {
        super(object);
    }

    @Override
    public int hashCode() {
        return getObject().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Variable other = (Variable) obj;
        return this.getObject().equals(other.getObject());
    }
}
