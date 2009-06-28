package org.abreslav.grammatic.atf.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.grammatic.atf.SemanticModule;

public class SemanticModuleDescriptor {
	private final String myModuleIdentifier;
	private final SemanticModule mySemanticModule;
	private final Map<String, Object> myOptionsReadOnly;
	
	public SemanticModuleDescriptor(String moduleIdentifier,
			SemanticModule semanticModule, Map<String, String> options) {
		myModuleIdentifier = moduleIdentifier;
		mySemanticModule = semanticModule;
		myOptionsReadOnly = Collections.unmodifiableMap(new HashMap<String, Object>(options));
	}
	
	public String getModuleIdentifier() {
		return myModuleIdentifier;
	}
	
	public SemanticModule getSemanticModule() {
		return mySemanticModule;
	}
	
	public Map<String, Object> getOptions() {
		return myOptionsReadOnly;
	}
	
}
