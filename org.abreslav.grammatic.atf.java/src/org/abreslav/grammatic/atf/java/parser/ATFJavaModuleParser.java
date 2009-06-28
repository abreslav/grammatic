package org.abreslav.grammatic.atf.java.parser;

import org.abreslav.grammatic.atf.parser.ATFModuleParser;
import org.abreslav.grammatic.utils.FileLocator;

public class ATFJavaModuleParser extends ATFModuleParser {

	public ATFJavaModuleParser(FileLocator fileLocator, JavaTypeSystemBuilder typeSystemBuilder) {
		super(typeSystemBuilder, new ATFJavaParserImplementationFactory(typeSystemBuilder), fileLocator);
	}
	
	public ATFJavaModuleParser(FileLocator fileLocator) {
		this(fileLocator, new JavaTypeSystemBuilder());
	}

}
