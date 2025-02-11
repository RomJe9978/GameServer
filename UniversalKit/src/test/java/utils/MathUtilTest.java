package utils;

import com.romje.utils.MathUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class MathUtilTest {

    /**
     * {@link MathUtil#inRange(long, long, long)}
     */
    @Test
    public void testIsInRange() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        assertTrue(MathUtil.inRange(min, min, min));
        assertTrue(MathUtil.inRange(min, min, max));
        assertFalse(MathUtil.inRange(min, max, min));
        assertFalse(MathUtil.inRange(min, max, max));

        assertFalse(MathUtil.inRange(max, min, min));
        assertTrue(MathUtil.inRange(max, min, max));
        assertFalse(MathUtil.inRange(max, max, min));
        assertTrue(MathUtil.inRange(max, max, max));

        assertFalse(MathUtil.inRange(0, min, min));
        assertTrue(MathUtil.inRange(0, min, max));
        assertFalse(MathUtil.inRange(0, max, min));
        assertFalse(MathUtil.inRange(0, max, max));
    }

    /**
     * {@link MathUtil#addToLong(int, int)}
     * {@link MathUtil#subtractToLong(int, int)}
     * {@link MathUtil#multiplyToLong(int, int)}
     */
    @Test
    public void testToLong() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        assertEquals((long) min + (long) min, MathUtil.addToLong(min, min));
        assertEquals((long) min + (long) max, MathUtil.addToLong(min, max));
        assertEquals((long) max + (long) min, MathUtil.addToLong(max, min));
        assertEquals((long) max + (long) max, MathUtil.addToLong(max, max));

        assertEquals(0, MathUtil.subtractToLong(min, min));
        assertEquals(0, MathUtil.subtractToLong(max, max));
        assertEquals((long) min - (long) max, MathUtil.subtractToLong(min, max));
        assertEquals((long) max - (long) min, MathUtil.subtractToLong(max, min));

        assertEquals((long) min * (long) min, MathUtil.multiplyToLong(min, min));
        assertEquals((long) min * (long) max, MathUtil.multiplyToLong(min, max));
        assertEquals((long) max * (long) max, MathUtil.multiplyToLong(max, max));
    }

    /**
     * {@link MathUtil#diffInt(int, int)}
     * {@link MathUtil#diffLong(long, long)}
     */
    @Test
    public void testDiff() {
        int minInt = Integer.MIN_VALUE;
        int maxInt = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> MathUtil.diffInt(minInt, maxInt));
        assertThrows(ArithmeticException.class, () -> MathUtil.diffInt(maxInt, minInt));
        assertEquals(5, MathUtil.diffInt(-1, 4));
        assertEquals(5, MathUtil.diffInt(4, -1));

        long minLong = Long.MIN_VALUE;
        long maxLong = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> MathUtil.diffLong(minLong, maxLong));
        assertThrows(ArithmeticException.class, () -> MathUtil.diffLong(maxLong, minLong));
        assertEquals(5, MathUtil.diffLong(-1, 4));
        assertEquals(5, MathUtil.diffLong(4, -1));
    }

    /**
     * {@link MathUtil#equalsFloat(float, float)}
     * {@link MathUtil#equalsFloat(float, float, float)}
     */
    @Test
    public void testEqualsFloat() {
        assertFalse(MathUtil.equalsFloat(0.0001f, -0.0001f));
        assertFalse(MathUtil.equalsFloat(0.0001f, 0.0f));
        assertFalse(MathUtil.equalsFloat(-0.0001f, 0.0f));

        assertFalse(MathUtil.equalsFloat(0.0001f, -0.0001f, 0.0001f));
        assertTrue(MathUtil.equalsFloat(0.0001f, -0.0001f, 0.0002f));
        assertTrue(MathUtil.equalsFloat(0.0001f, 0.0f, 0.0002f));
        assertTrue(MathUtil.equalsFloat(-0.0001f, 0.0f, 0.0002f));
    }

    /**
     * {@link MathUtil#isPowerOf2(int)}
     */
    @Test
    public void testIsPowerOfTwo() {
        assertFalse(MathUtil.isPowerOf2(0));
        assertFalse(MathUtil.isPowerOf2(-1));
        assertFalse(MathUtil.isPowerOf2(-2));
        assertFalse(MathUtil.isPowerOf2(-9));
        assertFalse(MathUtil.isPowerOf2(Integer.MIN_VALUE));
        assertFalse(MathUtil.isPowerOf2(Integer.MIN_VALUE + 1));

        assertTrue(MathUtil.isPowerOf2(1));
        assertTrue(MathUtil.isPowerOf2(2));
        assertFalse(MathUtil.isPowerOf2(9));
        assertFalse(MathUtil.isPowerOf2(Integer.MAX_VALUE));
        assertFalse(MathUtil.isPowerOf2(Integer.MAX_VALUE - 1));
        assertTrue(MathUtil.isPowerOf2(1 << 30));
    }

    /**
     * {@link MathUtil#nextPowerOf2(int)}
     */
    @Test
    public void testNextPowerOf2() {
        assertEquals(1, MathUtil.nextPowerOf2(Integer.MIN_VALUE));
        assertEquals(1, MathUtil.nextPowerOf2(Integer.MIN_VALUE >> 1));
        assertEquals(1, MathUtil.nextPowerOf2(Integer.MIN_VALUE >> 2));
        assertEquals(1, MathUtil.nextPowerOf2(Integer.MIN_VALUE >> 3));
        assertEquals(1, MathUtil.nextPowerOf2(-2));
        assertEquals(1, MathUtil.nextPowerOf2(-1));
        assertEquals(1, MathUtil.nextPowerOf2(0));

        assertEquals(1, MathUtil.nextPowerOf2(1));
        assertEquals(2, MathUtil.nextPowerOf2(2));
        assertEquals(4, MathUtil.nextPowerOf2(3));
        assertEquals(4, MathUtil.nextPowerOf2(4));
        assertEquals(8, MathUtil.nextPowerOf2(5));
        assertEquals(1 << 30, MathUtil.nextPowerOf2((1 << 30) - 1));
        assertEquals(1 << 30, MathUtil.nextPowerOf2((1 << 30) - 2));
        assertEquals(1 << 30, MathUtil.nextPowerOf2((1 << 30) - 128));

        assertEquals(1 << 30, MathUtil.nextPowerOf2(1 << 30));
        assertThrows(ArithmeticException.class, () -> MathUtil.nextPowerOf2((1 << 30) + 1));
        assertThrows(ArithmeticException.class, () -> MathUtil.nextPowerOf2((1 << 30) + 5));
        assertThrows(ArithmeticException.class, () -> MathUtil.nextPowerOf2(Integer.MAX_VALUE));
    }
}
