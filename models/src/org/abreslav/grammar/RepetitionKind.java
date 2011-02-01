package org.abreslav.grammar;

/**
 * @author abreslav
 */
public enum RepetitionKind {
    ONE_OR_MORE("+"),
    ZERO_OR_MORE("*");

    private final String label;

    RepetitionKind(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
