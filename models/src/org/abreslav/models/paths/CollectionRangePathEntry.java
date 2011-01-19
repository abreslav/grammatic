package org.abreslav.models.paths;

/**
 * @author abreslav
 */
public class CollectionRangePathEntry implements IModelPathEntry {
    private final int firstIndex;
    private final int lastIndex;

    public CollectionRangePathEntry(int firstIndex, int lastIndex) {
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public boolean isSingleEntry() {
        return firstIndex == lastIndex;
    }

    public int size() {
        return lastIndex - firstIndex + 1;
    }

    public <R, D> R accept(IModelPathVisitor<R, D> visitor, D data) {
        return visitor.visitCollectionRange(this, data);
    }

    @Override
    public String toString() {
        return "CollectionRangePathEntry{" +
                "firstIndex=" + firstIndex +
                ", lastIndex=" + lastIndex +
                '}';
    }
}
