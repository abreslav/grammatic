package org.abreslav.grammatic.metadata.aspects.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.MetadataFactory;
import org.abreslav.grammatic.metadata.Namespace;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.AspectsFactory;
import org.abreslav.grammatic.metadata.aspects.AttributeAssignment;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class WritableAspect implements IWritableAspect {

	public interface IAspectWritingStrategy {
		Iterable<AttributeAssignment> getInitialAssignments();
		void addAssignment(AttributeAssignment assignment);
	}
	
	public static final IAspectWritingStrategy EMPTY_WRITER = new IAspectWritingStrategy() {

		@Override
		public void addAssignment(AttributeAssignment assignment) {
			
		}

		@Override
		public Iterable<AttributeAssignment> getInitialAssignments() {
			return Collections.emptySet();
		}
		
	};
	
	private final IAspectWritingStrategy myAspectWritingStrategy;
	private final Map<EObject, Map<AttributeKey, Attribute>> myCache = new HashMap<EObject, Map<AttributeKey,Attribute>>();
	private final Map<EObject, AttributeAssignment> myAssignments = new HashMap<EObject, AttributeAssignment>();
	
	public WritableAspect(IAspectWritingStrategy strategy) {
		myAspectWritingStrategy = strategy;
		for (AttributeAssignment assignment : strategy.getInitialAssignments()) {
			myAssignments.put(assignment.getSubject(), assignment);
			Map<AttributeKey, Attribute> attributes = new HashMap<AttributeKey, Attribute>();
			myCache.put(assignment, attributes);
			for (Attribute attribute : assignment.getAttributes()) {
				attributes.put(new AttributeKey(attribute), attribute);
			}
		}
	}
	
	public Set<Attribute> getAttributes(EObject subject) {
		AttributeAssignment assignment = myAssignments.get(subject);
		if (assignment == null) {
			return Collections.emptySet();
		}
		return new HashSet<Attribute>(assignment.getAttributes());
	}

	@Override
	public void setAttribute(EObject subject, Namespace namespace, String name,
			Value value) {
		if (subject == null) {
			throw new IllegalArgumentException("Attribute subject cannot be null");
		}
		Map<AttributeKey, Attribute> attributes = myCache.get(subject);
		if (attributes == null) {
			attributes = addAssignment(subject);
		}
		AttributeKey key = createKey(namespace, name);
		Attribute attribute = attributes.get(key);
		if (attribute != null) {
			attribute.setValue(value);
		} else { 
			addAttribute(subject, namespace, value, attributes, key);
		}
	}

	@Override
	public void copyAttributes(EObject from, EObject to) {
		AttributeAssignment assignment = myAssignments.get(from);
		if (assignment == null) {
			return;
		}
		EList<Attribute> attributes = assignment.getAttributes();
		for (Attribute attribute : attributes) {
			setAttribute(to, attribute.getNamespace(), attribute.getName(), EMFProxyUtil.copy(attribute.getValue()));
		}
	}

	private AttributeKey createKey(Namespace namespace, String name) {
		String uri = getUri(namespace);
		AttributeKey key = new AttributeKey(uri, name);
		return key;
	}

	private static String getUri(Namespace namespace) {
		return namespace == null ? null : namespace.getUri();
	}

	private void addAttribute(EObject subject, Namespace namespace,
			Value value, Map<AttributeKey, Attribute> attributes,
			AttributeKey key) {
		Attribute attribute = MetadataFactory.eINSTANCE.createAttribute();
		attribute.setName(key.ffName);
		attribute.setNamespace(namespace);
		attribute.setValue(value);
		AttributeAssignment assignment = myAssignments.get(subject);
		assignment.getAttributes().add(attribute);
		attributes.put(key, attribute);
	}

	private Map<AttributeKey, Attribute> addAssignment(EObject subject) {
		AttributeAssignment assignment = AspectsFactory.eINSTANCE.createAttributeAssignment();
		assignment.setSubject(subject);
		myAspectWritingStrategy.addAssignment(assignment);
		myAssignments.put(subject, assignment);
		Map<AttributeKey, Attribute> attributes = new HashMap<AttributeKey, Attribute>();
		myCache.put(subject, attributes);
		return attributes;
	}

	private static final class AttributeKey {
		public final String ffNamespaceUri;
		public final String ffName;

		public AttributeKey(String namespaceUri, String name) {
			ffName = name;
			ffNamespaceUri = namespaceUri;
		}

		public AttributeKey(Attribute attribute) {
			ffName = attribute.getName();
			ffNamespaceUri = getUri(attribute.getNamespace());
		}

		@Override
		/* generated */
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((ffName == null) ? 0 : ffName.hashCode());
			result = prime
					* result
					+ ((ffNamespaceUri == null) ? 0 : ffNamespaceUri.hashCode());
			return result;
		}

		@Override
		/* generated */
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AttributeKey other = (AttributeKey) obj;
			if (ffName == null) {
				if (other.ffName != null)
					return false;
			} else if (!ffName.equals(other.ffName))
				return false;
			if (ffNamespaceUri == null) {
				if (other.ffNamespaceUri != null)
					return false;
			} else if (!ffNamespaceUri.equals(other.ffNamespaceUri))
				return false;
			return true;
		}
		
	}
	
}
