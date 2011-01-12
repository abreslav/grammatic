package org.abreslav.lambda;

import java.util.List;

/**
 * @author abreslav
 */
public interface IApplication extends ITerm {
    IAbstraction getAbstraction();
    List<ITerm> getArguments();

}
