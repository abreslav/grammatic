package org.abreslav.models;

import java.util.Collection;
import java.util.List;

/**
 * @author abreslav
 */
public interface ICollectionValue extends IValue {
    Collection<IValue> getValue();

    ICollectionValue createNewFromList(List<IValue> list);
}
