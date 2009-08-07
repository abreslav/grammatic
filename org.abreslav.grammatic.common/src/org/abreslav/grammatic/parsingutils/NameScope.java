package org.abreslav.grammatic.parsingutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NameScope implements INameScope {
	public static INameScope createTopLevelScope() {
		return new NameScope(null);
	}
	
	public static INameScope createMultiparentScope(INameScope... scopes) {
		NameScope nameScope = new NameScope(null);
		nameScope.myParentScopes.addAll(Arrays.asList(scopes));
		return nameScope;
	}
	
	private final Collection<INameScope> myParentScopes = new ArrayList<INameScope>();
	private final Set<String> myUsedNames = new HashSet<String>();
	
	private NameScope(NameScope parent) {
		if (parent != null) {
			myParentScopes.add(parent);
		}
	}

	@Override
	public INameScope createChildScope() {
		return new NameScope(this);
	}

	@Override
	public boolean isUsed(String name) {
		if (myUsedNames.contains(name)) {
			return true;
		}
		for (INameScope parent : myParentScopes) {
			if (parent.isUsed(name)) {
				myUsedNames.add(name);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getUniqueName(String prefix) {
		String name = prefix;
		for (int n = 1; isUsed(name); n++) {
			name = prefix + Integer.toString(n);
		}
		myUsedNames.add(name);
		return name; 
	}
	
	@Override
	public void registerName(String name) {
		if (isUsed(name)) {
			throw new IllegalArgumentException("Name is already used: " + name);
		}
		myUsedNames.add(name);
	}
}