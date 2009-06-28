package org.abreslav.grammatic.grammar.aspects;

public interface IGrammarAspectFunctions<S, R> {

	void remove(S subject);
	void instead(S subject, R replacement);
	void before(S subject, R addition);
	void after(S subject, R addition);
	
}
