package org.abreslav.grammatic.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
// A HashMap with a strategy for hashing and comparing
public class CustomHashMap<K, V> extends AbstractMap<K, V> implements
		Map<K, V>, Cloneable, Serializable {

	static final int DEFAULT_INITIAL_CAPACITY = 16;

	static final int MAXIMUM_CAPACITY = 1 << 30;

	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	protected final transient IHashingStrategy myHashingStrategy;

	transient Entry[] myTable;

	transient int mySize;

	int myThreshold;

	final float myLoadFactor;

	transient volatile int myModCount;

	transient volatile Set<K> myKeySet = null;
	transient volatile Collection<V> myValues = null;

	public CustomHashMap(int initialCapacity, float loadFactor,
			IHashingStrategy hashingStrategy) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal initial capacity: "
					+ initialCapacity);
		if (initialCapacity > MAXIMUM_CAPACITY)
			initialCapacity = MAXIMUM_CAPACITY;
		if (loadFactor <= 0 || Float.isNaN(loadFactor))
			throw new IllegalArgumentException("Illegal load factor: "
					+ loadFactor);

		int capacity = 1;
		while (capacity < initialCapacity)
			capacity <<= 1;

		this.myLoadFactor = loadFactor;
		myThreshold = (int) (capacity * loadFactor);
		myTable = new Entry[capacity];
		myHashingStrategy = hashingStrategy;
		init();
	}

	public CustomHashMap(IHashingStrategy hashingStrategy) {
		this.myLoadFactor = DEFAULT_LOAD_FACTOR;
		myThreshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
		myTable = new Entry[DEFAULT_INITIAL_CAPACITY];
		myHashingStrategy = hashingStrategy;
		init();
	}

    public CustomHashMap(int initialCapacity, IHashingStrategy hashingStrategy) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, hashingStrategy);
    }

    public CustomHashMap(Map<? extends K, ? extends V> m, IHashingStrategy hashingStrategy) {
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
                      DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR, hashingStrategy);
        putAllForCreate(m);
    }

    void init() {
	}

	static int hash(int h) {

		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	public int size() {
		return mySize;
	}

	public boolean isEmpty() {
		return mySize == 0;
	}

	public V get(Object key) {
		if (key == null)
			return getForNullKey();
		int hash = hash(myHashingStrategy.hashCode(key));
		for (Entry<K, V> e = myTable[indexFor(hash, myTable.length)]; e != null; e = e.next) {
			Object k;
			if (e.hash == hash
					&& ((k = e.key) == key || myHashingStrategy.equals(key, k)))
				return e.value;
		}
		return null;
	}

	private V getForNullKey() {
		for (Entry<K, V> e = myTable[0]; e != null; e = e.next) {
			if (e.key == null)
				return e.value;
		}
		return null;
	}

	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	final Entry<K, V> getEntry(Object key) {
		int hash = (key == null) ? 0 : hash(myHashingStrategy.hashCode(key));
		for (Entry<K, V> e = myTable[indexFor(hash, myTable.length)]; e != null; e = e.next) {
			Object k;
			if (e.hash == hash
					&& ((k = e.key) == key || (key != null && myHashingStrategy
							.equals(key, k))))
				return e;
		}
		return null;
	}

	public V put(K key, V value) {
		if (key == null)
			return putForNullKey(value);
		int hash = hash(myHashingStrategy.hashCode(key));
		int i = indexFor(hash, myTable.length);
		for (Entry<K, V> e = myTable[i]; e != null; e = e.next) {
			Object k;
			if (e.hash == hash
					&& ((k = e.key) == key || myHashingStrategy.equals(key, k))) {
				V oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}

		myModCount++;
		addEntry(hash, key, value, i);
		return null;
	}

	private V putForNullKey(V value) {
		for (Entry<K, V> e = myTable[0]; e != null; e = e.next) {
			if (e.key == null) {
				V oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}
		myModCount++;
		addEntry(0, null, value, 0);
		return null;
	}

	private void putForCreate(K key, V value) {
		int hash = (key == null) ? 0 : hash(myHashingStrategy.hashCode(key));
		int i = indexFor(hash, myTable.length);

		for (Entry<K, V> e = myTable[i]; e != null; e = e.next) {
			Object k;
			if (e.hash == hash
					&& ((k = e.key) == key || (key != null && myHashingStrategy
							.equals(key, k)))) {
				e.value = value;
				return;
			}
		}

		createEntry(hash, key, value, i);
	}

	private void putAllForCreate(Map<? extends K, ? extends V> m) {
		for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m
				.entrySet().iterator(); i.hasNext();) {
			Map.Entry<? extends K, ? extends V> e = i.next();
			putForCreate(e.getKey(), e.getValue());
		}
	}

	void resize(int newCapacity) {
		Entry[] oldTable = myTable;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			myThreshold = Integer.MAX_VALUE;
			return;
		}

		Entry[] newTable = new Entry[newCapacity];
		transfer(newTable);
		myTable = newTable;
		myThreshold = (int) (newCapacity * myLoadFactor);
	}

	void transfer(Entry[] newTable) {
		Entry[] src = myTable;
		int newCapacity = newTable.length;
		for (int j = 0; j < src.length; j++) {
			Entry<K, V> e = src[j];
			if (e != null) {
				src[j] = null;
				do {
					Entry<K, V> next = e.next;
					int i = indexFor(e.hash, newCapacity);
					e.next = newTable[i];
					newTable[i] = e;
					e = next;
				} while (e != null);
			}
		}
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		int numKeysToBeAdded = m.size();
		if (numKeysToBeAdded == 0)
			return;

		if (numKeysToBeAdded > myThreshold) {
			int targetCapacity = (int) (numKeysToBeAdded / myLoadFactor + 1);
			if (targetCapacity > MAXIMUM_CAPACITY)
				targetCapacity = MAXIMUM_CAPACITY;
			int newCapacity = myTable.length;
			while (newCapacity < targetCapacity)
				newCapacity <<= 1;
			if (newCapacity > myTable.length)
				resize(newCapacity);
		}

		for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m
				.entrySet().iterator(); i.hasNext();) {
			Map.Entry<? extends K, ? extends V> e = i.next();
			put(e.getKey(), e.getValue());
		}
	}

	public V remove(Object key) {
		Entry<K, V> e = removeEntryForKey(key);
		return (e == null ? null : e.value);
	}

	final Entry<K, V> removeEntryForKey(Object key) {
		int hash = (key == null) ? 0 : hash(myHashingStrategy.hashCode(key));
		int i = indexFor(hash, myTable.length);
		Entry<K, V> prev = myTable[i];
		Entry<K, V> e = prev;

		while (e != null) {
			Entry<K, V> next = e.next;
			Object k;
			if (e.hash == hash
					&& ((k = e.key) == key || (key != null && myHashingStrategy
							.equals(key, k)))) {
				myModCount++;
				mySize--;
				if (prev == e)
					myTable[i] = next;
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	final Entry<K, V> removeMapping(Object o) {
		if (!(o instanceof Map.Entry))
			return null;

		Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		Object key = entry.getKey();
		int hash = (key == null) ? 0 : hash(myHashingStrategy.hashCode(key));
		int i = indexFor(hash, myTable.length);
		Entry<K, V> prev = myTable[i];
		Entry<K, V> e = prev;

		while (e != null) {
			Entry<K, V> next = e.next;
			if (e.hash == hash && e.equals(entry)) {
				myModCount++;
				mySize--;
				if (prev == e)
					myTable[i] = next;
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	public void clear() {
		myModCount++;
		Entry[] tab = myTable;
		for (int i = 0; i < tab.length; i++)
			tab[i] = null;
		mySize = 0;
	}

	public boolean containsValue(Object value) {
		if (value == null)
			return containsNullValue();

		Entry[] tab = myTable;
		for (int i = 0; i < tab.length; i++)
			for (Entry e = tab[i]; e != null; e = e.next)
				if (value.equals(e.value))
					return true;
		return false;
	}

	private boolean containsNullValue() {
		Entry[] tab = myTable;
		for (int i = 0; i < tab.length; i++)
			for (Entry e = tab[i]; e != null; e = e.next)
				if (e.value == null)
					return true;
		return false;
	}

	public Object clone() {
		CustomHashMap<K, V> result = null;
		try {
			result = (CustomHashMap<K, V>) super.clone();
		} catch (CloneNotSupportedException e) {

		}
		result.myTable = new Entry[myTable.length];
		result.entrySet = null;
		result.myModCount = 0;
		result.mySize = 0;
		result.init();
		result.putAllForCreate(this);

		return result;
	}

	static class Entry<K, V> implements Map.Entry<K, V> {
		final K key;
		V value;
		Entry<K, V> next;
		final int hash;
		final IHashingStrategy myHashingStrategy;

		Entry(int h, K k, V v, Entry<K, V> n, IHashingStrategy hashingStrategy) {
			value = v;
			next = n;
			key = k;
			hash = h;
			myHashingStrategy = hashingStrategy;
		}

		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && myHashingStrategy.equals(k1, k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

		public final int hashCode() {
			return (key == null ? 0 : myHashingStrategy.hashCode(key))
					^ (value == null ? 0 : value.hashCode());
		}

		public final String toString() {
			return getKey() + "=" + getValue();
		}

		void recordAccess(CustomHashMap<K, V> m) {
		}

		void recordRemoval(CustomHashMap<K, V> m) {
		}
	}

	void addEntry(int hash, K key, V value, int bucketIndex) {
		Entry<K, V> e = myTable[bucketIndex];
		myTable[bucketIndex] = new Entry<K, V>(hash, key, value, e, myHashingStrategy);
		if (mySize++ >= myThreshold)
			resize(2 * myTable.length);
	}

	void createEntry(int hash, K key, V value, int bucketIndex) {
		Entry<K, V> e = myTable[bucketIndex];
		myTable[bucketIndex] = new Entry<K, V>(hash, key, value, e, myHashingStrategy);
		mySize++;
	}

	private abstract class HashIterator<E> implements Iterator<E> {
		Entry<K, V> next;
		int expectedModCount;
		int index;
		Entry<K, V> current;

		HashIterator() {
			expectedModCount = myModCount;
			if (mySize > 0) {
				Entry[] t = myTable;
				while (index < t.length && (next = t[index++]) == null)
					;
			}
		}

		public final boolean hasNext() {
			return next != null;
		}

		final Entry<K, V> nextEntry() {
			if (myModCount != expectedModCount)
				throw new ConcurrentModificationException();
			Entry<K, V> e = next;
			if (e == null)
				throw new NoSuchElementException();

			if ((next = e.next) == null) {
				Entry[] t = myTable;
				while (index < t.length && (next = t[index++]) == null)
					;
			}
			current = e;
			return e;
		}

		public void remove() {
			if (current == null)
				throw new IllegalStateException();
			if (myModCount != expectedModCount)
				throw new ConcurrentModificationException();
			Object k = current.key;
			current = null;
			CustomHashMap.this.removeEntryForKey(k);
			expectedModCount = myModCount;
		}

	}

	private final class ValueIterator extends HashIterator<V> {
		public V next() {
			return nextEntry().value;
		}
	}

	private final class KeyIterator extends HashIterator<K> {
		public K next() {
			return nextEntry().getKey();
		}
	}

	private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
		public Map.Entry<K, V> next() {
			return nextEntry();
		}
	}

	Iterator<K> newKeyIterator() {
		return new KeyIterator();
	}

	Iterator<V> newValueIterator() {
		return new ValueIterator();
	}

	Iterator<Map.Entry<K, V>> newEntryIterator() {
		return new EntryIterator();
	}

	private transient Set<Map.Entry<K, V>> entrySet = null;

	public Set<K> keySet() {
		Set<K> ks = myKeySet;
		return (ks != null ? ks : (myKeySet = new KeySet()));
	}

	private final class KeySet extends AbstractSet<K> {
		public Iterator<K> iterator() {
			return newKeyIterator();
		}

		public int size() {
			return mySize;
		}

		public boolean contains(Object o) {
			return containsKey(o);
		}

		public boolean remove(Object o) {
			return CustomHashMap.this.removeEntryForKey(o) != null;
		}

		public void clear() {
			CustomHashMap.this.clear();
		}
	}

	public Collection<V> values() {
		Collection<V> vs = myValues;
		return (vs != null ? vs : (myValues = new Values()));
	}

	private final class Values extends AbstractCollection<V> {
		public Iterator<V> iterator() {
			return newValueIterator();
		}

		public int size() {
			return mySize;
		}

		public boolean contains(Object o) {
			return containsValue(o);
		}

		public void clear() {
			CustomHashMap.this.clear();
		}
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet0();
	}

	private Set<Map.Entry<K, V>> entrySet0() {
		Set<Map.Entry<K, V>> es = entrySet;
		return es != null ? es : (entrySet = new EntrySet());
	}

	private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		public Iterator<Map.Entry<K, V>> iterator() {
			return newEntryIterator();
		}

		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<K, V> e = (Map.Entry<K, V>) o;
			Entry<K, V> candidate = getEntry(e.getKey());
			return candidate != null && candidate.equals(e);
		}

		public boolean remove(Object o) {
			return removeMapping(o) != null;
		}

		public int size() {
			return mySize;
		}

		public void clear() {
			CustomHashMap.this.clear();
		}
	}

	private void writeObject(java.io.ObjectOutputStream s) throws IOException {
		Iterator<Map.Entry<K, V>> i = (mySize > 0) ? entrySet0().iterator()
				: null;

		s.defaultWriteObject();

		s.writeInt(myTable.length);

		s.writeInt(mySize);

		if (i != null) {
			while (i.hasNext()) {
				Map.Entry<K, V> e = i.next();
				s.writeObject(e.getKey());
				s.writeObject(e.getValue());
			}
		}
	}

	private static final long serialVersionUID = 362498820763181265L;

	private void readObject(java.io.ObjectInputStream s) throws IOException,
			ClassNotFoundException {

		s.defaultReadObject();

		int numBuckets = s.readInt();
		myTable = new Entry[numBuckets];

		init();

		int size = s.readInt();

		for (int i = 0; i < size; i++) {
			K key = (K) s.readObject();
			V value = (V) s.readObject();
			putForCreate(key, value);
		}
	}

	int capacity() {
		return myTable.length;
	}

	float loadFactor() {
		return myLoadFactor;
	}
}
