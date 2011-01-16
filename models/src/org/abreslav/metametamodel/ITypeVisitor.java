package org.abreslav.metametamodel;

/**
 * @author abreslav
 */
public interface ITypeVisitor<R, D> {
    R visitAnyType(IAnyType type, D data);
    R visitListType(IListType type, D data);
    R visitObjectType(IObjectType type, D data);
    R visitPrimitiveType(IPrimitiveType type, D data);
    R visitReferenceType(IReferenceType type, D data);
    R visitSetType(ISetType type, D data);
    R visitNullableType(INullableType type, D data);

    class Adapter<R, D> implements ITypeVisitor<R, D> {

        public R visitAnyType(IAnyType type, D data) {
            return visitType(type, data);
        }

        public R visitListType(IListType type, D data) {
            return visitCollectionType(type, data);
        }

        public R visitObjectType(IObjectType type, D data) {
            return visitClassType(type, data);
        }

        public R visitPrimitiveType(IPrimitiveType type, D data) {
            return visitType(type, data);
        }

        public R visitReferenceType(IReferenceType type, D data) {
            return visitClassType(type, data);
        }

        public R visitSetType(ISetType type, D data) {
            return visitCollectionType(type, data);
        }

        public R visitNullableType(INullableType type, D data) {
            return visitType(type.getType(), data);
        }

        public R visitClassType(IClassType type, D data) {
            return visitType(type, data);
        }

        public R visitCollectionType(ICollectionType type, D data) {
            return visitType(type, data);
        }

        public R visitType(IType type, D data) {
            return null;
        }
    }
}
