package org.abreslav.templates;

import org.abreslav.lambda.ITerm;
import org.abreslav.lambda.IVariable;

/**
 * @author abreslav
 */
public interface ITemplateContext {
    ITemplateContext EMPTY = new ITemplateContext() {
        public ITerm getValue(IVariable variable) {
            return null;
        }
    };

    public ITerm getValue(IVariable variable);
}
