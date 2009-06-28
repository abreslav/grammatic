package org.abreslav.grammatic.metadata.system;

import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;

/**
 * This interface defines constants for system metadata attributes
 * (located in system namespace with 
 *  URI grammatic://www.abreslav.org/Grammatic/2008/Metadata/namespaces/system)
 *  and their possible values 
 *
 */
public interface ISystemMetadata {

	/*package*/ class SystemMetadata {

		/*package*/ static final Namespace SYSTEM_NAMESPACE;
		static {
			Namespace namespace = MetadataFactory.eINSTANCE.createNamespace();
			namespace.setUri(ISystemMetadata.SYSTEM_NAMESPACE_URI);
			SYSTEM_NAMESPACE = namespace;
		}
	}

	/**
	 * System namespace object
	 */
	Namespace SYSTEM_NAMESPACE = SystemMetadata.SYSTEM_NAMESPACE;

	/**
	 * System namespace URI
	 */
	String SYSTEM_NAMESPACE_URI = "grammatic://www.abreslav.org/Grammatic/2008/Metadata/namespaces/system";

	/**
	 * The attribute denoting a starting symbol of a grammar
	 * Has no value, is present only for one symbol 
	 */
	String SYMBOL_STARTING = "starting";
	
	/**
	 * A type of a grammar symbol: terminal or nonterminal
	 * Is to be one of the following strings: {@link #SYMBOL_TYPE_NONTERMINAL}, {@link #SYMBOL_TYPE_TERMINAL} 
	 */
	String SYMBOL_TYPE = "type"; 
	
	/**
	 * This symbol is nonterminal
	 */
	String SYMBOL_TYPE_NONTERMINAL = "nonterminal"; 

	/**
	 * This symbol is terminal
	 */
	String SYMBOL_TYPE_TERMINAL = "terminal";
	
	/**
	 * A name of this symbol (given in concrete syntax)
	 * Corresponds to {@link Symbol#getName()}
	 * Is to be a string
	 */
	String SYMBOL_NAME = "name";
	
	
}
