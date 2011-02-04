package org.abreslav.grammar.transformers;

import org.abreslav.grammar.*;
import org.abreslav.grammar.impl.*;
import org.abreslav.grammar.parser.Grammar;
import xtc.parser.Result;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author abreslav
 */
public class GrammarToTemplateGrammarTransformer {
    private static final Map<String, ISymbol> templateGrammar = new LinkedHashMap<String, ISymbol>();
    private static final ISymbol genericTerm;
    private static final ISymbol term;

    static {
        String fileName = "grammars/template.grm";
        try {
            Grammar grammar = new Grammar(new FileReader(fileName), fileName);
            Result result = grammar.pGrammar(0);
            if (result.hasValue()) {
                List<ISymbol> symbols = result.semanticValue();
                for (ISymbol symbol : symbols) {
                    templateGrammar.put(symbol.getName(), symbol);
                }
                genericTerm = templateGrammar.get("genericTerm");
                term = templateGrammar.get("term");
            } else {
                throw new IllegalArgumentException(result.parseError().msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final Map<ISymbol, ISymbol> symbolTrace = new HashMap<ISymbol, ISymbol>();
    private final Map<SymbolReferenceImpl, ISymbol> references = new HashMap<SymbolReferenceImpl, ISymbol>();

    public List<ISymbol> transform(List<ISymbol> symbols) {


        List<ISymbol> allSymbols = new ArrayList<ISymbol>(symbols);
        allSymbols.addAll(templateGrammar.values());
        allSymbols.remove(term);

        List<ISymbol> result = new ArrayList<ISymbol>();

        for (ISymbol symbol : allSymbols) {
            Set<String> labels = symbol.getLabels();
            if (labels.contains("token") || labels.contains("helper") || labels.contains("whitespace")) {
                result.add(symbol);
            } else {
                IExpression newDefinition = transform(symbol.getDefinition());
                if (newDefinition instanceof IAlternative) {
                    newDefinition = new AlternativeImpl(genericTermReference(), ((IAlternative) newDefinition).getExpressions());
                } else {
                    newDefinition = new AlternativeImpl(Arrays.asList(genericTermReference(), newDefinition));
                }
                SymbolImpl newSymbol = new SymbolImpl(labels, symbol.getName(), newDefinition);
                symbolTrace.put(symbol, newSymbol);
                result.add(newSymbol);
            }
        }

        List<IExpression> termDefinition = new ArrayList<IExpression>();
        for (ISymbol symbol : symbols) {
            ISymbol newSymbol = symbolTrace.get(symbol);
            if (newSymbol != null) {
                termDefinition.add(new SymbolReferenceImpl(newSymbol));
            }
        }

        SymbolImpl newTerm = new SymbolImpl(Collections.<String>emptyList(), "term", new AlternativeImpl(genericTermReference(), termDefinition));
        result.add(newTerm);
        symbolTrace.put(term, newTerm);

        for (Map.Entry<SymbolReferenceImpl, ISymbol> entry : references.entrySet()) {
            ISymbol referencedSymbol = symbolTrace.get(entry.getValue());
            if (referencedSymbol == null) {
                referencedSymbol = entry.getValue();
            }
            entry.getKey().setReferencedSymbol(referencedSymbol);
        }

        return result;
    }

    private IExpression genericTermReference() {
        return new SymbolReferenceImpl(genericTerm);
    }

    private IExpression transform(IExpression expression) {
        return expression.accept(new ExpressionVisitor<IExpression, Object>() {
            @Override
            public IExpression visitExpression(IExpression expression, Object data) {
                return expression;
            }

            @Override
            public IExpression visitSymbolReference(ISymbolReference symbolReference, Object data) {
                SymbolReferenceImpl result = new SymbolReferenceImpl(null);
                references.put(result, symbolReference.getReferencedSymbol());
                return result;
            }

            @Override
            public IExpression visitAlternative(IAlternative alternative, Object data) {
                List<IExpression> result = new ArrayList<IExpression>();
                for (IExpression expression : alternative.getExpressions()) {
                    result.add(transform(expression));
                }
                return new AlternativeImpl(result);
            }

            @Override
            public IExpression visitSequence(ISequence sequence, Object data) {
                List<IExpression> result = new ArrayList<IExpression>();
                for (IExpression expression : sequence.getExpressions()) {
                    result.add(transform(expression));
                }
                return new SequenceImpl(result);
            }

            @Override
            public IExpression visitOption(IOption option, Object data) {
                return new OptionImpl(transform(option.getBody()));
            }

            @Override
            public IExpression visitRepetition(IRepetition repetition, Object data) {
                return new RepetitionImpl(transform(repetition.getBody()), repetition.getKind());
            }
        }, null);
    }
}
