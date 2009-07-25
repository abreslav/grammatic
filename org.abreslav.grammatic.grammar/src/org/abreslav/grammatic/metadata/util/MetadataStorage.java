package org.abreslav.grammatic.metadata.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.metadata.Attribute;
import org.abreslav.grammatic.metadata.AttributeValue;
import org.abreslav.grammatic.metadata.ContainmentReferenceValue;
import org.abreslav.grammatic.metadata.CrossReferenceValue;
import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.eclipse.emf.ecore.EObject;

public class MetadataStorage implements IMetadataStorage {

	public static IMetadataStorage getMetadataStorage(EObject subject, IMetadataProvider metadataProvider) {
		return new MetadataStorage(metadataProvider.getAttributes(subject));
	}
	
	private final Map<String, Value> myMetadata;
	
	public MetadataStorage(Map<String, Value> metadata) {
		myMetadata = new HashMap<String, Value>(metadata);
	}

	public MetadataStorage(Collection<? extends Attribute> attributes) {
		this(MetadataUtils.createAttributeMap(attributes));
	}
	
	@Override
	public String readId(String name) {
		Value value = readValue(name);
		return value == null ? null : ((IdValue) value).getId();
	}

	@Override
	public Integer readInteger(String name) {
		Value value = readValue(name);
		return value == null ? null : ((IntegerValue) value).getValue();
	}

	@Override
	public List<Value> readMulti(String name) {
		Value value = readValue(name);
		return value == null ? null : ((MultiValue) value).getValues();
	}

	@Override
	public PunctuationType readPunctuation(String name) {
		Value value = readValue(name);
		return value == null ? null : ((PunctuationValue) value).getType();
	}

	@Override
	public String readString(String name) {
		Value value = readValue(name);
		return value == null ? null : ((StringValue) value).getValue();
	}

	@Override
	public IMetadataStorage readTuple(String name) {
		Value value = readValue(name);
		return value == null ? null : new MetadataStorage(((TupleValue) value).getAttributes());
	}

	@Override
	public Value readValue(String name) {
		return myMetadata.get(name);
	}

	@Override
	public boolean isPresent(String name) {
		return myMetadata.containsKey(name);
	}

	@Override
	public Set<String> getAttributeNames() {
		return Collections.unmodifiableSet(myMetadata.keySet());
	}

	@Override
	public String readIdOrString(String name) {
		Value value = readValue(name);
		if (value == null) {
			return null;
		}
		if (value instanceof IdValue) {
			IdValue id = (IdValue) value;
			return id.getId();
		}
		return ((StringValue) value).getValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends EObject> T readEObject(String name) {
		List<? extends EObject> list = getList(readValue(name));
		return list == null ? null : (T) list.get(0);
	}

	@Override
	public <T extends EObject> List<T> readEObjects(String name) {
		Value value = readValue(name);
		if (value == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) getList(value);
		return Collections.unmodifiableList(list);
	}

	private List<? extends EObject> getList(Value value) {
		if (value instanceof ContainmentReferenceValue) {
			ContainmentReferenceValue crValue = (ContainmentReferenceValue) value;
			return crValue.getValues();
		}
		return value == null ? null : ((CrossReferenceValue) value).getValues();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T readObject(String name) {
		AttributeValue value = (AttributeValue) readValue(name);
		return value == null ? null : (T) value.getValues().get(0);
	}

	@Override
	public <T> List<T> readObjects(String name) {
		AttributeValue value = (AttributeValue) readValue(name);
		if (value == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<T> values = (List<T>) value.getValues();
		return value == null ? null : Collections.unmodifiableList(values);
	}

}
