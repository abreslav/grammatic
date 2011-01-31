package org.abreslav.grammar;

import java.util.Collection;

/**
 * @author abreslav
 */
public interface ICollectionExpression extends IExpression {
    Collection<IExpression> getExpressions();
}
