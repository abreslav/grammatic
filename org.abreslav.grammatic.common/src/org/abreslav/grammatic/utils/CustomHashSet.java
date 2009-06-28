package org.abreslav.grammatic.utils;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CustomHashSet<E> extends AbstractSet<E> implements Set<E> {

	private transient Map<E, Object> myMap;

	private static final Object PRESENT = new Object();

	public CustomHashSet() {
		myMap = new HashMap<E, Object>();
	}
	
	public CustomHashSet(Collection<? extends E> c) {
		myMap = new HashMap<E, Object>(Math.max((int) (c.size() / .75f) + 1, 16));
		addAll(c);
	}
	public CustomHashSet(IHashingStrategy hashingStrategy) {
		myMap = new CustomHashMap<E, Object>(hashingStrategy);
	}

	public CustomHashSet(Collection<? extends E> c, IHashingStrategy hashingStrategy) {
		myMap = new CustomHashMap<E, Object>(Math.max((int) (c.size() / .75f) + 1, 16), CustomHashMap.DEFAULT_LOAD_FACTOR, hashingStrategy);
		addAll(c);
	}

    CustomHashSet(int initialCapacity, float loadFactor, boolean dummy, IHashingStrategy hashingStrategy) {
		myMap = new CustomLinkedHashMap<E, Object>(initialCapacity, loadFactor, hashingStrategy);
	}

    public Iterator<E> iterator() {
		return myMap.keySet().iterator();
	}

	public int size() {
		return myMap.size();
	}

	public boolean isEmpty() {
		return myMap.isEmpty();
	}

	public boolean contains(Object o) {
		return myMap.containsKey(o);
	}

	public boolean add(E e) {
		return myMap.put(e, PRESENT) == null;
	}

	public boolean remove(Object o) {
		return myMap.remove(o) == PRESENT;
	}

	public void clear() {
		myMap.clear();
	}

}
