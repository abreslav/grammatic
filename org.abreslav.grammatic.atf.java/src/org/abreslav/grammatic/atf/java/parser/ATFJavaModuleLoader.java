package org.abreslav.grammatic.atf.java.parser;

import org.abreslav.grammatic.atf.parser.ATFModuleLoader;
import org.abreslav.grammatic.utils.FileLocator;

public class ATFJavaModuleLoader extends ATFModuleLoader {

	public ATFJavaModuleLoader(FileLocator fileLocator, JavaTypeSystemBuilder typeSystemBuilder) {
		super(typeSystemBuilder, new ATFJavaParserImplementationFactory(typeSystemBuilder), fileLocator);
	}
	
	public ATFJavaModuleLoader(FileLocator fileLocator) {
		this(fileLocator, new JavaTypeSystemBuilder());
	}

}
