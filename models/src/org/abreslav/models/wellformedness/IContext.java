package org.abreslav.models.wellformedness;

import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;

import java.util.Collection;
import java.util.Collections;

/**
 * @author abreslav
 */
public interface IContext {

    IContext EMPTY = new IContext() {
        public ObjectValue getObject(IValue identity) {
            return null;
        }

        public Collection<ObjectValue> getAllObjects() {
            return Collections.emptySet();
        }
    };

    /**
     * @param identity not null
     * @return null if not defined
     */
    ObjectValue getObject(IValue identity);

    Collection<ObjectValue> getAllObjects();

}
