package org.abreslav.grammatic.astrans.semantics;

import org.eclipse.emf.ecore.EClassifier;

public class SemanticalAttribute {
	
	private final String myName;
	private final EClassifier myType;
	
	public SemanticalAttribute(String name, EClassifier type) {
		myName = name;
		myType = type;
	}
	
	public final String getName() {
		return myName;
	}
	
	public final EClassifier getType() {
		return myType;
	}
}
