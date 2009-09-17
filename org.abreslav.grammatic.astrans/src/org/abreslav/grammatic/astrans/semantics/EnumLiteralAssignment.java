/**
 * 
 */
package org.abreslav.grammatic.astrans.semantics;

import org.eclipse.emf.ecore.EEnumLiteral;

public class EnumLiteralAssignment extends SemanticalAssignment {
	private final SemanticalAttribute myAttribute;
	private final EEnumLiteral myLiteral;
	
	public EnumLiteralAssignment(SemanticalAttribute attribute,
			EEnumLiteral literal) {
		myAttribute = attribute;
		myLiteral = literal;
	}
	
	public SemanticalAttribute getAttribute() {
		return myAttribute;
	}
	
	public EEnumLiteral getLiteral() {
		return myLiteral;
	}
	
}