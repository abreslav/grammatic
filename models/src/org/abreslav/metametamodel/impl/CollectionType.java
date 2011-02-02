package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.ICollectionType;
import org.abreslav.metametamodel.IType;

/**
 * @author abreslav
 */
public abstract class CollectionType implements ICollectionType {
    private final boolean nonempty;
    private final IType elementType;

    protected CollectionType(boolean nonempty, IType elementType) {
        this.nonempty = nonempty;
        this.elementType = elementType;
    }

    public boolean isNonempty() {
        return nonempty;
    }

    public IType getElementType() {
        return elementType;
    }
}
