package org.abreslav.grammatic.template.validator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypeParameter;

public interface IInferer {
	boolean inferTypeParameterSubstitution(ETypeParameter typeParameter,
			EObject object, ISubstitution substitution);
}
