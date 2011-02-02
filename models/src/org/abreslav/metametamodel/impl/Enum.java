package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IEnum;
import org.abreslav.metametamodel.IEnumLiteral;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author abreslav
 */
public class Enum implements IEnum {
    private final String name;
    private final Set<IEnumLiteral> literals = new LinkedHashSet<IEnumLiteral>();

    public Enum(String name, Set<IEnumLiteral> literals) {
        this.name = name;
        this.literals.addAll(literals);
    }

    public Set<IEnumLiteral> getLiterals() {
        return Collections.unmodifiableSet(literals);
    }

    @Override
    public String toString() {
        return name;
    }
}
