package org.abreslav.grammatic.atf.types.unification.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.atf.types.unification.ExplicitType;
import org.abreslav.grammatic.atf.types.unification.IClosedConstraint;

public class ClosedConstraint<T> implements IClosedConstraint<T> {

	private final Set<T> myUpperBounds = new HashSet<T>();
	private final Set<T> myLowerBounds = new HashSet<T>();
	private final Set<T> myUpperBoundsReadOnly = Collections.unmodifiableSet(myUpperBounds);
	private final Set<T> myLowerBoundsReadOnly = Collections.unmodifiableSet(myLowerBounds);
	
	public void addUpperBound(ExplicitType<T> type) {
		myUpperBounds.add(type.getType());
	}
	
	public void addLowerBound(ExplicitType<T> type) {
		myLowerBounds.add(type.getType());
	}
	
	@Override
	public Set<T> getLowerBounds() {
		return myLowerBoundsReadOnly;
	}

	@Override
	public Set<T> getUpperBounds() {
		return myUpperBoundsReadOnly;
	}

}
