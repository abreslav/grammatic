package org.abreslav.lambda;

/**
 * @author abreslav
 */
public interface ITermVisitor<R, D> {
    R visitApplication(IApplication application, D data);
    R visitVariableUsage(IVariableUsage variableUsage, D data);
    R visitTerm(ITerm term, D data);

    class Adapter<R, D> implements ITermVisitor<R, D> {

        public R visitApplication(IApplication application, D data) {
            return visitPredefinedTerm(application, data);
        }

        public R visitVariableUsage(IVariableUsage variableUsage, D data) {
            return visitPredefinedTerm(variableUsage, data);
        }

        public R visitPredefinedTerm(IPredefinedTerm term, D data) {
            return visitTerm(term, data);
        }

        public R visitTerm(ITerm term, D data) {
            return null;
        }
    }
}
