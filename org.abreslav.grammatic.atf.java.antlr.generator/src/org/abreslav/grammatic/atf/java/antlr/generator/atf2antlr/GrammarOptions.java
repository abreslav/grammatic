package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class GrammarOptions {
	private final String myName;
	private final String myPackage;
	
	public GrammarOptions(IMetadataStorage metadataStorage) {
		myName = metadataStorage.readString(ANTLRMetadata.GRAMMAR_NAME);
		myPackage = metadataStorage.readString(ANTLRMetadata.GRAMMAR_PACKAGE);
	}

	public String getName() {
		return myName;
	}
	
	public String getPackage() {
		return myPackage;
	}
}
