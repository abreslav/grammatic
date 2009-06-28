package org.abreslav.grammatic.metadata.aspectdef.interpreter;

import java.util.List;

import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.query.variables.util.VariablesSwitch;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;

public class AssignmentHandler extends VariablesSwitch<INull, List<Attribute>> {

	public void caseGrammar(Grammar grammar, List<Attribute> assignment) {
		defaultCase(grammar, null);
	}
	
	@Override
	public INull defaultCase(EObject object, List<Attribute> data) {
		if (object instanceof Grammar) {
			caseGrammar((Grammar) object, data);
			return INull.NULL;
		}
		throw new IllegalStateException("Unsupported object: " + object);
	}
	
}
