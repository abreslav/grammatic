package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpressionVisitor;
import org.abreslav.grammar.ISequence;

/**
 * @author abreslav
 */
public class SequenceImpl extends CollectionExpressionImpl implements ISequence {
    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitSequence(this, data);
    }
}
