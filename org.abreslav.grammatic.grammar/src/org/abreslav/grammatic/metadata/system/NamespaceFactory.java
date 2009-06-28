package org.abreslav.grammatic.metadata.system;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class NamespaceFactory implements Factory {

	private static Resource ourResource = null;
	
	@Override
	public Resource createResource(URI uri) {
		if (uri.toString().equals(ISystemMetadata.SYSTEM_NAMESPACE_URI)) {
			if (ourResource == null) {
				ourResource = new ResourceSetImpl().createResource(URI.createFileURI("system"));
				ourResource.getContents().add(ISystemMetadata.SYSTEM_NAMESPACE);
			}
			return ourResource; 
		}
		return null;
	}

}
