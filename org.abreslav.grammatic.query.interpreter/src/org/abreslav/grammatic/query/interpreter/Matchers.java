package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;

public class Matchers {

	private final AttributeMatcher myAttributeMatcher;
	private final ExpressionMatcher myExpressionMatcher;
	private final RuleMatcher myRuleMatcher;
	private final GrammarTraverser myGrammarTraverser;

	public Matchers(IMetadataProvider metadataProvider) {
		myAttributeMatcher = new AttributeMatcher(metadataProvider);
		myExpressionMatcher = new ExpressionMatcher(myAttributeMatcher);
		myRuleMatcher = new RuleMatcher(myExpressionMatcher);
		myGrammarTraverser = new GrammarTraverser(myRuleMatcher);
	}
	
	public AttributeMatcher getAttributeMatcher() {
		return myAttributeMatcher;
	}
	
	public ExpressionMatcher getExpressionMatcher() {
		return myExpressionMatcher;
	}
	
	public RuleMatcher getRuleMatcher() {
		return myRuleMatcher;
	}
	
	public GrammarTraverser getGrammarTraverser() {
		return myGrammarTraverser;
	}
}
