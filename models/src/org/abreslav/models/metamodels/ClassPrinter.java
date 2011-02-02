package org.abreslav.models.metamodels;

import org.abreslav.metametamodel.*;
import org.abreslav.models.util.ObjectWrapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author abreslav
 */
public class ClassPrinter {

    private String indent;
    private static final String INDENT_UNIT = "  ";
    private final Appendable out;

    public ClassPrinter(Appendable out, String indent) {
        this.out = out;
        this.indent = indent;
    }

    public ClassPrinter(Appendable out) {
        this(out, "");
    }

    public void printClass(IClass modelClass) {
        String className = getIdentity(modelClass);
        String abstractModifier = modelClass.isAbstract() ? "abstract " : "";
        print(indent + abstractModifier + "class " + className);
        Set<IClass> superClasses = modelClass.getSuperclasses();
        if (!superClasses.isEmpty()) {
            print(" : ");
            for (Iterator<IClass> iterator = superClasses.iterator(); iterator.hasNext();) {
                IClass superClass = iterator.next();
                print(getIdentity(superClass));
                if (iterator.hasNext()) {
                    print(", ");
                }
            }
        }
        print(" {");
        newline();
        String oldIndent = indent;
        increaseIndent();
        for (IPropertyDescriptor descriptor : modelClass.getPropertyDescriptors()) {
            printPropertyDescriptor(descriptor);
        }
        indent = oldIndent;
        print("}");
        newline();
    }

    private void printPropertyDescriptor(IPropertyDescriptor descriptor) {
        print(indent + getIdentity(descriptor) + " : ");
        printType(descriptor.getType());
        newline();
    }

    private void printType(IType type) {
        type.accept(new ITypeVisitor.Adapter<Void, Void>() {
            @Override
            public Void visitListType(IListType type, Void data) {
                print("[");
                printType(type.getElementType());
                print(type.isNonempty() ? "+" : "*");
                print("]");
                return null;
            }

            @Override
            public Void visitObjectType(IObjectType type, Void data) {
                print("val(" + getIdentity(type.getUnderlyingClass()) + ")");
                return null;
            }

            @Override
            public Void visitPrimitiveType(IPrimitiveType type, Void data) {
                print(type.getType());
                return null;
            }

            @Override
            public Void visitReferenceType(IReferenceType type, Void data) {
                print("ref(" + getIdentity(type.getUnderlyingClass()) + ")");
                return null;
            }

            @Override
            public Void visitSetType(ISetType type, Void data) {
                print("{");
                printType(type.getElementType());
                print(type.isNonempty() ? "+" : "*");
                print("}");
                return null;
            }

            @Override
            public Void visitNullableType(INullableType type, Void data) {
                print("(");
                printType(type.getType());
                print(")?");
                return null;
            }

            @Override
            public Void visitAnyType(IAnyType type, Void data) {
                print("_");
                return null;
            }

            @Override
            public Void visitEnumType(IEnumType type, Void data) {
                print("enum " + getIdentity(type.getEnum()));
                return null;
            }

            @Override
            public Void visitType(IType type, Void data) {
                throw new IllegalStateException("Unknown type: " + type);
            }
        }, null);
    }

    private void newline() {
        try {
            out.append("\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void print(String s) {
        try {
            out.append(s);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void increaseIndent() {
        indent += INDENT_UNIT;
    }

    private String getIdentity(Object object) {
        if (object instanceof ObjectWrapper) {
            return ((ObjectWrapper) object).getObject().getIdentity().toString();
        }
        return "" + object;
    }

    public void printEnum(Enum anEnum) {
        print(indent + "enum ");
        print(anEnum.getObject().getIdentity().toString());
        print(" {");
        newline();
        increaseIndent();
        for (Iterator<IEnumLiteral> iterator = anEnum.getLiterals().iterator(); iterator.hasNext();) {
            IEnumLiteral literal = iterator.next();
            print(indent + getIdentity(literal));
            if (iterator.hasNext()) {
                print(",");
            }
            newline();
        }
        print("}");
    }
}
