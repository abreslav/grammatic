package org.abreslav.models.util;

import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;

import java.util.Collection;
import java.util.Set;

/**
 * @author abreslav
 */
public class ModelUtils {
    public static void collectAllObjects(Set<? extends IValue> preModel, final Collection<ObjectValue> allObjects) {
        for (IValue value : preModel) {
            value.accept(new TraverseValueTreeVisitor<Void, Void>() {
                @Override
                public Void visitObject(ObjectValue value, Void data) {
                    allObjects.add(value);
                    return super.visitObject(value, data);
                }
            }, null);
        }
    }
}
