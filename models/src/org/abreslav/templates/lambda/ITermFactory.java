package org.abreslav.templates.lambda;

import org.abreslav.lambda.ITerm;
import org.abreslav.models.*;

/**
 * @author abreslav
 */
public interface ITermFactory {

    ITermFactory TEMPLATE_TERM_FACTORY = new TermFactory();

    ITerm createTerm(IValue value);

    class TermFactory implements ITermFactory {

        protected TermFactory() {}

        public ITerm createTerm(IValue value) {
            return value.accept(new IValueVisitor.Adapter<ITerm, IValue>() {
                @Override
                public ITerm visitValue(IValue value, IValue data) {
                    return new TemplateTerm(data);
                }

                @Override
                public ITerm visitObject(ObjectValue value, IValue data) {
                    ReferenceValue classReference = value.getClassReference();
                    // class identity must be a primitive value (well-formedness)
                    IValue referredIdentity = classReference.getReferredIdentity();
                    return referredIdentity.accept(getObjectCreator(), value);
                }
            }, value);

        }

        private IValueVisitor.Adapter<ITerm, ObjectValue> getObjectCreator() {
            return new IValueVisitor.Adapter<ITerm, ObjectValue>() {
                @Override
                public ITerm visitValue(IValue value, ObjectValue data) {
                    return new TemplateTerm(data);
                }

                @Override
                public ITerm visitString(StringValue value, ObjectValue object) {
                    String className = value.getValue();
                    if ("Application".equals(className)) {
                        return new Application(object, TermFactory.this);
                    } else if ("VariableUsage".equals(className)) {
                        return new VariableUsage(object);
                    } else {
                        return otherClass(className, object);
                    }
                }
            };
        }

        protected ITerm otherClass(String className, ObjectValue object) {
            return new TemplateTerm(object);
        }
    }
}
