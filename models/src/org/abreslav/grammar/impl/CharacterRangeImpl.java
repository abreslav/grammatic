package org.abreslav.grammar.impl;

import org.abreslav.grammar.ICharacterRange;
import org.abreslav.grammar.IExpressionVisitor;

/**
 * @author abreslav
 */
public class CharacterRangeImpl implements ICharacterRange {
    private final char from;
    private final char to;

    public CharacterRangeImpl(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitCharacterRange(this, data);
    }
}
