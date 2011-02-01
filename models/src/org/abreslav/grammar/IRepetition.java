package org.abreslav.grammar;

/**
 * @author abreslav
 */
public interface IRepetition extends IExpression {
    IExpression getBody();
    RepetitionKind getKind();
}
