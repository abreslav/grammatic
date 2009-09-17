package org.abreslav.grammatic.astrans.semantics;

public abstract class SemanticalReference {
	private final SemanticalAttribute myVariable;

	public SemanticalReference(SemanticalAttribute variable) {
		myVariable = variable;
	}
	
	public final SemanticalAttribute getVariable() {
		return myVariable;
	}
}