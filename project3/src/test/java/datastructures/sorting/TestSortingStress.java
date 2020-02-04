package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
	
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
	
    
    @Test(timeout=10*SECOND)
    public void testHeapStress() {
    	 	IPriorityQueue<Integer> heap = this.makeInstance();
    	 	int limit = 1000000;
    	 	for (int i = 0; i < limit; i++) {
    	 		assertEquals(i, heap.size());
    	 		heap.insert(i);
    	 	}
    	 	
    	 	for (int i = 0; i < limit; i++) {
    	 		assertEquals(limit - i, heap.size());
    	 		assertEquals(i, heap.removeMin());
    	 	}
    }
    
    @Test(timeout=10*SECOND)
    public void testSortingStress() {
    	 	IList<Integer> list = new DoubleLinkedList<>();
    	 	int limit = 1000000;
         for (int i = 0; i < limit; i++) {
             list.add(i);
         }
        
         IList<Integer> top = Searcher.topKSort(limit, list);   
         
         assertEquals(limit, top.size());
         int i = 0;
         for (Integer element : top) {
             assertEquals(i++, element);
         }
    }
    
}
