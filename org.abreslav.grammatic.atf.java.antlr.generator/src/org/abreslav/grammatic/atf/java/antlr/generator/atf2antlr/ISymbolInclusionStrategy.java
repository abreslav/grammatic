package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public interface ISymbolInclusionStrategy {
	boolean doInclude(Symbol symbol, boolean isFront, IMetadataStorage metadata);
}