package org.abreslav.grammatic.atf.types.unification.graph.impl;

import java.util.Set;

public interface IRepresentativeChooser<V> {
	V chooseRepresentative(Set<V> component);
}
