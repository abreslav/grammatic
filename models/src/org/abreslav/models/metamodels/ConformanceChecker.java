package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.*;
import org.abreslav.models.*;
import org.abreslav.models.StringValue;
import org.abreslav.models.util.ObjectWrapper;
import org.abreslav.models.wellformedness.IContext;

import java.util.*;

/**
 * @author abreslav
 */
public class ConformanceChecker {
   // Metamodel: no inheritance cycles

    // Class references are references to objects of class Class
    // An object has all properties declared in its class and all of its superclasses
       // TODO: overriding by flag
    // An object does not have any other properties
    // Every property has a value that agrees with the type declared by its descriptor

    public Collection<IDiagnostic> check(IContext context, final IContext mmmContext, Set<? extends IValue> model) {
        Collection<IDiagnostic> diagnostics = new ArrayList<IDiagnostic>();

        for (IValue value : model) {
            value.accept(new IValueVisitor.Adapter<Void, Collection<IDiagnostic>>() {
                @Override
                public Void visitObject(ObjectValue value, Collection<IDiagnostic> diagnostics) {
                    ReferenceValue classReference = value.getClassReference();
                    ObjectValue classObject = classReference.getReferredObject();

                    // Class references are references to objects of class Class
                    if (classObject != mmmContext.getObject(new StringValue("Class"))) {
                        diagnostics.add(new ClassReferenceDiagnostic(value, "Must be a reference to a an instance of the Class meta-meta-class"));
                    }

                    // TODO: change the exception type
                    ModelClass modelClass = null;
                    try {
                        modelClass = new ModelClass(classObject);
                    } catch (IllegalArgumentException e) {
                        diagnostics.add(new ClassReferenceDiagnostic(value, e.getMessage()));
                    }

                    if (modelClass != null) {
                        // An object has all properties declared in its class and all of its superclasses
                        Set<IValue> validPropertyIdentities = new LinkedHashSet<IValue>();
                        Collection<IPropertyDescriptor> allPropertyDescriptors = MetaModelUtils.getAllPropertyDescriptors(modelClass);
                        for (IPropertyDescriptor descriptor : allPropertyDescriptors) {
                            IValue propertyDescriptorIdentity = ((ObjectWrapper) descriptor).getObject().getIdentity();
                            validPropertyIdentities.add(propertyDescriptorIdentity);
                            IValue propertyValue = value.getPropertyValue(new ReferenceValue(propertyDescriptorIdentity));
                            if (propertyValue == null) {
                                diagnostics.add(new PropertyDiagnostic(value, descriptor, "Property not present"));
                            } else {
                                // Every property has a value that agrees with the type declared by its descriptor
                                checkType(propertyValue, descriptor.getType());
                            }
                        }
                        // An object does not have any other properties
                        for (Map.Entry<IValue, IValue> property : value.getProperties()) {
                            IValue propertyIdentity = property.getKey();
                            if (!validPropertyIdentities.contains(propertyIdentity)) {
                                diagnostics.add(new PropertyDiagnostic(value, propertyIdentity, "This property is not declared in the metamodel"));
                            }
                        }
                    }
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

    private boolean checkType(IValue value, IType type) {
        return value.accept(new IValueVisitor<Boolean, IType>() {
            public Boolean visitBoolean(BooleanValue value, IType type) {
                return assurePrimitiveType(type, "Boolean");
            }

            public Boolean visitInteger(IntegerValue value, IType type) {
                return assurePrimitiveType(type, "Integer");
            }

            public Boolean visitString(StringValue value, IType type) {
                return assurePrimitiveType(type, "String");
            }

            public Boolean visitList(ListValue value, IType type) {
                type = unwrapNullable(type);
                if (false == type instanceof IListType) {
                    return false;
                }
                if (checkCollectionType(value, (ICollectionType) type)) return false;

                return true;
            }

            public Boolean visitSet(SetValue value, IType type) {
                type = unwrapNullable(type);
                if (false == type instanceof ISetType) {
                    return false;
                }
                if (checkCollectionType(value, (ICollectionType) type)) return false;

                return true;
            }

            public Boolean visitNull(NullValue value, IType type) {
                return type instanceof INullableType;
            }

            public Boolean visitObject(ObjectValue value, IType type) {
                return null;
            }

            public Boolean visitReference(ReferenceValue value, IType type) {
                // TODO: not implemented
                return null;
            }
        }, type);
    }

    private boolean checkCollectionType(ICollectionValue value, ICollectionType type) {
        CollectionType collectionType = (CollectionType) type;
        Collection<IValue> collection = value.getValue();
        if (collection.isEmpty() && collectionType.isNonempty()) {
            return true;
        }

        IType elementType = collectionType.getElementType();
        for (IValue item : collection) {
            if (!checkType(item, elementType)) {
                return true;
            }
        }
        return false;
    }

    private boolean assurePrimitiveType(IType type, String primitiveTypeName) {
        type = unwrapNullable(type);
        if (type instanceof INullableType) {
            return true;
        } else if (type instanceof IPrimitiveType) {
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

    public interface IDiagnostic {
        String getMessage();
    }

    private abstract class Diagnostic implements IDiagnostic {

        private final String message;

        private Diagnostic(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private class ClassReferenceDiagnostic extends Diagnostic implements IDiagnostic {
        public ClassReferenceDiagnostic(ObjectValue value, String message) {
            super(message);
        }
    }
    private class PropertyDiagnostic extends Diagnostic implements IDiagnostic {
        public PropertyDiagnostic(ObjectValue value, IPropertyDescriptor descriptor, String message) {
            super(message);
        }

        public PropertyDiagnostic(ObjectValue value, IValue propertyIdentity, String message) {
            super(message);
        }
    }
}
