/**
 * 
 */
package com.nordic.coding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Sakthimurugan Elangovan
 *
 */
public class SynchronizedUnbalancedSnapshotListTest {
	
	private SnapshotList<String> snapShotList;
	
	@BeforeEach
	void configure(){
		snapShotList = new SynchronizedUnbalancedSnapshotList<>();
	}
	
	@Test
	@DisplayName("Basic object creation validation")
    void testCreateList_valid_ShouldPass() {	
        Assertions.assertNotNull(snapShotList);
        Assertions.assertEquals(0,snapShotList.size());
        Assertions.assertEquals(0,snapShotList.version());
        Assertions.assertEquals(1,snapShotList.snapshot());
    }
	
	@Test
	@DisplayName("Basic List Add operations validation")
    void testAddList_valid_ShouldPass() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        Assertions.assertEquals(2,snapShotList.size());
        Assertions.assertEquals(1,snapShotList.indexOf("Banana"));
        Assertions.assertEquals("Apple",snapShotList.get(0));   
    }
	
	@Test
	@DisplayName("Valid List Update operations")
    void testUpdateList_valid_ShouldPass() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.set(1, "Batman");
        Assertions.assertEquals("Batman",snapShotList.get(1));   
    }
	
	@Test
	@DisplayName("Invalid List Update operations")
    void testUpdateList_valid_ShouldThrowException() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        Assertions.assertThrows(IndexOutOfBoundsException.class,()->snapShotList.set(3, "Batman"));   
    }
	
	@Test
	@DisplayName("Basic List remove operations validation")
    void testRemoveList_valid_ShouldPass() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.remove(0);
        Assertions.assertEquals("Banana",snapShotList.get(0));
        Assertions.assertEquals(1,snapShotList.size());   
    }
	
	@Test
	@DisplayName("Invalid List remove operations")
    void testRemoveList_valid_ShouldThrowException() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        Assertions.assertThrows(IndexOutOfBoundsException.class,()->snapShotList.remove(4));
    }
	
	@Test
	@DisplayName("valid snaphsot creation")
    void testSnapshotList_valid_ShouldPass() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        Assertions.assertEquals(1, snapShotList.snapshot());
    }
	
	@Test
	@DisplayName("valid snaphsot elements validation")
    void testSnapshotListElements_valid_ShouldPass() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.snapshot();
        snapShotList.set(1, "Basils");
        snapShotList.snapshot();
        snapShotList.set(1, "Bamboo");
        Assertions.assertEquals(2,snapShotList.version());
        Assertions.assertEquals("Banana", snapShotList.getAtVersion(1, 1));
        Assertions.assertEquals("Basils", snapShotList.getAtVersion(1, 2));
        Assertions.assertEquals("Bamboo", snapShotList.get(1));
    }
	
	@Test
	@DisplayName("Invalid snaphsot version access")
    void testSnapshotListElements_Invalid_ShouldThrowException() {
        Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.snapshot();
        snapShotList.set(1, "Basils");
        snapShotList.snapshot();
        snapShotList.set(1, "Bamboo");
        Assertions.assertEquals(2,snapShotList.version());
        Assertions.assertThrows(NullPointerException.class, () -> snapShotList.getAtVersion(1, 3));
    }
	
	@Test
	@DisplayName("valid snaphsot drop operation")
	void testSnapshotDrop_Valid_ShouldPass(){
		Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.snapshot();
        snapShotList.set(1, "Basils");
        snapShotList.snapshot();
        snapShotList.set(1, "Bamboo");
        snapShotList.snapshot();
        snapShotList.snapshot();
        snapShotList.snapshot();
        snapShotList.snapshot();
        Assertions.assertEquals(6,snapShotList.version());
        snapShotList.dropPriorSnapshots(6);
	}
	
	@Test
	@DisplayName("Invalid snaphsot drop operation")
	void testSnapshotDrop_InValid_ShouldPass(){
		Assertions.assertNotNull(snapShotList);
        snapShotList.add("Apple");
        snapShotList.add("Banana");
        snapShotList.snapshot();
        snapShotList.set(1, "Basils");
        snapShotList.snapshot();
        snapShotList.set(1, "Bamboo");
        snapShotList.snapshot();
        Assertions.assertEquals(3,snapShotList.version());
	}
}
