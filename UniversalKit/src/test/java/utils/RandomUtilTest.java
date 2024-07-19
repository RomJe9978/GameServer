package utils;

import com.romje.utils.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class RandomUtilTest {

    /**
     * {@link RandomUtil#selectUnique(List, int, boolean)}
     */
    @Test
    public void testSelectUnique() {
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, Integer.MIN_VALUE, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, -1, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, 0, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, 1, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, Integer.MAX_VALUE, false));

        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, Integer.MIN_VALUE, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, -1, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, 0, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, 1, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(null, Integer.MAX_VALUE, true));

        ArrayList<?> emptyList = new ArrayList<>();
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, Integer.MIN_VALUE, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, -1, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, 0, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, 1, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, Integer.MAX_VALUE, false));

        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, Integer.MIN_VALUE, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, -1, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, 0, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, 1, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(emptyList, Integer.MAX_VALUE, true));

        List<Integer> singleList = Collections.singletonList(1);
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, Integer.MIN_VALUE, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, -1, false));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, 0, false));
        assertEquals(singleList.toString(), RandomUtil.selectUnique(singleList, 1, false).toString());
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, Integer.MAX_VALUE, false));

        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, Integer.MIN_VALUE, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, -1, true));
        assertEquals(Collections.emptyList(), RandomUtil.selectUnique(singleList, 0, true));
        assertEquals(singleList.toString(), RandomUtil.selectUnique(singleList, 1, true).toString());
        assertEquals(singleList.toString(), RandomUtil.selectUnique(singleList, Integer.MAX_VALUE, true).toString());

        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), Integer.MIN_VALUE, false), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), -1, false), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 0, false), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 1, false), 1);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 3, false), 3);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 5, false), 5);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), Integer.MAX_VALUE, false), 0);

        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), Integer.MIN_VALUE, true), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), -1, true), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 0, true), 0);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 1, true), 1);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 3, true), 3);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), 5, true), 5);
        checkSizeAndUnique(RandomUtil.selectUnique(Arrays.asList(1, 4, 2, 3, 5), Integer.MAX_VALUE, true), 5);
    }

    public <T> void checkSizeAndUnique(List<T> result, int checkSize) {
        assertNotEquals(null, result);
        assertEquals(checkSize, result.size());

        Set<T> existSet = new HashSet<>(result.size());
        result.forEach(value -> {
            assertFalse(existSet.contains(value));
            existSet.add(value);
        });
    }

    /**
     * {@link RandomUtil#randomWeight(List)}
     */
    @Test
    public void testRandomWeight() {
        assertEquals(-1, RandomUtil.randomWeight(null));
        assertEquals(-1, RandomUtil.randomWeight(Collections.emptyList()));

        assertEquals(0, RandomUtil.randomWeight(List.of(Integer.MIN_VALUE)));
        assertEquals(0, RandomUtil.randomWeight(List.of(-1)));
        assertEquals(0, RandomUtil.randomWeight(List.of(0)));
        assertEquals(0, RandomUtil.randomWeight(List.of(1)));
        assertEquals(0, RandomUtil.randomWeight(List.of(Integer.MAX_VALUE)));

        assertEquals(0, RandomUtil.randomWeight(Arrays.asList(1, Integer.MIN_VALUE, 0, -1)));
        assertEquals(1, RandomUtil.randomWeight(Arrays.asList(Integer.MIN_VALUE, 1, 0, -1)));
        assertEquals(2, RandomUtil.randomWeight(Arrays.asList(Integer.MIN_VALUE, 0, 1, -1)));
        assertEquals(3, RandomUtil.randomWeight(Arrays.asList(Integer.MIN_VALUE, -1, 0, 1)));

        checkBound(RandomUtil.randomWeight(Arrays.asList(10, 20, 10, 20)), 0, 3, null);
        checkBound(RandomUtil.randomWeight(Arrays.asList(-1, 20, -1, 20)), 0, 3, Arrays.asList(0, 2));
        checkBound(RandomUtil.randomWeight(Arrays.asList(-1, 20, 10, -1)), 0, 3, Arrays.asList(0, 3));
        checkBound(RandomUtil.randomWeight(Arrays.asList(10, -1, -1, 20)), 0, 3, Arrays.asList(1, 2));
        checkBound(RandomUtil.randomWeight(Arrays.asList(10, -1, 20, -1)), 0, 3, Arrays.asList(1, 3));
        checkBound(RandomUtil.randomWeight(Arrays.asList(-1, -1, 20, 20)), 0, 3, Arrays.asList(0, 1));
    }

    public void checkBound(int value, int min, int max, List<Integer> excludeList) {
        assertTrue(value >= min && value <= max);
        if (Objects.nonNull(excludeList)) {
            assertFalse(excludeList.contains(value));
        }
    }
}
