package org.abreslav.grammatic.atf.types.unification;

public interface IConstraintSystemSolver {
	<T, E extends Throwable> IVariableSubstitution<T> solve(
			IConstraintSystem<T, E> constraintSystem, 
			IClosedConstraintSolver<T> closedConstraintSolver, 
			ITypeErrorHandler<E> errorHandler) throws E;
}
