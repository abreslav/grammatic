package org.abreslav.metametamodel.impl;

import org.abreslav.metametamodel.IEnumLiteral;

/**
 * @author abreslav
 */
public class EnumLiteral implements IEnumLiteral {
    private final String name;

    public EnumLiteral(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
