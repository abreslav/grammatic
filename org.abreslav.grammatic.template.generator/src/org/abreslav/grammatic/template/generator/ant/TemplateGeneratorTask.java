package org.abreslav.grammatic.template.generator.ant;

import java.io.File;
import java.io.IOException;

import org.abreslav.grammatic.emfutils.PackageLocationUtils;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.template.TemplatePackage;
import org.abreslav.grammatic.template.generator.EcoreToTemplateModels;
import org.abreslav.grammatic.template.generator.InstantiatorBuilder;
import org.abreslav.grammatic.template.instantiator.InstantiatorPackage;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

public class TemplateGeneratorTask extends Task {

	private String mySourceEcoreUri;
	private String myTemplateEcoreUri;
	private String myInstantiatorUri;
	private String myBaseDir;
	private String myBaseTemplateEcoreUri;
	private String myInstantiatorEcoreUri;
	
	public void execute() throws BuildException {
		try {
			File baseDir;
			log("Generating templates for " + mySourceEcoreUri, 1);
			if (myBaseDir == null) {
				log("Generation failed: no base directory specified", 0);
				return;
			} else {
				baseDir = new File(myBaseDir);
				log("Base dir is " + baseDir.getAbsolutePath());
			}
			ResourceLoader resourceLoader = new ResourceLoader(baseDir);
			resourceLoader.getResourceSet().getURIConverter().getURIMap().put(
					URI.createURI(TemplatePackage.eINSTANCE.getNsURI()), 
					URI.createURI(myBaseTemplateEcoreUri));
			resourceLoader.putSchemaLocation(
					InstantiatorPackage.eINSTANCE.getNsURI(), 
					URI.createURI("tralala"));
			InstantiatorBuilder traceBuilder = new InstantiatorBuilder();
			EcoreToTemplateModels models = new EcoreToTemplateModels(traceBuilder);
			EPackage templatePackage = models.generateTemplateEcore(getSourcePackage(resourceLoader, baseDir));
			resourceLoader.save(myTemplateEcoreUri, templatePackage);
			if (myInstantiatorUri != null) {
				URI oldUri = PackageLocationUtils.temporarilyChangeLocation(myInstantiatorEcoreUri, InstantiatorPackage.eINSTANCE);
				resourceLoader.save(myInstantiatorUri, traceBuilder.getModel());
				PackageLocationUtils.restoreLocation(InstantiatorPackage.eINSTANCE, oldUri);
			}
			log("Generation finished", 1);
		} catch (Exception e) {
			log(e, 0);
			throw new BuildException(e);
		}
	}
	
	private EPackage getSourcePackage(ResourceLoader resourceLoader, File baseDir) throws IOException {
		return (EPackage) resourceLoader.loadStaticModel(mySourceEcoreUri, EcorePackage.eINSTANCE);
	}

	public void setSourceEcoreUri(String sourceEcoreUri) {
		mySourceEcoreUri = sourceEcoreUri;
	}

	public void setTemplateEcoreUri(String templateEcoreUri) {
		myTemplateEcoreUri = templateEcoreUri;
	}

	public void setInstantiatorUri(String instantiatorUri) {
		myInstantiatorUri = instantiatorUri;
	}

	public void setBaseDir(String dir) {
		myBaseDir = dir;
	}
	
	public void setBaseTemplateEcoreUri(String baseTemplateEcoreUri) {
		myBaseTemplateEcoreUri = baseTemplateEcoreUri;
	}
	
	public void setInstantiatorEcoreUri(String instantiatorEcoreUri) {
		myInstantiatorEcoreUri = instantiatorEcoreUri;
	}
	
}