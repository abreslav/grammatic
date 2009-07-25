package org.abreslav.grammatic.atf.interpreter;


public class AspectApplication {
	private String myGrammarName;
	private String myAspectName;
	
	public void setAspectName(String aspectName) {
		myAspectName = aspectName;
	}
	
	public void setGrammarName(String grammarName) {
		myGrammarName = grammarName;
	}
	
	public String getAspectName() {
		return myAspectName;
	}
	
	public String getGrammarName() {
		return myGrammarName;
	}
}