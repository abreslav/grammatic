package org.abreslav.grammar;

/**
 * @author abreslav
 */
public class ExpressionVisitor<R, D> {
    public R visitExpression(IExpression expression, D data) {
        return null;
    }

    public R visitSymbolReference(ISymbolReference symbolReference, D data) {
        return visitExpression(symbolReference, data);
    }

    public R visitLiteral(ILiteral literal, D data) {
        return visitExpression(literal, data);
    }

    public R visitCharacterRange(ICharacterRange characterRange, D data) {
        return visitExpression(characterRange, data);
    }

    public R visitEmpty(IEmpty empty, D data) {
        return visitExpression(empty, data);
    }

    public R visitAlternative(IAlternative alternative, D data) {
        return visitExpression(alternative, data);
    }

    public R visitSequence(ISequence sequence, D data) {
        return visitExpression(sequence, data);
    }

    public R visitOption(IOption option, D data) {
        return visitExpression(option, data);
    }

    public R visitRepetition(IRepetition repetition, D data) {
        return visitExpression(repetition, data);
    }
}
