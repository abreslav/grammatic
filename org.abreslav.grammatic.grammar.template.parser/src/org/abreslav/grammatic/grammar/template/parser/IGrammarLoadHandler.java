/**
 * 
 */
package org.abreslav.grammatic.grammar.template.parser;

import org.abreslav.grammatic.grammar.Grammar;

public interface IGrammarLoadHandler {
	
	IGrammarLoadHandler NONE = new IGrammarLoadHandler() {

		@Override
		public void grammarLoaded(String fileName, Grammar grammar) {
		}
	};
	
	void grammarLoaded(String fileName, Grammar grammar);
}