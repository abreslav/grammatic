package org.abreslav.metametamodel;

import java.util.Set;

/**
 * @author abreslav
 */
public interface IEnum extends IClassifier {
    Set<IEnumLiteral> getLiterals();
}
