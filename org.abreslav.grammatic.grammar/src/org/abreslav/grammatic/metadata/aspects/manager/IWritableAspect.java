package org.abreslav.grammatic.metadata.aspects.manager;

import java.util.Collections;
import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.Value;
import org.eclipse.emf.ecore.EObject;

public interface IWritableAspect extends IAttributeProvider {
	IWritableAspect NONE = new IWritableAspect() {

		@Override
		public void setAttribute(EObject subject, Namespace namespace,
				String name, Value value) {
		}

		@Override
		public void copyAttributes(EObject from, EObject to) {
		}

		@Override
		public Set<Attribute> getAttributes(EObject subject) {
			return Collections.emptySet();
		}
		
	};
	
	IWritableAspect ERROR = new IWritableAspect() {
		
		@Override
		public void setAttribute(EObject subject, Namespace namespace,
				String name, Value value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void copyAttributes(EObject from, EObject to) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Set<Attribute> getAttributes(EObject subject) {
			throw new UnsupportedOperationException();
		}
		
	};
	
	void setAttribute(EObject subject, Namespace namespace, String name, Value value);
	void copyAttributes(EObject from, EObject to);
}
