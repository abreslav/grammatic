package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.IEnumLiteral;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.ObjectWrapper;

/**
 * @author abreslav
 */
public class EnumLiteral extends ObjectWrapper implements IEnumLiteral {
    public EnumLiteral(ObjectValue object) {
        super(object);
    }
}
