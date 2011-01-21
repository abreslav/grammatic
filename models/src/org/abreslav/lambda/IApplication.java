package org.abreslav.lambda;

import java.util.List;

/**
 * @author abreslav
 */
public interface IApplication extends IPredefinedTerm {
    IAbstraction getAbstraction();
    List<ITerm> getArguments();
}
