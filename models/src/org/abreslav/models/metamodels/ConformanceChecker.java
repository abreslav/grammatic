package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.*;
import org.abreslav.models.*;
import org.abreslav.models.StringValue;
import org.abreslav.models.util.ObjectWrapper;
import org.abreslav.models.util.TraverseValueTreeVisitor;

import java.util.*;

/**
 * @author abreslav
 */
public class ConformanceChecker {
   // Metamodel: no inheritance cycles

    // Class references are references to objects of class Class

    // Every property has a value that agrees with the type declared by its descriptor
    // An object does not have any other properties
    // TODO: overriding by flag
    // An object has all properties declared in its class and all of its superclasses
    // Abstract classes have no instances


    public static Collection<IDiagnostic> check(Set<? extends IValue> model) {
        return new ConformanceChecker().checkModel(model);
    }

    private static final String META_CLASS_NAME = "Class";

    private Collection<IDiagnostic> diagnostics = new ArrayList<IDiagnostic>();

    private ConformanceChecker() {}

    public Collection<IDiagnostic> checkModel(Set<? extends IValue> model) {

        for (IValue value : model) {
            value.accept(new IValueVisitor.Adapter<Void, Collection<IDiagnostic>>() {
                @Override
                public Void visitObject(ObjectValue value, Collection<IDiagnostic> diagnostics) {
                    checkObject(value);
                    return null;
                }

                @Override
                public Void visitCollectionValue(ICollectionValue value, Collection<IDiagnostic> data) {
                    for (IValue v : value.getValue()) {
                        v.accept(this, data);
                    }
                    return null;
                }
            }, diagnostics);
        }

        return diagnostics;
    }

    private IClass checkObject(ObjectValue value) {
//        System.out.println("Checked: " + value);
        ModelClass modelClass = getIClass(value);

        if (modelClass == null) {
            return null;
        }

        // Abstract classes have no instances
        if (modelClass.isAbstract()) {
            diagnostics.add(new ClassReferenceDiagnostic(value, "An instance of an abstract class"));
        }

        // An object has all properties declared in its class and all of its superclasses
        Set<IValue> validPropertyIdentities = new LinkedHashSet<IValue>();
        Collection<IPropertyDescriptor> allPropertyDescriptors = MetaModelUtils.getAllPropertyDescriptors(modelClass);
        for (IPropertyDescriptor descriptor : allPropertyDescriptors) {
            IValue propertyDescriptorIdentity = ((ObjectWrapper) descriptor).getObject().getIdentity();
            validPropertyIdentities.add(new ReferenceValue(propertyDescriptorIdentity));
            IValue propertyValue = value.getPropertyValue(new ReferenceValue(propertyDescriptorIdentity));
            if (propertyValue == null) {
                diagnostics.add(new PropertyDiagnostic(value, descriptor, "Property not present"));
            } else {
                // Every property has a value that agrees with the type declared by its descriptor
                IType expectedType = descriptor.getType();
                if (!checkType(propertyValue, expectedType)) {
                    diagnostics.add(new PropertyDiagnostic(value, descriptor, propertyValue + " is not a value of type " + expectedType));
                }
            }
        }
        // An object does not have any other properties
        for (Map.Entry<IValue, IValue> property : value.getProperties()) {
            IValue propertyIdentity = property.getKey();
            if (!validPropertyIdentities.contains(propertyIdentity)) {
                diagnostics.add(new PropertyDiagnostic(value, propertyIdentity, "This property is not declared in the metamodel"));
            }
        }

        return modelClass;
    }

    private ModelClass getIClass(ObjectValue value) {
        ReferenceValue classReference = value.getClassReference();
        ObjectValue classObject = classReference.getReferredObject();

        // Class references are references to objects of class Class
        if (!new StringValue(META_CLASS_NAME).equals(classObject.getClassReference().getReferredIdentity())) {
            diagnostics.add(new ClassReferenceDiagnostic(value,
                    "Must be a reference to a an instance of the Class meta-meta-class"));
        }

        // TODO: change the exception type
        ModelClass modelClass = null;
        try {
            modelClass = new ModelClass(classObject);
        } catch (IllegalArgumentException e) {
            diagnostics.add(new ClassReferenceDiagnostic(value, e.getMessage()));
        }
        return modelClass;
    }

