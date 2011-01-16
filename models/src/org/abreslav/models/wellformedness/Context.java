package org.abreslav.models.wellformedness;

import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;
import org.abreslav.models.util.TraverseValueTreeVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author abreslav
 */
public class Context implements IContext {

    private final Map<IValue, ObjectValue> objects = new LinkedHashMap<IValue, ObjectValue>();

    public Context() {
    }

    public Context(IValue model) {
        model.accept(new TraverseValueTreeVisitor<Void, Void>() {
            @Override
            public Void visitObject(ObjectValue value, Void data) {
                putObject(value);
                return super.visitObject(value, data);
            }
        }, null);
    }

    public Context(IContext context) {
        for (ObjectValue objectValue : context.getAllObjects()) {
            putObject(objectValue);
        }
    }

    public ObjectValue getObject(IValue identity) {
        return objects.get(identity);
    }

    public final void putObject(ObjectValue object) {
        assert object != null;
        IValue identity = object.getIdentity();
        assert identity != null;

        ObjectValue objectValue = objects.get(identity);
        if (objectValue != null && objectValue != object) {
            throw new IllegalArgumentException("Duplicate identity in the context: " + identity);
        }

        objects.put(identity, object);
    }

    public Collection<ObjectValue> getAllObjects() {
        return Collections.unmodifiableCollection(objects.values());
    }
}
