package org.abreslav.grammatic.utils;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class CustomLinkedHashMap<K, V> extends CustomHashMap<K, V> implements
		Map<K, V> {

	private static final long serialVersionUID = 3801124242820219131L;

	private transient Entry<K, V> myHeader;

	private final boolean myAccessOrder;

	public CustomLinkedHashMap(int initialCapacity, float loadFactor, IHashingStrategy hashingStrategy) {
		super(initialCapacity, loadFactor, hashingStrategy);
		myAccessOrder = false;
	}

	public CustomLinkedHashMap(int initialCapacity, IHashingStrategy hashingStrategy) {
		super(initialCapacity, hashingStrategy);
		myAccessOrder = false;
	}

	public CustomLinkedHashMap(IHashingStrategy hashingStrategy) {
		super(hashingStrategy);
		myAccessOrder = false;
	}

	public CustomLinkedHashMap(Map<? extends K, ? extends V> m, IHashingStrategy hashingStrategy) {
		super(m, hashingStrategy);
		myAccessOrder = false;
	}

	public CustomLinkedHashMap(int initialCapacity, float loadFactor,
			boolean accessOrder, IHashingStrategy hashingStrategy) {
		super(initialCapacity, loadFactor, hashingStrategy);
		this.myAccessOrder = accessOrder;
	}

	void init() {
		myHeader = new Entry<K, V>(-1, null, null, null, myHashingStrategy);
		myHeader.before = myHeader.after = myHeader;
	}

	void transfer(CustomHashMap.Entry[] newTable) {
		int newCapacity = newTable.length;
		for (Entry<K, V> e = myHeader.after; e != myHeader; e = e.after) {
			int index = indexFor(e.hash, newCapacity);
			e.next = newTable[index];
			newTable[index] = e;
		}
	}

	public boolean containsValue(Object value) {
		if (value == null) {
			for (Entry e = myHeader.after; e != myHeader; e = e.after)
				if (e.value == null)
					return true;
		} else {
			for (Entry e = myHeader.after; e != myHeader; e = e.after)
				if (value.equals(e.value))
					return true;
		}
		return false;
	}

	public V get(Object key) {
		Entry<K, V> e = (Entry<K, V>) getEntry(key);
		if (e == null)
			return null;
		e.recordAccess(this);
		return e.value;
	}

	public void clear() {
		super.clear();
		myHeader.before = myHeader.after = myHeader;
	}

	private static class Entry<K, V> extends CustomHashMap.Entry<K, V> {
		Entry<K, V> before, after;

		Entry(int hash, K key, V value, CustomHashMap.Entry<K, V> next, IHashingStrategy hashingStrategy) {
			super(hash, key, value, next, hashingStrategy);
		}

		private void remove() {
			before.after = after;
			after.before = before;
		}

		private void addBefore(Entry<K, V> existingEntry) {
			after = existingEntry;
			before = existingEntry.before;
			before.after = this;
			after.before = this;
		}

		void recordAccess(CustomHashMap<K, V> m) {
			CustomLinkedHashMap<K, V> lm = (CustomLinkedHashMap<K, V>) m;
			if (lm.myAccessOrder) {
				lm.myModCount++;
				remove();
				addBefore(lm.myHeader);
			}
		}

		void recordRemoval(HashMap<K, V> m) {
			remove();
		}
	}

	private abstract class LinkedHashIterator<T> implements Iterator<T> {
		Entry<K, V> nextEntry = myHeader.after;
		Entry<K, V> lastReturned = null;

		int expectedModCount = myModCount;

		public boolean hasNext() {
			return nextEntry != myHeader;
		}

		public void remove() {
			if (lastReturned == null)
				throw new IllegalStateException();
			if (myModCount != expectedModCount)
				throw new ConcurrentModificationException();

			CustomLinkedHashMap.this.remove(lastReturned.key);
			lastReturned = null;
			expectedModCount = myModCount;
		}

		Entry<K, V> nextEntry() {
			if (myModCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (nextEntry == myHeader)
				throw new NoSuchElementException();

			Entry<K, V> e = lastReturned = nextEntry;
			nextEntry = e.after;
			return e;
		}
	}

	private class KeyIterator extends LinkedHashIterator<K> {
		public K next() {
			return nextEntry().getKey();
		}
	}

	private class ValueIterator extends LinkedHashIterator<V> {
		public V next() {
			return nextEntry().value;
		}
	}

	private class EntryIterator extends LinkedHashIterator<Map.Entry<K, V>> {
		public Map.Entry<K, V> next() {
			return nextEntry();
		}
	}

	// These Overrides alter the behavior of superclass view iterator() methods
	Iterator<K> newKeyIterator() {
		return new KeyIterator();
	}

	Iterator<V> newValueIterator() {
		return new ValueIterator();
	}

	Iterator<Map.Entry<K, V>> newEntryIterator() {
		return new EntryIterator();
	}

	void addEntry(int hash, K key, V value, int bucketIndex) {
		createEntry(hash, key, value, bucketIndex);

		Entry<K, V> eldest = myHeader.after;
		if (removeEldestEntry(eldest)) {
			removeEntryForKey(eldest.key);
		} else {
			if (mySize >= myThreshold)
				resize(2 * myTable.length);
		}
	}

	void createEntry(int hash, K key, V value, int bucketIndex) {
		CustomHashMap.Entry<K, V> old = myTable[bucketIndex];
		Entry<K, V> e = new Entry<K, V>(hash, key, value, old, myHashingStrategy);
		myTable[bucketIndex] = e;
		e.addBefore(myHeader);
		mySize++;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return false;
	}
}
