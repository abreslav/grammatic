package org.abreslav.grammar;

/**
 * @author abreslav
 */
public interface ISymbolReference extends IExpression {
    ISymbol getReferencedSymbol();
}
