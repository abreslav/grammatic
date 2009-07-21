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
	<T extends EObject> T readEObject(String name);
	<T extends EObject> List<T> readEObjects(String name);
	<T> T readObject(String name);
	<T> List<T> readObjects(String name);
	boolean isPresent(String name);
	Set<String> getAttributeNames();
}
