package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.system.ISystemMetadata;

/**
 * A preprocessor is responsible for attaching system metadata to grammar objects
 * 
 * For attributes and their possible values see {@link ISystemMetadata} 
 */
public interface IModelPreprocessor {

	void attachSystemMetadata(Grammar grammar);
	void attachSystemMetadataToASymbol(Symbol symbol);
}
