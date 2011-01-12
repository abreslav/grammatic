package org.abreslav.models;

/**
 * @author abreslav
 */
public class ReferenceValue implements IValue {
    private final IValue referredIdentity;
    private ObjectValue referredObject;

    public ReferenceValue(IValue referredIdentity) {
        this.referredIdentity = referredIdentity;
    }

    public IValue getReferredIdentity() {
        return referredIdentity;
    }

    public <R, D> R accept(IValueVisitor<R, D> visitor, D data) {
        return visitor.visitReference(this, data);
    }

    public ObjectValue getReferredObject() {
        return referredObject;
    }

    public void setReferredObject(ObjectValue referredObject) {
        if (this.referredObject != null) {
            throw new IllegalStateException("Attempt to retarget a reference");
        }
        this.referredObject = referredObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceValue that = (ReferenceValue) o;

        if (referredIdentity != null ? !referredIdentity.equals(that.referredIdentity) : that.referredIdentity != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return referredIdentity != null ? referredIdentity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "@" + referredIdentity;
    }
}
