package org.abreslav.templates.lambda;

import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.ITermVisitor;
import org.abreslav.models.*;

/**
 * @author abreslav
 */
public class TemplateTerm implements ITerm {
    private final IValue value;
    private ITerm structuredTerm;

    public TemplateTerm(IValue value) {
        this.value = value;
        value.accept(new IValueVisitor.Adapter<Void, Void>() {
            @Override
            public Void visitValue(IValue value, Void data) {
                structuredTerm = TemplateTerm.this;
                return null;
            }

            @Override
            public Void visitObject(ObjectValue value, Void data) {
                ReferenceValue classReference = value.getClassReference();
                // class identity must be a primitive value (well-formedness)
                IValue referredIdentity = classReference.getReferredIdentity();
                structuredTerm = referredIdentity.accept(new Adapter<ITerm, ObjectValue>() {
                    @Override
                    public ITerm visitValue(IValue value, ObjectValue data) {
                        return TemplateTerm.this;
                    }

                    @Override
                    public ITerm visitString(StringValue value, ObjectValue object) {
                        String className = value.getValue();
                        if ("Application".equals(className)) {
                            return new Application(object);
                        } else if ("VariableUsage".equals(className)) {
                            return new VariableUsage(object);
                        } else {
                            return TemplateTerm.this;
                        }
                    }
                }, value);
                return null;
            }
        }, null);
    }

    public IValue getValue() {
        return value;
    }

    public <R, D> R accept(final ITermVisitor<R, D> visitor, final D data) {
        if (structuredTerm == this) {
            return visitor.visitTerm(this, data);
        }
        return structuredTerm.accept(visitor, data);
    }

    @Override
    public String toString() {
        return "TemplateTerm{" +
                value +
                '}';
    }
}
