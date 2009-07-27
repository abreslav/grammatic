package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Map;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;

public class SymbolMetadataProvider {
	private final Map<Symbol, IMetadataStorage> mySymbolsMetadata = EMFProxyUtil.customHashMap();
	private final IMetadataProvider myMetadataProvider;

	public SymbolMetadataProvider(IMetadataProvider metadataProvider) {
		myMetadataProvider = metadataProvider;
	}

	public IMetadataStorage getSymbolMetadata(Symbol symbol) {
		IMetadataStorage metadata = mySymbolsMetadata.get(symbol);
		if (metadata == null) {
			metadata = MetadataStorage.getMetadataStorage(symbol, myMetadataProvider);
			mySymbolsMetadata.put(symbol, metadata);
		}
		return metadata;		
	}

}
