package org.abreslav.models.paths;

import java.util.*;

/**
 * @author abreslav
 */
public class ModelPath {
    private final List<IModelPathEntry> entries = new ArrayList<IModelPathEntry>();

    public ModelPath(IModelPathEntry... entries) {
        this.entries.addAll(Arrays.asList(entries));
    }

    public ModelPath(Collection<IModelPathEntry> entries) {
        this.entries.addAll(entries);
    }

    public ModelPath() {
    }

    public void addEntry(IModelPathEntry entry) {
        this.entries.add(entry);
    }

    public Iterable<IModelPathEntry> getEntries() {
        return Collections.unmodifiableCollection(entries);
    }

    public ModelPath append(IModelPathEntry entry) {
        ModelPath modelPath = new ModelPath(entries);
        modelPath.addEntry(entry);
        return modelPath;
    }
}
