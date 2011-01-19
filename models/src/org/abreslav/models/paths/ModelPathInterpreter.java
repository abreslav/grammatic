package org.abreslav.models.paths;

import org.abreslav.models.ICollectionValue;
import org.abreslav.models.IValue;
import org.abreslav.models.ObjectValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.abreslav.models.util.CastUtils.cast;

/**
 * @author abreslav
 */
public class ModelPathInterpreter {
    public IValue getValueByPath(IValue start, Iterable<IModelPathEntry> path) {
        IValue result = start;
        for (IModelPathEntry entry : path) {
            result = entry.accept(new IModelPathVisitor<IValue, IValue>() {
                public IValue visitProperty(PropertyPathEntry entry, IValue data) {
                    ObjectValue objectValue = cast(data, ObjectValue.class, "Object expected but " + data + " found at " + entry);
                    return objectValue.getPropertyValue(entry.getPropertyKey());
                }

                public IValue visitCollectionRange(CollectionRangePathEntry entry, IValue data) {
                    ICollectionValue collectionValue = cast(data, ICollectionValue.class, "Collection expected but " + data + " found at " + entry);
                    List<IValue> list = new ArrayList<IValue>();
                    int i = 0;
                    for (IValue value : collectionValue.getValue()) {
                        if (i > entry.getLastIndex()) break;
                        if (i >= entry.getFirstIndex()) {
                            list.add(value);
                        }
                        i++;
                    }
                    if (list.size() < entry.size()) {
                        throw new IllegalArgumentException("To few arguments in the collection: " + collectionValue + " expecting " + entry);
                    }
                    return collectionValue.createNewFromList(list);
                }

                public IValue visitCollectionItem(CollectionItemPathEntry entry, IValue data) {
                    ICollectionValue collectionValue = cast(data, ICollectionValue.class, "Collection expected but " + data + " found at " + entry);
                    Collection<IValue> value = collectionValue.getValue();
                    Iterator<IValue> iterator = value.iterator();
                    IValue result = null;
                    for (int i = 0; i < entry.getIndex() - 1; i++) {
                        result = iterator.next();
                    }
                    if (result == null) {
                        throw new IllegalArgumentException("Unexisting index " + entry.getIndex() + " in collection " + collectionValue);
                    }
                    return result;
                }
            }, result);
        }
        return result;
    }
}
