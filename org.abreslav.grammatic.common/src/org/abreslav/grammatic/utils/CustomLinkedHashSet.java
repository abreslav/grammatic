package org.abreslav.grammatic.utils;

import java.util.Collection;
import java.util.Set;

public class CustomLinkedHashSet<E> extends CustomHashSet<E> implements Set<E>,
		Cloneable, java.io.Serializable {

	private static final long serialVersionUID = -2851667679971038690L;

	public CustomLinkedHashSet(IHashingStrategy hashingStrategy) {
		super(16, .75f, true, hashingStrategy);
	}

	public CustomLinkedHashSet(Collection<? extends E> c, IHashingStrategy hashingStrategy) {
		super(Math.max(2 * c.size(), 11), .75f, true, hashingStrategy);
		addAll(c);
	}
}
