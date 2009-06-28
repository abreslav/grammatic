package org.abreslav.grammatic.metadata.aspects.manager;

import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.eclipse.emf.ecore.EObject;

public interface IAttributeProvider {
	
	Set<Attribute> getAttributes(EObject subject);
}
