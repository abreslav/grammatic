package org.abreslav.grammatic.parsingutils;

import java.util.HashSet;
import java.util.Set;

public class NameScope {
	private final Set<String> myUsedNames = new HashSet<String>();
	
	private NameScope(NameScope parent) {
		if (parent != null) {
			myUsedNames.addAll(parent.myUsedNames);
		}
	}
	
	public static NameScope createTopLevelScope() {
		return new NameScope(null);
	}
	
	public NameScope createChildScope() {
		return new NameScope(this);
	}
	
	public String getUniqueName(String prefix) {
		String name = prefix;
		for (int n = 1; myUsedNames.contains(name); n++) {
			name = prefix + Integer.toString(n);
		}
		myUsedNames.add(name);
		return name; 
	}
	
	public void registerName(String name) {
		if (myUsedNames.contains(name)) {
			throw new IllegalArgumentException("Name is already used: " + name);
		}
		myUsedNames.add(name);
	}
}