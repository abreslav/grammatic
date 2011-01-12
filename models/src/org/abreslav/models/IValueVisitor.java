package org.abreslav.models;

/**
 * @author abreslav
 */
public interface IValueVisitor<R, D> {
    R visitBoolean(BooleanValue value, D data);
    R visitInteger(IntegerValue value, D data);
    R visitList(ListValue value, D data);
    R visitNull(NullValue value, D data);
    R visitObject(ObjectValue value, D data);
    R visitReference(ReferenceValue value, D data);
    R visitSet(SetValue value, D data);
    R visitString(StringValue value, D data);

    class Adapter<R, D> implements IValueVisitor<R, D> {

        public R visitBoolean(BooleanValue value, D data) {
            return visitValue(value, data);
        }

        public R visitInteger(IntegerValue value, D data) {
            return visitValue(value, data);
        }

        public R visitList(ListValue value, D data) {
            return visitCollectionValue(value, data);
        }

        public R visitNull(NullValue value, D data) {
            return visitValue(value, data);
        }

        public R visitObject(ObjectValue value, D data) {
            return visitValue(value, data);
        }

        public R visitReference(ReferenceValue value, D data) {
            return visitValue(value, data);
        }

        public R visitSet(SetValue value, D data) {
            return visitCollectionValue(value, data);
        }

        public R visitString(StringValue value, D data) {
            return visitValue(value, data);
        }

        public R visitCollectionValue(ICollectionValue value, D data) {
            return visitValue(value, data);
        }

        public R visitValue(IValue value, D data) {
            return null;
        }
    }
}
