package org.abreslav.grammatic.metadata.util;

import java.util.List;
import java.util.Set;

import org.abreslav.grammatic.metadata.PunctuationType;
import org.abreslav.grammatic.metadata.Value;
import org.eclipse.emf.ecore.EObject;

public interface IMetadataStorage {
	Value readValue(String name);
	String readString(String name);
	String readId(String name);
	String readIdOrString(String name);
	Integer readInteger(String name);
	PunctuationType readPunctuation(String name);
	List<Value> readMulti(String name);
	IMetadataStorage readTuple(String name);
	EObject readEObject(String name);
	List<? extends EObject> readEObjects(String name);
	Object readObject(String name);
	List<?> readObjects(String name);
	boolean isPresent(String name);
	Set<String> getAttributeNames();
}
