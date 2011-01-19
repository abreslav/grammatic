package org.abreslav.models.paths;

/**
 * @author abreslav
 */
public interface IModelPathVisitor<R, D> {
    R visitProperty(PropertyPathEntry entry, D data);
    R visitCollectionRange(CollectionRangePathEntry entry, D data);
    R visitCollectionItem(CollectionItemPathEntry entry, D data);
}
