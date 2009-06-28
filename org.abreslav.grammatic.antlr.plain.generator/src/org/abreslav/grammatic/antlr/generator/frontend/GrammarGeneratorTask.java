package org.abreslav.grammatic.antlr.generator.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class GrammarGeneratorTask extends Task {

	public static class ImportDir {
		
		private String myPath;
		
		public ImportDir(Project project) {
			
		}
		
		public void setPath(String path) {
			myPath = path;
		}
		
		public String getPath() {
			return myPath;
		}
	}
	
	private String myGrammarName;
	private String myGrammarDir = "./";
	private String myGeneratedGrammarDir = "./";
	private String myTargetPackageDir = "./";
	private final Collection<File> myGrammarPath = new LinkedHashSet<File>();
	private final List<AspectApplication> myAspectApplications = new ArrayList<AspectApplication>();
	
	public void execute() throws BuildException {
		try {
			log(String.format("Generation of %s started", myGrammarName), 0);
			myGrammarPath.add(new File(myGrammarDir).getAbsoluteFile());
			myGrammarPath.add(new File(".").getAbsoluteFile());
			ANTLRGrammarGenerator.generate(
					myGrammarName, 
					myGrammarDir, 
					myGeneratedGrammarDir,
					myTargetPackageDir, 
					myGrammarPath,
					myAspectApplications
			);
			log("Generation finished", 0);
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		} catch (RecognitionException e) {
			throw new BuildException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
	
	public void setGrammarName(String grammarName) {
		myGrammarName = grammarName;
	}
	
	public void setGrammarDir(String grammarDir) {
		myGrammarDir = grammarDir;
	}
	
	public void setGeneratedGrammarDir(String generatedGrammarDir) {
		myGeneratedGrammarDir = generatedGrammarDir;
	}
	
	public void setTargetPackageDir(String targetPackageDir) {
		myTargetPackageDir = targetPackageDir;
	}
	
	public void addConfiguredImportDir(ImportDir importDir) {
		myGrammarPath.add(new File(importDir.getPath()).getAbsoluteFile());
	}
	
	public void addConfiguredAspectApplication(AspectApplication application) {
		myAspectApplications.add(application);
	}
}
