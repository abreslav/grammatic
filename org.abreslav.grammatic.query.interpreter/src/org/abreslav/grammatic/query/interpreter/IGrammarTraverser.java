package org.abreslav.grammatic.query.interpreter;

import java.util.Collection;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.query.Query;
import org.abreslav.grammatic.query.QueryContainer;

public interface IGrammarTraverser {

	void traverseGrammar(Grammar grammar, Collection<QueryContainer<? extends Query>> queries, IMatchHandler matchHandler);
}
