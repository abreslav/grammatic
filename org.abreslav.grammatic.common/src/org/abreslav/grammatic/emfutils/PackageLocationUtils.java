package org.abreslav.grammatic.emfutils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

public class PackageLocationUtils {
	public static void restoreLocation(EPackage ePackage, URI oldUri) {
		ePackage.eResource().setURI(oldUri);
	}

	public static URI temporarilyChangeLocation(String location,
			EPackage ePackage) {
		Resource resource = ePackage.eResource();
		URI oldUri = resource.getURI();
		if (location != null) {
			resource.setURI(URI.createURI(location));
		}
		return oldUri;
	}
	
}
