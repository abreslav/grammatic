package org.abreslav.grammatic.metadata.aspects.manager;

import java.util.Collections;
import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Namespace;
import org.eclipse.emf.ecore.EObject;

public interface IMetadataProvider extends IAttributeProvider {
	
	IMetadataProvider EMPTY = new IMetadataProvider() {
		
		@Override
		public Set<Attribute> getAttributes(EObject subject) {
			return Collections.emptySet();
		}

		@Override
		public IMetadataProvider getProjection(Namespace namespace) {
			return this;
		}

		@Override
		public IMetadataProvider getProjection(Set<Namespace> namespaces) {
			return this;
		}
		
	};
	
	IMetadataProvider getProjection(Namespace namespace);
	IMetadataProvider getProjection(Set<Namespace> namespaces);
}
