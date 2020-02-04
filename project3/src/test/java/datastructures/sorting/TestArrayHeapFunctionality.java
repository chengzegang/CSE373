package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestArrayHeapFunctionality extends BaseTest {
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
		return new ArrayHeap<>();
	}
	
	@Test(timeout=SECOND)
	public void testBasicSize() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		heap.insert(3);
		assertEquals(1, heap.size());
	}
	
	@Test(timeout=SECOND)
	public void testSizeUpdate() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 0; i < 20; i++) {
			assertEquals(i, heap.size());
			heap.insert(i);
		}
		
		assertEquals(20, heap.size());
		heap.peekMin();
		assertEquals(20, heap.size());
		heap.peekMin();
		assertEquals(20, heap.size());
		heap.peekMin();
		assertEquals(20, heap.size());
		
		for (int i = 20; i > 0; i--) {
			assertEquals(i, heap.size());
			heap.removeMin();
		}
		
		assertEquals(0, heap.size());
	}
	
	@Test(timeout=SECOND)
	public void testInsertAndRemoveMinWithComparableObject() {
		IPriorityQueue<String> heap = this.makeInstance();
		
		heap.insert("a");
		heap.insert("banana");
		heap.insert("apple");
		heap.insert("b");
		heap.insert("ape");
		heap.insert("a");
		heap.insert("bad");
		
		assertEquals("a", heap.removeMin());
		assertEquals("a", heap.removeMin());
		assertEquals("ape", heap.removeMin());
		assertEquals("apple", heap.removeMin());
		assertEquals("b", heap.removeMin());
		assertEquals("bad", heap.removeMin());
		assertEquals("banana", heap.removeMin());
	}
	
	@Test(timeout=SECOND)
	public void testInsertAndRemoveMin() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		
		heap.insert(3);
		heap.insert(9);
		heap.insert(4);
		heap.insert(14);
		heap.insert(10);
		heap.insert(3);
		
		assertEquals(3, heap.removeMin());
		assertEquals(3, heap.removeMin());
		assertEquals(4, heap.removeMin());
		assertEquals(9, heap.removeMin());
		assertEquals(10, heap.removeMin());
		assertEquals(14, heap.removeMin());
	}
	
	@Test(timeout=SECOND)
	public void testRemoveMinEmptyException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		try {
			heap.removeMin();
			fail("should throw EmptyContainerException");
		} catch (EmptyContainerException e) {
			 // This is ok: do nothing
		}
	}
	
	@Test(timeout=SECOND)
	public void tsetInsertNull() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		try {
			heap.insert(null);
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			 // This is ok: do nothing
		}
	}
	
	@Test(timeout=SECOND)
	public void testBasicPeekMin() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		heap.insert(3);
		heap.insert(5);
		heap.insert(3);
		heap.insert(4);
		
		assertEquals(3, heap.peekMin());
	}
	
	@Test(timeout=SECOND)
	public void testInsertandRemoveMinWithSameValueManyTimes() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 0; i < 100; i++) {
			heap.insert(1);
			heap.insert(3);
			heap.insert(2);
		}
		
		for (int i = 0; i < 100; i++) {
			assertEquals(1, heap.removeMin());
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(2, heap.removeMin());
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(3, heap.removeMin());
		}
	}
	
	@Test(timeout=SECOND)
	public void testPeekMinWithComparableObject() {
		IPriorityQueue<String> heap = this.makeInstance();
		
		heap.insert("a");
		heap.insert("banana");
		heap.insert("apple");
		heap.insert("b");
		heap.insert("ape");
		heap.insert("a");
		heap.insert("bad");
		
		for (int i = 0; i < heap.size(); i++) {
			assertEquals(heap.peekMin(), heap.removeMin());
		}
	}
	
	@Test(timeout=SECOND)
	public void testPeekMinEmptyException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		try {
			heap.peekMin();
			fail("should throw EmptyContainerException");
		} catch (EmptyContainerException e) {
			 // This is ok: do nothing
		}
	}
	
	@Test(timeout=SECOND)
	public void testIsEmpty() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		assertTrue(heap.isEmpty());
		heap.insert(1);
		assertFalse(heap.isEmpty());
		heap.removeMin();
		assertTrue(heap.isEmpty());
	}
}
