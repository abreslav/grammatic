package org.abreslav.grammatic.utils;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileLocator {

	private final Set<File> myBaseDirs = new HashSet<File>();
	
	public FileLocator(Collection<File> baseDirs) {
		myBaseDirs.addAll(baseDirs);
	}
	
	public FileLocator(File baseDir) {
		myBaseDirs.add(baseDir);
	}
	
	public File findFile(String fileName) {
		for (File dir : myBaseDirs) {
			File file = new File(dir, fileName);
			if (file.exists() && file.isFile()) {
				return file;
			}
		}
		return null;
	}
}
