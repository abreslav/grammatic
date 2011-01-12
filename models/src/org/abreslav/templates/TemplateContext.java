package org.abreslav.templates;

import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.IVariable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author abreslav
 */
public class TemplateContext implements  ITemplateContext {
    private final ITemplateContext delegate;
    private final Map<IVariable, ITerm> data = new LinkedHashMap<IVariable, ITerm>();

    public TemplateContext(ITemplateContext delegate) {
        this.delegate = delegate;
    }

    public TemplateContext() {
        this(ITemplateContext.EMPTY);
    }

    public ITerm getValue(IVariable variable) {
        ITerm term = data.get(variable);
        if (term == null) {
            return delegate.getValue(variable);
        }
        return term;
    }

    public void putValue(IVariable variable, ITerm value) {
        ITerm oldTerm = data.put(variable, value);
        if (oldTerm != null) {
            throw new IllegalStateException("Variable reassigned");
        }
    }
}
