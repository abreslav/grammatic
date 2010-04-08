package org.abreslav.grammatic.atf.interpreter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;


public class AspectApplication {
	private String myGrammarName;
	private String myAspectName;
	
	public void setAspectName(String aspectName) {
		myAspectName = aspectName;
	}
	
	public void setGrammarName(String grammarName) {
		myGrammarName = grammarName;
	}
	
	public String getAspectName() {
		return myAspectName;
	}
	
	public String getGrammarName() {
		return myGrammarName;
	}

	public static Collection<AspectApplication> createApplicationsFromDirectory(
			File baseDir) {
		Collection<AspectApplication> aspectAppliactions = new ArrayList<AspectApplication>();
		for (File file : baseDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".atf");
			}
		})) {
			AspectApplication aspectApplication = new AspectApplication();
			String baseName = file.getName();
			aspectApplication.setAspectName(baseName);
			aspectApplication.setGrammarName(baseName.replace(".atf", ".grammar"));
			aspectAppliactions.add(aspectApplication);
		}
		return aspectAppliactions;
	}
}