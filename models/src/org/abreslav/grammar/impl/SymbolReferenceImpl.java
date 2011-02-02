package org.abreslav.grammar.impl;

import org.abreslav.grammar.ExpressionVisitor;
import org.abreslav.grammar.ISymbol;
import org.abreslav.grammar.ISymbolReference;

/**
 * @author abreslav
 */
public class SymbolReferenceImpl implements ISymbolReference {
    private ISymbol referencedSymbol;

    public SymbolReferenceImpl(ISymbol referencedSymbol) {
        this.referencedSymbol = referencedSymbol;
    }

    public ISymbol getReferencedSymbol() {
        return referencedSymbol;
    }

    public void setReferencedSymbol(ISymbol referencedSymbol) {
        assert this.referencedSymbol == null;
        this.referencedSymbol = referencedSymbol;
    }

    public <R, D> R accept(ExpressionVisitor<R, D> visitor, D data) {
        return visitor.visitSymbolReference(this, data);
    }

    @Override
    public String toString() {
        return referencedSymbol == null ? "<null>" : referencedSymbol.getName();
    }
}
