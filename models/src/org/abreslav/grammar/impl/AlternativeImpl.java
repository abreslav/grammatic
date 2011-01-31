package org.abreslav.grammar.impl;

import org.abreslav.grammar.IAlternative;
import org.abreslav.grammar.IExpressionVisitor;

/**
 * @author abreslav
 */
public class AlternativeImpl extends CollectionExpressionImpl implements IAlternative {
    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitAlternative(this, data);
    }
}
