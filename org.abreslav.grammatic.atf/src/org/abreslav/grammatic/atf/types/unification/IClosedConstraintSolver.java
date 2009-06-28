package org.abreslav.grammatic.atf.types.unification;

import java.util.Set;

public interface IClosedConstraintSolver<T> {
	<E extends Throwable> Set<T> solve(
			IClosedConstraint<T> closedConstraint, 
			ITypeErrorHandler<E> errorHandler) throws E;
}
