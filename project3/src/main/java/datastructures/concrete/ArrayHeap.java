package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
* See IPriorityQueue for details on what each method must do.
*/
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
	// See spec: you must implement a implement a 4-heap.
	private static final int NUM_CHILDREN = 4;
	
	// You MUST use this field to store the contents of your heap.
	// You may NOT rename this field: we will be inspecting it within
	// our private tests.
	private T[] heap;
	private int size;
	
	// Feel free to add more fields and constants.
	
	public ArrayHeap() {
		heap = makeArrayOfT(16);
		size = 0;
	}
	
	/**
	* This method will return a new, empty array of the given arraySize
	* that can contain elements of type T.
	*
	* Note that each element in the array will initially be null.
	*/
	@SuppressWarnings("unchecked")
	private T[] makeArrayOfT(int length) {
		// This helper method is basically the same one we gave you
		// in ArrayDictionary and ChainedHashDictionary.
		//
		// As before, you do not need to understand how this method
		// works, and should not modify it in any way.
		return (T[]) (new Comparable[length]);
	}
	
	private void resize() {
		T[] newHeap = makeArrayOfT(heap.length * 2);
		for (int i = 0; i < this.heap.length; i++) {
			newHeap[i] = this.heap[i];
		}
		this.heap = newHeap;
	}
	
	@Override
	public T removeMin() {
		if (isEmpty()) {
			throw new EmptyContainerException();
		}
		
		T output = heap[0];
		size--;
		
		if (size == 0) {
			heap[0] = null;
		} else {
			heap[0] = heap[size];
			heap[size] = null;
			if (size > 1) {
				percolateDown(0);
			}
		}
		return output;
	}
	
	@Override
	public T peekMin() {
		if (isEmpty()) {
			throw new EmptyContainerException();
		}
		return heap[0];
	}
	
	@Override
	public void insert(T item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		if (size == heap.length) {
			resize();
		}
		
		heap[size] = item;
		size++;
		
		if (size > 1) {
			percolateUp(size - 1);
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	private void percolateUp(int index) {
		int parent = (index - 1) / NUM_CHILDREN;
		if (heap[index].compareTo(heap[parent]) < 0) {
			T container = heap[index];
			heap[index] = heap[parent];
			heap[parent] = container;
			percolateUp(parent);
		}
	}
	
	private void percolateDown(int index) {
		int indexOfMin = index;
		for (int i = 4 * index + 1; i <= 4 * index + 4 && i < size; i++) {
			if (heap[i].compareTo(heap[indexOfMin]) < 0) {
				indexOfMin = i;
			}
		}
		
		if (indexOfMin != index) {
			T container = heap[index];
			heap[index] = heap[indexOfMin];
			heap[indexOfMin] = container;
			if (indexOfMin * 4 + 1 < size) {
				percolateDown(indexOfMin);
			}
		}
	}
}
