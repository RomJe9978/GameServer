package utils;

import com.romje.utils.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
