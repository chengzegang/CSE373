package datastructures.concrete;
import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements IList<T> {
	// You may not rename these fields or change their types.
	// We will be inspecting these in our private tests.
	// You also may not add any additional fields.
	private Node<T> front;
	private Node<T> back;
	private int size;
	
	public DoubleLinkedList() {
		this.front = null;
		this.back = null;
		this.size = 0;
	}
	
	@Override
	public void add(T item) {
		if (front == null) {
			front = new Node<T>(item);
			back = front;
		} else {
			back.next = new Node<T>(back, item, null);
			back = back.next;
		}
		size++;
	}
	
	@Override
	public T remove() {
		if (size == 0) {
			throw new EmptyContainerException();
		}
		
		T data = back.data;
		if (size == 1) {
			front = null;
			back = null;
		} else {
			back = back.prev;
			back.next = null;
		}
		size--;
		return data;
	}
	
	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		Node<T> current = front;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current.data;
	}
	
	private Node<T> nodeAt(int index) {
		Node<T> current;
		if (index < size / 2) {
			current = front;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
		} else {
			current = back;
			for (int i = size - 1; i > index; i--) {
				current = current.prev;
			}
		}
		return current;
	}
	
	@Override
	public void set(int index, T item) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		if (size == 1) {
			front = new Node<T>(item);
			back = front;
		} else if (index == 0) {
			front = front.next;
			front.prev = new Node<T>(null, item, front);
			front = front.prev;
		} else if (index == size - 1) {
			back = back.prev;
			back.next = new Node<T>(back, item, null);
			back = back.next;
		} else {
			Node<T> prevNode;
			Node<T> nextNode;
			Node<T> current = nodeAt(index);
			prevNode = current.prev;
			nextNode = current.next;
			Node<T> node = new Node<T>(prevNode, item, nextNode);
			prevNode.next = node;
			nextNode.prev = node;
		}
		
	}
	
	@Override
	public void insert(int index, T item) {
		if (index < 0 || index >= size + 1) {
			throw new IndexOutOfBoundsException();
		}
		
		if (size == 0) {
			front = new Node<T>(item);
			back = front;
		} else if (index == 0) {
			front.prev = new Node<T>(null, item, front);
			front = front.prev;
		} else if (index == size) {
			back.next = new Node<T>(back, item, null);
			back = back.next;
		} else {
			Node<T> prevNode;
			Node<T> current = nodeAt(index);
			prevNode = current.prev;
			Node<T> node = new Node<T>(prevNode, item, current);
			prevNode.next = node;
			current.prev = node;
			
		}
		
		size++;
	}
	
	@Override
	public T delete(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		T data = null;
		if (size == 1) {
			data = front.data;
			front = null;
			back = null;
		} else if (index == 0) {
			data = front.data;
			front = front.next;
			front.prev = null;
		} else if (index == size - 1) {
			data = back.data;
			back = back.prev;
			back.next = null;
		} else {
			Node<T> prevNode;
			Node<T> nextNode;
			Node<T> current = nodeAt(index);
			data = current.data;
			prevNode = current.prev;
			nextNode = current.next;
			prevNode.next = nextNode;
			nextNode.prev = prevNode;
		}
		size--;
		return data;
	}
	
	@Override
	public int indexOf(T item) {
		Node<T> current = front;
		for (int i = 0; i < size; i++) {
			if (item == null && current.data == null || current.data.equals(item)) {
				return i;
			}
			current = current.next;
		}
		
		return -1;
	}
	
	@Override 
	public int size() {
		return size;
	}
	
	@Override
	public boolean contains(T other) {
		return indexOf(other) != -1;
	}
	
	@Override
	public Iterator<T> iterator() {
		// Note: we have provided a part of the implementation of
		// an iterator for you. You should complete the methods stubs
		// in the DoubleLinkedListIterator inner class at the bottom
		// of this file. You do not need to change this method.
		return new DoubleLinkedListIterator<>(this.front);
	}
	
	private static class Node<E> {
		// You may not change the fields in this node or add any new fields.
		public final E data;
		public Node<E> prev;
		public Node<E> next;
		
		public Node(Node<E> prev, E data, Node<E> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}
		
		public Node(E data) {
			this (null, data, null);
		}
		
		// Feel free to add additional constructors or methods to this class.
	}
	
	private static class DoubleLinkedListIterator<T> implements Iterator<T> {
		// You should not need to change this field, or add any new fields.
		private Node<T> current;
		
		public DoubleLinkedListIterator(Node<T> current) {
			// You do not need to make any changes to this constructor.
			this.current = current;
		}
		
		/**
		* Returns 'true' if the iterator still has elements to look at;
		* returns 'false' otherwise.
		*/
		public boolean hasNext() {
			return current != null;
		}
		
		/**
		* Returns the next item in the iteration and internally updates the
		* iterator to advance one element forward.
		*
		* @throws NoSuchElementException if we have reached the end of the iteration and
		*         there are no more elements to look at.
		*/
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			T data = current.data;
			current = current.next;
			return data;
		}
	}
}
