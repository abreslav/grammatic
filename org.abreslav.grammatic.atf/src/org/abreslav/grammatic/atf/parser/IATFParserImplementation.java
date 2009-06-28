/**
 * 
 */
package org.abreslav.grammatic.atf.parser;

import org.abreslav.grammatic.atf.SemanticModule;
import org.abreslav.grammatic.metadata.aspectdef.AspectDefinition;
import org.antlr.runtime.RecognitionException;

public interface IATFParserImplementation {
	AspectDefinition atfModule() throws RecognitionException;
	SemanticModule semanticModule() throws RecognitionException;
	void typeSystemModule() throws RecognitionException;
	String getPositionString();
}