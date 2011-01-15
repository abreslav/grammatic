package org.abreslav.aspects.patterns;

import org.abreslav.lambda.IVariable;
import org.abreslav.models.IValue;

import java.util.Collection;

/**
 * @author abreslav
 */
public interface IMultiEnvironment {
    Collection<IValue> getValues(IVariable variable);
}
