package org.abreslav.grammatic.parsingutils;

public interface INameScope {

	INameScope createChildScope();

	String getUniqueName(String prefix);

	void registerName(String name);
	
	boolean isUsed(String name);

}