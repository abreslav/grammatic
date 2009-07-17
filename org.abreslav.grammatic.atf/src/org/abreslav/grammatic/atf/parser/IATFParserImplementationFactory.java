/**
 * 
 */
package org.abreslav.grammatic.atf.parser;

import java.io.File;
import java.io.IOException;

import org.abreslav.grammatic.utils.FileLocator;
import org.abreslav.grammatic.utils.IErrorHandler;

public interface IATFParserImplementationFactory {
	IATFParserImplementation createParserImplementation(IATFModuleLoader parserFrontEnd, 
			File file, 
			ITypeSystemBuilder<?> typeSystemBuilder, 
			FileLocator fileLocator, 
			IErrorHandler<RuntimeException> errorHandler) throws IOException;
}