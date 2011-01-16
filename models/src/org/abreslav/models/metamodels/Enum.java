package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IEnum;
import org.abreslav.metametamodel.IEnumLiteral;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.SetValue;
import org.abreslav.models.util.ObjectWrapper;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.abreslav.models.metamodels.ReferenceUtil.ref;
import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class Enum extends ObjectWrapper implements IEnum {
    private final Set<IEnumLiteral> literals = new LinkedHashSet<IEnumLiteral>();

    public Enum(ObjectValue object) {
        super(object);

        IValue literalsValue = object.getPropertyValue(ref("Enum.literals"));
        SetValue literalsSet = cast(literalsValue, SetValue.class, "Enum.literals must be a set");
        for (IValue literalValue : literalsSet.getValue()) {
            literals.add(new EnumLiteral(cast(literalValue, ObjectValue.class, "Enum literal must be an object")));
        }
    }

    public Set<IEnumLiteral> getLiterals() {
        return literals;
    }
}
