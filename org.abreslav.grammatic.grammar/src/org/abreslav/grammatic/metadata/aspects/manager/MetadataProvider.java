package org.abreslav.grammatic.metadata.aspects.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.aspects.AttributeAssignment;
import org.abreslav.grammatic.metadata.aspects.MetadataAspect;
import org.eclipse.emf.ecore.EObject;

public class MetadataProvider implements IMetadataProvider {

	private final Map<EObject, Set<Attribute>> myAttributes = EMFProxyUtil.customHashMap();
	
	private MetadataProvider(MetadataProvider parent, Set<Namespace> namespaces) {
		for (Entry<EObject, Set<Attribute>> entry : parent.myAttributes.entrySet()) {
			Set<Attribute> attributes = getOrCreateAttributes(entry.getKey());
			for (Attribute attribute : entry.getValue()) {
				if (namespaces.contains(attribute.getNamespace())) {
					attributes.add(attribute);
				}
			}
		}
	}
	
	public MetadataProvider(MetadataAspect aspect) {
		this(Collections.singleton(aspect), null);
	}
	
	public MetadataProvider(Collection<? extends MetadataAspect> aspects, Set<Namespace> namespaces) {
		for (MetadataAspect metadataAspect : aspects) {
			for (AttributeAssignment attributeAssignment : metadataAspect.getAssignments()) {
				EObject subject = attributeAssignment.getSubject();
				Set<Attribute> attributes = getOrCreateAttributes(subject);
				addAttributes(attributes, attributeAssignment.getAttributes(), namespaces);
			}
		}
	}

	private Set<Attribute> getOrCreateAttributes(EObject subject) {
		Set<Attribute> attributes = myAttributes.get(subject);
		if (attributes == null) {
			attributes = new HashSet<Attribute>();
			myAttributes.put(subject, attributes);
		}
		return attributes;
	}
	
	private void addAttributes(Set<Attribute> to,
			List<Attribute> from, Set<Namespace> namespaces) {
		if (namespaces == null) {
			to.addAll(from);
		} else {
			for (Attribute attribute : from) {
				if (namespaces.contains(attribute.getNamespace())) {
					to.add(attribute);
				}
			}
		}
	}

	@Override
	public Set<Attribute> getAttributes(EObject subject) {
		Set<Attribute> attributes = myAttributes.get(subject);
		return attributes == null 
			? Collections.<Attribute>emptySet() 
			: attributes;
	}

	@Override
	public IMetadataProvider getProjection(Namespace namespace) {
		return new MetadataProvider(this, Collections.singleton(namespace));
	}

	@Override
	public IMetadataProvider getProjection(Set<Namespace> namespaces) {
		return new MetadataProvider(this, namespaces);
	}

	
}
