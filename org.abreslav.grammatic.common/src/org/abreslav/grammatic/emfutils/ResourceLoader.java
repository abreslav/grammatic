package org.abreslav.grammatic.emfutils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.URIHandler;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;

/**
 * @author ykiselev
 * @author efadina
 * @author abreslav
 */
public class ResourceLoader implements IResourceLoader {
	private class NormalizingURIHandler extends URIHandlerImpl {
		@Override
		public void setBaseURI(URI uri) {
			uri = myURIConverter.normalize(uri.trimSegments(1).appendSegment(""));
			super.setBaseURI(uri);
		}

		@Override
		public URI resolve(URI uri) {
			URI resolve = super.resolve(uri.resolve(baseURI));
			return resolve;
		}
		
		@Override
		public URI deresolve(URI uri) {
			URI deresolve = super.deresolve(myURIConverter.normalize(uri));
			return deresolve;
		}
	}

	private final ResourceSet myResourceSet;
	private final ResourceURIConverter myURIConverter;
	private final URIHandler myLoadURIHandler = new NormalizingURIHandler();
	private final BasicExtendedMetaData myExtendedMetadatata = new BasicExtendedMetaData(new EPackageRegistryImpl());
	private final Map<String, URI> mySchemaLocations = new HashMap<String, URI>();
	private final URIHandlerImpl mySaveURIHandler = new NormalizingURIHandler();

	public ResourceLoader(String root) {
		this(new File(root));
	}
	
	public ResourceLoader(File root) {
		myResourceSet = new ResourceSetImpl() {
			
			@Override
			protected void demandLoad(final Resource resource) throws IOException {
				resource.load(Collections.singletonMap(XMIResource.OPTION_URI_HANDLER, new NormalizingURIHandler()));
			}
			
		};
		myResourceSet.setResourceFactoryRegistry(new ResourceFactoryRegistryImpl());
		myURIConverter = new ResourceURIConverter(root);
		myResourceSet.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, myExtendedMetadatata);
		myResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());

		myResourceSet.setURIConverter(myURIConverter);
		
	}

	private Resource loadResource(String path) throws IOException {
		Resource resource = myResourceSet.createResource(URI
				.createFileURI(path));
		resource.load(Collections.singletonMap(XMIResource.OPTION_URI_HANDLER, myLoadURIHandler));
		EcoreUtil.resolveAll(myResourceSet);
		return resource;
	}

	public EObject loadStaticModel(String path, EPackage... ePackages)
			throws IOException {
		Resource resource = loadResourceAsStatic(path, ePackages);
		return resource.getContents().get(0);
	}

	public Resource loadResourceAsStatic(String path, EPackage... ePackages)
			throws IOException {
		for (EPackage ePackage : ePackages) {
//			myExtendedMetadatata.putPackage(ePackage.getNsURI(), ePackage);
			myResourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		}
		Resource resource = loadResource(path);
		return resource;
	}

	public List<EObject> loadDynamicModel(List<String> xmiPaths)
			throws IOException {
		List<EObject> models = new LinkedList<EObject>();
		for (String path : xmiPaths) {
			models.add(loadResource(path).getContents().get(0));
		}
		return models;
	}
	
	public ResourceSet getResourceSet() {
		return myResourceSet;
	}
	
	public void save(String path, EObject... objects) throws IOException {
		HashMap<Object,Object> map = new HashMap<Object, Object>();
		map.put(XMIResource.OPTION_URI_HANDLER, mySaveURIHandler);
		map.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		XMLOptionsImpl value = new XMLOptionsImpl();
		value.setExternalSchemaLocations(mySchemaLocations);
		value.setProcessSchemaLocations(true);
		map.put(XMLResource.OPTION_XML_OPTIONS, value);
		createResourceForObjects(path, objects).save(map);
	}

	public void print(EObject... objects) {
		try {
			printToSteam(System.out, Collections.singletonMap(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD), objects);
		} catch (IOException e) {
			// we are printing to System.out
			throw new IllegalStateException(e);
		}
	}

	public void printToSteam(OutputStream out, Map<?, ?> options, EObject... objects)
			throws IOException {
		createResourceForObjects("some.xmi", objects).save(out, options);
	}
	
	private Resource createResourceForObjects(String path, EObject... objects) {
		Resource resource = myResourceSet.createResource(URI.createFileURI(path));
		resource.getContents().addAll(Arrays.asList(objects));
		return resource;
	}
	
	public void putSchemaLocation(String ns, URI location) {
		mySchemaLocations.put(ns, location);
	}
}
