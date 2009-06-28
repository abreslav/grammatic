package org.abreslav.grammatic.atf.types.unification.graph.impl;

public final class Flag {
	private boolean myFlag = false;

	public Flag(boolean flag) {
		myFlag = flag;
	}
	
	public boolean isTrue() {
		return myFlag;
	}
	
	public void set(boolean flag) {
		myFlag = flag;
	}
}
