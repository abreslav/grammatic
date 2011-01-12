package org.abreslav.lambda;

import java.util.List;

/**
 * @author abreslav
 */
public interface IAbstraction {
    List<IVariable> getParameters();
    ITerm getBody();
}
