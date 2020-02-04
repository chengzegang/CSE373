package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* TODO: Replace this file with the one you wrote from project 1
* TODO: Add the missing "iterator()" method
*/
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
	// You may not change or rename this field: we will be inspecting
	// it using our private tests.
	private Pair<K, V>[] pairs;
	private int size;
	
	// You're encouraged to add extra fields (and helper methods) though!
	
	public ArrayDictionary() {
		pairs = null;
	}
	
	public ArrayDictionary(K key, V value) {
		pairs = this.makeArrayOfPairs(10);
		pairs[0] = new Pair<K, V>(key, value);
		size = 1;
	}
	
	/**
	* This method will return a new, empty array of the given size
	* that can contain Pair<K, V> objects.
	*
	* Note that each element in the array will initially be null.
	*/
	@SuppressWarnings("unchecked")
	private Pair<K, V>[] makeArrayOfPairs(int length) {
		// It turns out that creating arrays of generic objects in Java
		// is complicated due to something known as 'type erasure'.
		//
		// We've given you this helper method to help simplify this part of
		// your assignment. Use this helper method as appropriate when
		// implementing the rest of this class.
		//
		// You are not required to understand how this method works, what
		// type erasure is, or how arrays and generics interact. Do not
		// modify this method in any way.
		return (Pair<K, V>[]) (new Pair[length]);
		
	}
	
	private int indexOf(K key) {
		if (size == 0) {
			return -1;
		}
		for (int i = 0; i < size; i++) {
			if (key == null && pairs[i].key == null || pairs[i].key != null &&
			key != null && pairs[i].key.equals(key)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public V get(K key) {
		int index = indexOf(key);
		if (size == 0 || index == -1) {
			throw new NoSuchKeyException();
		}
		return pairs[index].value;
	}
	
	@Override
	public void put(K key, V value) {
		if (size == 0) {
			pairs = this.makeArrayOfPairs(10);
			pairs[0] = new Pair<K, V>(key, value);
			size = 1;
		} else {
			int index = indexOf(key);
			if (index != -1) {
				pairs[index].value = value;
			} else if (size == pairs.length) {
				Pair<K, V> increased[] = makeArrayOfPairs(pairs.length * 2);
				for (int i = 0; i < pairs.length; i++) {
					increased[i] = pairs[i];
				}
				increased[pairs.length] = new Pair<K, V>(key, value);
				pairs = increased;
				size++;
			} else {
				pairs[size] = new Pair<K, V>(key, value);
				size++;
			}
		}
	}
	
	@Override
	public V remove(K key) {
		int index = indexOf(key);
		if (size == 0 || index == -1) {
			throw new NoSuchKeyException();
		}
		V item = pairs[index].value;
		for (int i = index; i < pairs.length - 1; i++) {
			pairs[i] = pairs[i + 1];
		}
		size--;
		return item;
	}
	
	@Override
	public boolean containsKey(K key) {
		return indexOf(key) != -1;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Iterator<KVPair<K, V>> iterator() {
		return new ArrayDictionaryIterator<KVPair<K, V>>(pairs, size);
	}
	
	public class ArrayDictionaryIterator<T> implements Iterator<KVPair<K, V>> {
		
		private Pair<K, V>[] pairs;
		private int size;
		private int currentIndex;
		
		public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
			this.pairs = pairs;
			this.size = size;
			currentIndex = 0;
		}
		
		public boolean hasNext() {
			if (size != 0 && currentIndex < size && size - currentIndex != 1 || size - currentIndex == 1) {
				return true;
			}
			return false;
		}
		
		public KVPair<K, V> next() {
			if (!hasNext() && currentIndex != size -1) {
				throw new NoSuchElementException();
			}
			currentIndex++;
			return new KVPair<>(pairs[currentIndex - 1].key, pairs[currentIndex - 1].value);
		}
	}
	
	private static class Pair<K, V> {
		public K key;
		public V value;
		
		// You may add constructors and methods to this class as necessary.
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.key + "=" + this.value;
		}
	}
}
