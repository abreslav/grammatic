package org.abreslav.aspects.patterns.lambda;

import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.ITermVisitor;
import org.abreslav.metametamodel.IType;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.metamodels.TypeUtil;
import org.abreslav.templates.lambda.PredefinedTerm;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class Wildcard extends PredefinedTerm implements ITerm {

    private final IType type;

    public Wildcard(ObjectValue object) {
        super(object);

        IValue variable = object.getPropertyValue(ref("Wildcard.type"));
        ObjectValue typeObject = cast(variable, ObjectValue.class, "Wildcard.type must be an object");

        this.type = TypeUtil.createType(typeObject);
    }

    public IType getType() {
        return type;
    }

    public <R, D> R accept(ITermVisitor<R, D> visitor, D data) {
        return visitor.visitTerm(this, data);
    }
}
