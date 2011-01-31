package org.abreslav.grammar.impl;

import org.abreslav.grammar.IExpressionVisitor;
import org.abreslav.grammar.ISymbol;
import org.abreslav.grammar.ISymbolReference;

/**
 * @author abreslav
 */
public class SymbolReferenceImpl implements ISymbolReference {
    private final ISymbol referencedSymbol;

    public SymbolReferenceImpl(ISymbol referencedSymbol) {
        this.referencedSymbol = referencedSymbol;
    }

    public ISymbol getReferencedSymbol() {
        return referencedSymbol;
    }

    public <R, D> R accept(IExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitSymbolReference(this, data);
    }
}
