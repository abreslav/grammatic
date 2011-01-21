package org.abreslav.aspects.patterns;

import org.abreslav.lambda.IVariable;
import org.abreslav.models.paths.ModelPath;

import java.util.Collection;

/**
 * @author abreslav
 */
public interface IMultiEnvironment {
    Collection<ModelPath> getValues(IVariable variable);
}
