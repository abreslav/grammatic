package org.abreslav.grammatic.astrans.descriptors;

import org.abreslav.grammatic.metadata.util.IMetadataStorage;

public class EStructuralFeatureDescriptor {

	private final String myName;

	public EStructuralFeatureDescriptor(IMetadataStorage metadata) {
		myName = metadata.readId("name");
	}
	
	public String getName() {
		return myName;
	}
	
}
