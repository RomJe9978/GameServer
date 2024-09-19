package utils;

import com.romje.utils.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class CollectionUtilTest {

    /**
     * {@link CollectionUtil#nonContains(Collection, Object)}
     */
    @Test
    public void testNonContains() {
        Collection<Integer> testCollection = null;
        assertTrue(CollectionUtil.nonContains(testCollection, null));
        assertTrue(CollectionUtil.nonContains(testCollection, 2));

        testCollection = Collections.emptyList();
        assertTrue(CollectionUtil.nonContains(testCollection, null));
        assertTrue(CollectionUtil.nonContains(testCollection, 2));

        testCollection = Arrays.asList(2, 5);
        assertTrue(CollectionUtil.nonContains(testCollection, null));
        assertFalse(CollectionUtil.nonContains(testCollection, 2));
        assertTrue(CollectionUtil.nonContains(testCollection, 3));
        assertFalse(CollectionUtil.nonContains(testCollection, 5));

        testCollection = Arrays.asList(2, null, 5);
        assertFalse(CollectionUtil.nonContains(testCollection, null));
        assertFalse(CollectionUtil.nonContains(testCollection, 2));
        assertTrue(CollectionUtil.nonContains(testCollection, 3));
        assertFalse(CollectionUtil.nonContains(testCollection, 5));
    }

    /**
     * {@link CollectionUtil#insertWithCompare(List, Comparable)}
     */
    @Test
    public void testInsertByCompare() {
        List<Integer> sortedList = new ArrayList<>();
        assertThrows(NullPointerException.class, () -> CollectionUtil.insertWithCompare(null, 1));
        assertThrows(NullPointerException.class, () -> CollectionUtil.insertWithCompare(null, null));
        assertThrows(NullPointerException.class, () -> CollectionUtil.insertWithCompare(sortedList, null));

        // 向emptyList中插入
        CollectionUtil.insertWithCompare(sortedList, 1);
        assertEquals(1, sortedList.get(0));

        // 向list中插入一个最大的
        CollectionUtil.insertWithCompare(sortedList, 1000);
        assertEquals(1, sortedList.get(0));
        assertEquals(1000, sortedList.get(1));

        // 向list中插入一个最小的
        CollectionUtil.insertWithCompare(sortedList, -100);
        assertEquals(-100, sortedList.get(0));
        assertEquals(1, sortedList.get(1));
        assertEquals(1000, sortedList.get(2));

        // 以此插入有序的，{-200，200， 200， 35，-58}
        CollectionUtil.insertWithCompare(sortedList, -200);
        Integer int1 = 200;
        CollectionUtil.insertWithCompare(sortedList, int1);
        Integer int2 = 200;
        CollectionUtil.insertWithCompare(sortedList, int2);
        CollectionUtil.insertWithCompare(sortedList, 35);
        CollectionUtil.insertWithCompare(sortedList, -58);

        assertEquals(-200, sortedList.get(0));
        assertEquals(-100, sortedList.get(1));
        assertEquals(-58, sortedList.get(2));
        assertEquals(1, sortedList.get(3));
        assertEquals(35, sortedList.get(4));
        assertEquals(200, sortedList.get(5));
        assertEquals(200, sortedList.get(6));
        assertEquals(1000, sortedList.get(7));

        assertSame(sortedList.get(5), int1);
        assertNotSame(sortedList.get(5), int2);

        assertSame(sortedList.get(6), int2);
        assertNotSame(sortedList.get(6), int1);
    }
}
