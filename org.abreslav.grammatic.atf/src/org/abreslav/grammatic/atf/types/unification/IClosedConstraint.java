package org.abreslav.grammatic.atf.types.unification;

import java.util.Set;

public interface IClosedConstraint<T> {
	Set<T> getUpperBounds();
	Set<T> getLowerBounds();

	void addUpperBound(ExplicitType<T> type);
	
	void addLowerBound(ExplicitType<T> type);
	
}