    private boolean checkType(IValue value, IType type) {
        if (value != NullValue.NULL) {
            IType nonNullable = unwrapNullable(type);
            if (nonNullable instanceof IAnyType) {
                return value.accept(new TraverseValueTreeVisitor<Boolean, Void>() {
                    @Override
                    public Boolean visitObject(ObjectValue value, Void data) {
                        if (checkObject(value) == null) {
                            return false;
                        }
                        return super.visitObject(value, data);
                    }

                    @Override
                    public Boolean visitValue(IValue value, Void data) {
                        return true;
                    }
                }, null);
            }
        }
        return value.accept(new IValueVisitor<Boolean, IType>() {
            public Boolean visitBoolean(BooleanValue value, IType type) {
                return assurePrimitiveType(type, "Boolean");
            }

            public Boolean visitInteger(IntegerValue value, IType type) {
                return assurePrimitiveType(type, "Integer");
            }

            public Boolean visitString(StringValue value, IType type) {
                IType nonnullableType = unwrapNullable(type);
                if (false == nonnullableType instanceof IPrimitiveType) {
                    return false;
                }
                IPrimitiveType primitiveType = (IPrimitiveType) nonnullableType;
                if ("Character".equals(primitiveType.getType())) {
                    return value.getValue().length() == 1;
                }
                return assurePrimitiveType(type, "String");
            }

            public Boolean visitList(ListValue value, IType type) {
                type = unwrapNullable(type);
                if (false == type instanceof IListType) {
                    return false;
                }
                return checkCollectionType(value, (ICollectionType) type);
            }

            public Boolean visitSet(SetValue value, IType type) {
                type = unwrapNullable(type);
                if (false == type instanceof ISetType) {
                    return false;
                }
                return checkCollectionType(value, (ICollectionType) type);
            }

            public Boolean visitNull(NullValue value, IType type) {
                return type instanceof INullableType;
            }

            public Boolean visitObject(ObjectValue value, IType type) {
                type = unwrapNullable(type);
                if (false == type instanceof IObjectType) {
                    return false;
                }
                checkObject(value);
                return checkClassType(value, type);
            }

            public Boolean visitReference(ReferenceValue value, IType type) {
                type = unwrapNullable(type);
                if (type instanceof IEnumType) {
                    return checkEnumValue(value, (IEnumType) type);
                }
                if (false == type instanceof IReferenceType) {
                    return false;
                }
                return checkClassType(value.getReferredObject(), type);
            }
        }, type);
    }

    private boolean checkEnumValue(ReferenceValue value, IEnumType type) {
        Set<IEnumLiteral> literals = type.getEnum().getLiterals();
        ObjectValue referredObject = value.getReferredObject();
        for (IEnumLiteral literal : literals) {
            ObjectValue literalObject = ((ObjectWrapper) literal).getObject();
            if (literalObject.equals(referredObject)) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkClassType(ObjectValue value, IType type) {
        IClass valueClass = getIClass(value);
        if (valueClass == null) {
            return true; // there is an error inside already
        }
        type = unwrapNullable(type);
        IClassType objectType = (IClassType) type;
        return checkSubclass(valueClass, objectType);
    }

    private boolean checkSubclass(IClass valueClass, IClassType objectType) {
        return MetaModelUtils.getAllSuperclasesAndMe(valueClass).contains(objectType.getUnderlyingClass());
    }

    private boolean checkCollectionType(ICollectionValue value, ICollectionType type) {
        CollectionType collectionType = (CollectionType) type;
        Collection<IValue> collection = value.getValue();
        if (collection.isEmpty() && collectionType.isNonempty()) {
            return false;
        }

        IType elementType = collectionType.getElementType();
        for (IValue item : collection) {
            if (!checkType(item, elementType)) {
                return false;
            }
        }
        return true;
    }

    private boolean assurePrimitiveType(IType type, String primitiveTypeName) {
        type = unwrapNullable(type);
        if (type instanceof IPrimitiveType) {
            if (primitiveTypeName.equals(((IPrimitiveType) type).getType())) {
                return true;
            }
        }
        return false;
    }

    private IType unwrapNullable(IType type) {
        while (type instanceof INullableType) {
            type = ((INullableType) type).getType();
        }
        return type;
    }

    private abstract class Diagnostic implements IDiagnostic {

        private final String message;

        private Diagnostic(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Diagnostic{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    private class ClassReferenceDiagnostic extends Diagnostic implements IDiagnostic {
        private final ObjectValue object;

        public ClassReferenceDiagnostic(ObjectValue value, String message) {
            super(message);
            this.object = value;
        }

        @Override
        public String toString() {
            return getMessage() + " on " + object;
        }
    }

    private class PropertyDiagnostic extends Diagnostic implements IDiagnostic {

        private final ObjectValue object;
        private final IValue propertyIdentity;

        public PropertyDiagnostic(ObjectValue value, IPropertyDescriptor descriptor, String message) {
            super(message);
            this.object = value;
            this.propertyIdentity = ((ObjectWrapper) descriptor).getObject().getIdentity();
        }

        public PropertyDiagnostic(ObjectValue value, IValue propertyIdentity, String message) {
            super(message);
            this.object = value;
            this.propertyIdentity= propertyIdentity;
        }

        @Override
        public String toString() {
            return getMessage() + " on " + object.getIdentity() + "." + propertyIdentity;
        }
    }
}
