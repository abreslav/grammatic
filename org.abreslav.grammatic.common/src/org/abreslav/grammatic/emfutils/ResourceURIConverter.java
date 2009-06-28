package org.abreslav.grammatic.emfutils;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

public class ResourceURIConverter extends ExtensibleURIConverterImpl {
	private final URI myRoot;

	public ResourceURIConverter(File rootFolder) {
		myRoot = URI.createFileURI(rootFolder.getAbsolutePath() + "/");
	}

	public URI normalize(URI uri) {
	    String fragment = uri.fragment();
	    URI result =
	      fragment == null ?
	        getInternalURIMap().getURI(uri) :
	        getInternalURIMap().getURI(uri.trimFragment()).appendFragment(fragment);
		URI resolve = result.resolve(myRoot);
		return resolve;
	}
	
}
