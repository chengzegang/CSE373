package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* See the spec and IDictionary for more details on what each method should do
*/
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
	// You may not change or rename this field: we will be inspecting
	// it using our private tests.
	private IDictionary<K, V>[] chains;
	private int size;
	private static final double LOAD_FACTOR = 0.75;
	
	// You're encouraged to add extra fields (and helper methods) though!
	
	public ChainedHashDictionary() {
		this (16);
	}
	
	public ChainedHashDictionary(int length) {
		size = 0;
		chains = makeArrayOfChains(length);
	}
	
	private int hash(K key, int mod) {
		return key == null ? 0 : Math.abs(key.hashCode() % mod);
	}
	
	private void resize() {
		IDictionary<K, V>[] d = makeArrayOfChains(chains.length * 2);
		Iterator<KVPair<K, V>> it = this.iterator();
		while (it.hasNext()) {
			KVPair<K, V> p = it.next();
			int code = hash(p.getKey(), d.length);
			if (d[code] == null) {
				d[code] = new ArrayDictionary<>(p.getKey(), p.getValue());
			} else {
				d[code].put(p.getKey(), p.getValue());
			}
		}
		chains = d;
	}
	/**
	* This method will return a new, empty array of the given size
	* that can contain IDictionary<K, V> objects.
	*
	* Note that each element in the array will initially be null.
	*/
	@SuppressWarnings("unchecked")
	private IDictionary<K, V>[] makeArrayOfChains(int size) {
		// Note: You do not need to modify this method.
		// See ArrayDictionary's makeArrayOfPairs(...) method for
		// more background on why we need this method.
		return (IDictionary<K, V>[]) new IDictionary[size];
	}
	
	@Override
	public V get(K key) {
		if (!containsKey(key)) {
			throw new NoSuchKeyException();
		}
		
		int code = hash(key, chains.length);
		return chains[code].get(key);
	}
	
	@Override
	public void put(K key, V value) {
		if (size > chains.length * LOAD_FACTOR) {
			resize();
		}
		
		int code = hash(key, chains.length);
		if (containsKey(key)) {
			chains[code].put(key, value);
			size--;
		} else if (chains[code] == null) {
			chains[code] = new ArrayDictionary<>(key, value);
		} else {
			chains[code].put(key, value);
			if (chains[code].size() > chains.length) {
				resize();
			}
		}
		
		size++;
	}
	
	
	
	@Override
	public V remove(K key) {
		if (!containsKey(key)) {
			throw new NoSuchKeyException();
		}
		
		size--;
		int code = hash(key, chains.length);
		V value = chains[code].remove(key);
		if (chains[code].size() == 0) {
			chains[code] = null;
		}
		return value;
	}
	
	@Override
	public boolean containsKey(K key) {
		int code = hash(key, chains.length);
		if (code >= chains.length  || chains[code] == null) {
			return false;
		}
		return chains[code].containsKey(key);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Iterator<KVPair<K, V>> iterator() {
		// Note: you do not need to change this method
		return new ChainedIterator<>(this.chains);
	}
	
	/**
	* Hints:
	*
	* 1. You should add extra fields to keep track of your iteration
	*    state. You can add as many fields as you want. If it helps,
	*    our reference implementation uses three (including the one we
	*    gave you).
	*
	* 2. Before you try and write code, try designing an algorithm
	*    using pencil and paper and run through a few examples by hand.
	*
	* 3. Think about what exactly your *invariants* are. An *invariant*
	*    is something that must *always* be true once the constructor is
	*    done setting up the class AND must *always* be true both before and
	*    after you call any method in your class.
	*
	*    Once you've decided, write them down in a comment somewhere to
	*    help you remember.
	*
	*    You may also find it useful to write a helper method that checks
	*    your invariants and throws an exception if they're violated.
	*    You can then call this helper method at the start and end of each
	*    method if you're running into issues while debugging.
	*
	*    (Be sure to delete this method once your iterator is fully working.)
	*
	* Implementation restrictions:
	*
	* 1. You **MAY NOT** create any new data structures. Iterators
	*    are meant to be lightweight and so should not be copying
	*    the data contained in your dictionary to some other data
	*    structure.
	*
	* 2. You **MAY** call the `.iterator()` method on each IDictionary
	*    instance inside your 'chains' array, however.
	*/
	private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
		private IDictionary<K, V>[] chains;
		private int current;
		private Iterator<KVPair<K, V>> it;
		
		public ChainedIterator(IDictionary<K, V>[] chains) {
			this.chains = chains;
			this.current = -1;
			while (it == null && current < chains.length - 1) {
				current++;
				if (chains[current] != null) {
					it = chains[current].iterator();
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			while (current < chains.length - 1 && !it.hasNext()) {
				current++;
				if (chains[current] != null) {
					it = chains[current].iterator();
				}
			}
			
			if (it == null) {
				return false;
			}
			return it.hasNext();
		}
		
		@Override
		public KVPair<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			return it.next();
		}
	}
}
