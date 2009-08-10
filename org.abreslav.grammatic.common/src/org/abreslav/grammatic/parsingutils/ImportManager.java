package org.abreslav.grammatic.parsingutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EGenericType;

public class ImportManager {

	private final String myPackage;
	private final Set<String> myDefaultImports = new HashSet<String>();
	private final Map<String, String> myShortNameToFQN = new HashMap<String, String>();
	private final Map<String, String> myFQNToShortName = new HashMap<String, String>();

	public ImportManager(String pack) {
		myPackage = pack == null ? "" : pack;
		addType("java.lang.Object");
		addType("java.lang.String");
		addType("java.lang.Class");
		addType("java.lang.Integer");
		addType("java.lang.Byte");
		addType("java.lang.Short");
		addType("java.lang.Long");
		addType("java.lang.Boolean");
		addType("java.lang.Character");
		addType("java.lang.Float");
		addType("java.lang.Double");
		addType("java.lang.StringBuffer");
		addType("java.lang.StringBuilder");
		addType("java.lang.Appendable");
		addType("java.lang.ClassLoader");
		addType("java.lang.Cloneable");
		// TODO many more...
	}
	
	public final void addType(String fqn) {
		if (myFQNToShortName.containsKey(fqn)) {
			return;
		}
		int lastDot = fqn.lastIndexOf('.');
		String simpleName;
		if (lastDot < 0) {
			if (JavaUtils.getPrimitiveTypeNames().contains(fqn)) {
				myFQNToShortName.put(fqn, fqn);
				myShortNameToFQN.put(fqn, fqn);
				myDefaultImports.add(fqn);
				return;
			}
			if (myPackage.length() > 0) {
				throw new IllegalArgumentException("Unable to import from a default package: " + fqn);
			}
			simpleName = fqn;
			// If we already have such a name...
			if (myShortNameToFQN.containsKey(simpleName)) {
				throw new IllegalArgumentException("Simple name is already used: " + fqn);
			}
		} else {
			simpleName = fqn.substring(lastDot + 1);
		}
		
		if (myShortNameToFQN.containsKey(simpleName)) {
			myFQNToShortName.put(fqn, fqn);
		} else {
			myFQNToShortName.put(fqn, simpleName);
			myShortNameToFQN.put(simpleName, fqn);
			String pack = fqn.substring(0, lastDot);
			if ("java.lang".equals(pack) || myPackage.equals(pack)) {
				myDefaultImports.add(fqn);
			}
		}
	}
	
	public String getTypeName(String fqn) {
		addType(fqn);
		return myFQNToShortName.get(fqn);
	}
	
	public String getTypeName(EGenericType type) {
		return "#generic#";
	}
	
	public List<String> getImports() {
		List<String> imports = new ArrayList<String>(myShortNameToFQN.values());
		imports.removeAll(myDefaultImports);
		Collections.sort(imports);
		return imports;
	}
}
