package org.abreslav.models.paths;

/**
 * @author abreslav
 */
public class CollectionItemPathEntry implements IModelPathEntry {
    private final int index;

    public CollectionItemPathEntry(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public <R, D> R accept(IModelPathVisitor<R, D> visitor, D data) {
        return visitor.visitCollectionItem(this, data);
    }

    @Override
    public String toString() {
        return "CollectionItemPathEntry{" +
                "index=" + index +
                '}';
    }
}
