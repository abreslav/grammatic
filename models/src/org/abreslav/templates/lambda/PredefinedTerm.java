package org.abreslav.templates.lambda;

import org.abreslav.lambda.IPredefinedTerm;
import org.abreslav.models.BooleanValue;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public abstract class PredefinedTerm extends ObjectWrapper implements IPredefinedTerm {
    private final boolean inlineCollection;

    public PredefinedTerm(ObjectValue object) {
        super(object);

        IValue inlineCollectionValue = object.getPropertyValue(ref("PredefinedTerm.inlineCollection"));
        BooleanValue inlineCollectionBoolean = cast(inlineCollectionValue, BooleanValue.class, "PredefinedTerm.inlineCollection must be boolean");
        this.inlineCollection = inlineCollectionBoolean.getValue();
    }

    public boolean isInlineCollection() {
        return inlineCollection;
    }
}
