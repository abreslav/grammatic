package org.abreslav.grammar;

import java.util.Set;

/**
 * @author abreslav
 */
public interface ISymbol {
    String getName();
    IExpression getDefinition();
    Set<String> getLabels();
}
