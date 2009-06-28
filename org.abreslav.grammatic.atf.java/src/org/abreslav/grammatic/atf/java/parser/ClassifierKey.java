package org.abreslav.grammatic.atf.java.parser;

public final class ClassifierKey {

	private final String myPackage;
	private final String myName;
	
	public ClassifierKey(String pack, String name) {
		myName = name;
		myPackage = pack;
	}
	
	public String getName() {
		return myName;
	}
	
	public String getPackage() {
		return myPackage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myName == null) ? 0 : myName.hashCode());
		result = prime * result
				+ ((myPackage == null) ? 0 : myPackage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassifierKey other = (ClassifierKey) obj;
		if (myName == null) {
			if (other.myName != null)
				return false;
		} else if (!myName.equals(other.myName))
			return false;
		if (myPackage == null) {
			if (other.myPackage != null)
				return false;
		} else if (!myPackage.equals(other.myPackage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + myPackage + "." + myName + "]";
	}
}
