package org.abreslav.models.paths;

/**
 * @author abreslav
 */
public interface IModelPathEntry {
    <R, D> R accept(IModelPathVisitor<R, D> visitor, D data);
}
