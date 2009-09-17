package org.abreslav.grammatic.metadata.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.AttributeValue;
import org.abreslav.grammatic.metadata.CrossReferenceValue;
import org.abreslav.grammatic.metadata.MetadataFactory;
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
	
	public static CrossReferenceValue createCrossReferenceValue(EObject object) {
		CrossReferenceValue value = MetadataFactory.eINSTANCE.createCrossReferenceValue();
		value.getValues().add(object);
		return value;
	}
	
	public static CrossReferenceValue createCrossReferenceValue(Collection<? extends EObject> object) {
		CrossReferenceValue value = MetadataFactory.eINSTANCE.createCrossReferenceValue();
		value.getValues().addAll(object);
		return value;
	}
	
	public static AttributeValue createAttributeValue(
			Object object) {
		AttributeValue value = MetadataFactory.eINSTANCE.createAttributeValue();
		value.getValues().add(object);
		return value;
	}

	public static AttributeValue createAttributeValue(
			Collection<?> object) {
		AttributeValue value = MetadataFactory.eINSTANCE.createAttributeValue();
		value.getValues().addAll(object);
		return value;
	}
	
}
