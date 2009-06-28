package org.abreslav.grammatic.emfutils;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

public interface IResourceLoader {
	public EObject loadStaticModel(String path, EPackage... ePackages)
			throws IOException;

	public List<EObject> loadDynamicModel(List<String> xmiPaths)
			throws IOException;
}
