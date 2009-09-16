package org.abreslav.grammatic.astrans;

import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class EPackageDescriptor {

	private final String myName;
	private final String myNsURI;
	private final String myNsPrefix;

	public EPackageDescriptor(IMetadataStorage grammarMetadata) {
		myName = grammarMetadata.readString("ePackageName");
		myNsURI = grammarMetadata.readString("ePackageNsURI");
		myNsPrefix = grammarMetadata.readString("ePackageNsPrefix");
	}
	
	public String getName() {
		return myName;
	}

	public String getNsURI() {
		return myNsURI;
	}

	public String getNsPrefix() {
		return myNsPrefix;
	}

}
