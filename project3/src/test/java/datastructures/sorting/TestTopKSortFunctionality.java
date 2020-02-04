package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
* See spec for details on what kinds of tests this class should include.
*/
public class TestTopKSortFunctionality extends BaseTest {
	
	/**
	* This test will check if a list contains exactly the same elements as
	* the "expected" array. See the tests you were provided for example
	* usage.
	*
	* Please do not modify this method: our private tests rely on this.
	*/
	protected <T> void assertListMatches(T[] expected, IList<T> actual) {
		assertEquals(expected.length, actual.size());
		assertEquals(expected.length == 0, actual.isEmpty());
		
		for (int i = 0; i < expected.length; i++) {
			try {
				assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
			} catch (Exception ex) {
				String errorMessage = String.format(
				"Got %s when getting item at index %d (expected '%s')",
				ex.getClass().getSimpleName(),
				i,
				expected[i]);
				throw new AssertionError(errorMessage, ex);
			}
		}
	}
	
	@Test(timeout=SECOND)
	public void testSimpleUsage() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		
		IList<Integer> top = Searcher.topKSort(5, list);
		assertEquals(5, top.size());
		for (int i = 0; i < top.size(); i++) {
			assertEquals(15 + i, top.get(i));
		}
	}
	
	@Test(timeout=SECOND)
	public void testBasicTopSize() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		
		IList<Integer> top = Searcher.topKSort(10, list);
		assertEquals(10, top.size());
	}
	
	@Test(timeout=SECOND)
	public void testComparableObject() {
		IList<String> list = new DoubleLinkedList<>();
		list.add("a");
		list.add("b");
		list.add("apple");
		list.add("ape");
		list.add("banana");
		list.add("bad");
		list.add("a");
		
		IList<String> top = Searcher.topKSort(7, list);
		assertEquals(7, top.size());
		this.assertListMatches(new String[] {"a", "a", "ape", "apple",
		"b", "bad", "banana"}, top);
	}
	
	@Test(timeout=SECOND)
	public void testSizeSmallerThanK() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		
		IList<Integer> top = Searcher.topKSort(25, list);
		assertEquals(20, top.size());
		for (int i = 0; i < top.size(); i++) {
			assertEquals(i, top.get(i));
		}
	}
	
	@Test(timeout=SECOND)
	public void testIllegalStateException() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		
		try {
			IList<Integer> top = Searcher.topKSort(-1, list);
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			 // This is ok: do nothing
		}
	}
	
	
	@Test(timeout=SECOND)
	public void testWhenKEqualsZero() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		
		IList<Integer> top = Searcher.topKSort(0, list);
		assertEquals(0, top.size());
		this.assertListMatches(new Integer[] {}, top);
	}
}
