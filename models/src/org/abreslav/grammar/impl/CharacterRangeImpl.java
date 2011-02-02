package org.abreslav.grammar.impl;

import org.abreslav.grammar.ICharacterRange;
import org.abreslav.grammar.IExpressionVisitor;

/**
 * @author abreslav
 */
public class CharacterRangeImpl implements ICharacterRange {
    private final char from;
    private final char to;
    private final boolean negated;

    public CharacterRangeImpl(boolean negated, char from, char to) {
        this.negated = negated;
        this.from = from;
        this.to = to;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }

    public boolean isNegated() {
        return negated;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitCharacterRange(this, data);
    }

    @Override
    public String toString() {
        String neg = negated ? "!" : "";
        if (from != to) {
            return "['" + neg + escape(from) + "'-'" + escape(to) + "']";
        }
        return neg + "'" + escape(from) + "'";
    }

    private String escape(char c) {
        switch (c) {
            case '\b': return "\\b";
            case '\f': return "\\f";
            case '\n': return "\\n";
            case '\t': return "\\t";
            case '\r': return "\\r";
            case '\u0000': return "\\u0000";
        }
        return c + "";
    }
}
