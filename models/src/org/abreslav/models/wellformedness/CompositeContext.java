package org.abreslav.models.wellformedness;

import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author abreslav
 */
public class CompositeContext implements IContext {
    private final IContext first;
    private final IContext second;

    public CompositeContext(IContext first, IContext second) {
        this.first = first;
        this.second = second;

        checkOverlap(first, second);
        checkOverlap(second, first);
    }

    private void checkOverlap(IContext first, IContext second) {
        Collection<ObjectValue> allObjects = first.getAllObjects();
        for (ObjectValue firstObject : allObjects) {
            IValue identity = firstObject.getIdentity();
            ObjectValue secondObject = second.getObject(identity);
            if (secondObject != null && !secondObject.equals(firstObject)) {
                throw new IllegalArgumentException("Joined contexts overlap on the identity: " + identity);
            }
        }
    }

    public ObjectValue getObject(IValue identity) {
        ObjectValue firstResult = first.getObject(identity);
        if (firstResult != null) {
            return firstResult;
        }
        return second.getObject(identity);
    }

    public Collection<ObjectValue> getAllObjects() {
        ArrayList<ObjectValue> result = new ArrayList<ObjectValue>(first.getAllObjects());
        result.addAll(second.getAllObjects());
        return Collections.unmodifiableCollection(result);
    }
}
