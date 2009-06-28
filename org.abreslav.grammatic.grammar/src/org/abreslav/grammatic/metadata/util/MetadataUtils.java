package org.abreslav.grammatic.metadata.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.eclipse.emf.ecore.EObject;

public class MetadataUtils {
	public static Map<String, Value> getMetadata(
			EObject subject, IMetadataProvider metadataProvider) {
		Set<Attribute> attributes = metadataProvider.getAttributes(subject);
		return createAttributeMap(attributes);
	}

	public static Map<String, Value> createAttributeMap(Collection<? extends Attribute> attributes) {
		Map<String, Value> metadata = new HashMap<String, Value>();
		for (Attribute attribute : attributes) {
			metadata.put(attribute.getName(), attribute.getValue());
		}
		return metadata;
	}
	
	
}
