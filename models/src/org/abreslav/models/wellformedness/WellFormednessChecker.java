package org.abreslav.models.wellformedness;

import org.abreslav.models.*;
import org.abreslav.models.util.ModelUtils;
import org.abreslav.models.util.TraverseValueTreeVisitor;

import java.util.*;

/**
 * @author abreslav
 */
public class WellFormednessChecker {

    public static final WellFormednessChecker INSTANCE = new WellFormednessChecker();

    private WellFormednessChecker() {
    }

    public void checkWellFormedness(IContext context, Set<? extends IValue> preModel) {
        Set<ObjectValue> allObjectsInContext = new LinkedHashSet<ObjectValue>(context.getAllObjects());
        checkWithWellFormedContext(context, allObjectsInContext);

        checkWithWellFormedContext(context, preModel);
    }

    private void checkWithWellFormedContext(IContext context, Set<? extends IValue> preModel) {
        Collection<ObjectValue> allObjects = new ArrayList<ObjectValue>();
        ModelUtils.collectAllObjects(preModel, allObjects);

//      - identities are not objects and don't contain objects
        for (ObjectValue object : allObjects) {
            object.getIdentity().accept(new TraverseValueTreeVisitor<Void, Void>() {
                    @Override
                    public Void visitObject(ObjectValue value, Void data) {
                        throw new IllegalArgumentException("Objects are not allowed in property names");
                    }
                }, null);
        }

//      - property names are not objects and don't contain objects
        for (ObjectValue object : allObjects) {
            for (Map.Entry<IValue, IValue> property : object.getProperties()) {
                IValue propertyName = property.getKey();
                propertyName.accept(new TraverseValueTreeVisitor<Void, Void>() {
                    @Override
                    public Void visitObject(ObjectValue value, Void data) {
                        throw new IllegalArgumentException("Objects are not allowed in property names");
                    }
                }, null);
            }
        }

//      - every identity is assigned to only one object
        Context preModelContext = new Context(context);
        for (ObjectValue object : allObjects) {
            preModelContext.putObject(object); // this throws an exception if the identity is reused
        }

//      - every reference refers to an existing object (within an context)
        Collection<ReferenceValue> allReferences = new ArrayList<ReferenceValue>();
        collectAllReferences(preModel, allReferences);
        for (ReferenceValue reference : allReferences) {
            IValue referredIdentity = reference.getReferredIdentity();
            ObjectValue referredObject = context.getObject(referredIdentity);
            if (referredObject == null) {
                referredObject = preModelContext.getObject(referredIdentity);
                if (referredObject == null) {
                    throw new IllegalArgumentException("Dangling reference to " + referredIdentity);
                }
            }
            ObjectValue oldReferred = reference.getReferredObject();
            if (oldReferred != null) {
                if (oldReferred != referredObject) {
                    throw new IllegalArgumentException("Same identity points to different places over time");
                }
            } else {
                reference.setReferredObject(referredObject);
            }
        }

//      - property names are not duplicated inside one object (trivial)
//      - sets do not contain duplicates (trivial)
    }

    private void collectAllReferences(Set<? extends IValue> preModel, final Collection<ReferenceValue> allReferences) {
        for (IValue value : preModel) {
            value.accept(new TraverseValueTreeVisitor<Void, Void>() {
                @Override
                public Void visitReference(ReferenceValue value, Void data) {
                    allReferences.add(value);
                    return super.visitReference(value, data);
                }

                @Override
                public void visitPropertyName(IValue key, Void data) {
                    key.accept(this, data);
                }
            }, null);
        }
    }
}
