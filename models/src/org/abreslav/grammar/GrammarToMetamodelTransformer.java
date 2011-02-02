package org.abreslav.grammar;

import org.abreslav.metametamodel.IClass;
import org.abreslav.metametamodel.IPropertyDescriptor;
import org.abreslav.metametamodel.IType;
import org.abreslav.metametamodel.impl.*;

import java.util.*;

/**
 * @author abreslav
 */
public class GrammarToMetamodelTransformer {
    private final Map<ISymbol, IClass> classes = new HashMap<ISymbol, IClass>();
    private final Map<IExpression, IClass> expressionClasses = new HashMap<IExpression, IClass>();
    private final List<IClass> result = new ArrayList<IClass>();

    public GrammarToMetamodelTransformer() {}

    public List<IClass> transform(List<? extends ISymbol> symbols) {
        for (ISymbol symbol : symbols) {
            Set<String> labels = symbol.getLabels();
            if (labels.contains("token") || labels.contains("helper") || labels.contains("whitespace")) {
                continue;
            }
            IExpression definition = symbol.getDefinition();
            classes.put(symbol, new ModelClass(definition instanceof IAlternative, symbol.getName()));
        }


        for (ISymbol symbol : symbols) {
            IClass modelClass = classes.get(symbol);
            if (modelClass == null) {
                continue;
            }
            fillInClass(modelClass, symbol.getDefinition());
            result.add(modelClass);
        }

        return result;
    }

    private void fillInClass(final IClass modelClass, IExpression expression) {
        expression.accept(new ExpressionVisitor<Object, Object>() {
            @Override
            public Object visitSequence(ISequence sequence, Object data) {
                for (IExpression expression : sequence.getExpressions()) {
                    IPropertyDescriptor property = createProperty(expression);
                    if (property != null) {
                        modelClass.getPropertyDescriptors().add(property);
                    }
                }
                return null;
            }

            @Override
            public Object visitAlternative(IAlternative alternative, Object data) {
                for (IExpression expression : alternative.getExpressions()) {
                    createExpressionClass(expression, modelClass);
                }
                return null;
            }

            @Override
            public Object visitExpression(IExpression expression, Object data) {
                IPropertyDescriptor property = createProperty(expression);
                if (property != null) {
                    modelClass.getPropertyDescriptors().add(property);
                }
                return null;
            }
        }, null);
    }

    private IPropertyDescriptor createProperty(IExpression expression) {
        return expression.accept(new ExpressionVisitor<IPropertyDescriptor, Object>() {
            @Override
            public IPropertyDescriptor visitLiteral(ILiteral literal, Object data) {
                // Nothing to do: a constant
                return null;
            }

            @Override
            public IPropertyDescriptor visitCharacterRange(ICharacterRange characterRange, Object data) {
                if (characterRange.isNegated() || characterRange.getFrom() != characterRange.getTo()) {
                    return new PropertyDescriptor(
                       "value",
                       new PrimitiveType("Character")
                    );
                } else {
                    // A constant
                    return null;
                }
            }

            @Override
            public IPropertyDescriptor visitEmpty(IEmpty empty, Object data) {
                // A constant
                return null;
            }

            @Override
            public IPropertyDescriptor visitExpression(IExpression expression, Object data) {
                return new PropertyDescriptor("value", getExpressionType(expression));
            }
        }, null);
    }

    private IClass createExpressionClass(IExpression expression, IClass superClass) {
        IClass expressionClass = expressionClasses.get(expression);
        if (expressionClass != null) {
            return expressionClass;
        }
        expressionClass = new ModelClass(expression instanceof IAlternative, "expressionClass");
        if (superClass != null) {
            expressionClass.getSuperclasses().add(superClass);
        }
        fillInClass(expressionClass, expression);
        result.add(expressionClass);
        expressionClasses.put(expression, expressionClass);
        return expressionClass;
    }

    private IType getExpressionType(IExpression expression) {
        return expression.accept(new ExpressionVisitor<IType, Object>() {
            @Override
            public IType visitSymbolReference(ISymbolReference symbolReference, Object data) {
                ISymbol symbol = symbolReference.getReferencedSymbol();
                if (symbol.getLabels().contains("token")) {
                    return new PrimitiveType("String");
                }
                return new ObjectType(classes.get(symbol));
            }

            @Override
            public IType visitLiteral(ILiteral literal, Object data) {
                return new PrimitiveType("String");
            }

            @Override
            public IType visitCharacterRange(ICharacterRange characterRange, Object data) {
                return new PrimitiveType("Character");
            }

            @Override
            public IType visitEmpty(IEmpty empty, Object data) {
                throw new IllegalArgumentException("Optional or iterated empty");
            }

            @Override
            public IType visitAlternative(IAlternative alternative, Object data) {
                return new ObjectType(createExpressionClass(alternative, null));
            }

            @Override
            public IType visitSequence(ISequence sequence, Object data) {
                return new ObjectType(createExpressionClass(sequence, null));
            }

            @Override
            public IType visitOption(IOption option, Object data) {
                return new NullableType(getExpressionType(option.getBody()));
            }

            @Override
            public IType visitRepetition(IRepetition repetition, Object data) {
                return new ListType(repetition.getKind() == RepetitionKind.ONE_OR_MORE, getExpressionType(repetition.getBody()));
            }
        }, null);
    }
}
