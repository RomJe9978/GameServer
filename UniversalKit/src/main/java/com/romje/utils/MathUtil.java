package com.romje.utils;

/**
 * This util is used to handle math calculation.
 *
 * @author liu xuan jie
 */
public final class MathUtil {

    private MathUtil() {
    }

    /**
     * 指定{@code value}是否在指定范围[min, max]内
     *
     * @param min 左边界(包含)
     * @param max 右边界(包含)
     * @return Returns true if {@code min <= value <= max}
     */
    public static boolean inRange(long value, long min, long max) {
        return (min <= value) && (value <= max);
    }

    /**
     * 两个{@code int}值相加，返回{@code long}，防止数据越界
     */
    public static long addToLong(int first, int second) {
        return ((long) first) + second;
    }

    /**
     * 两个{@code int}值相减，返回{@code long}，防止数据越界
     */
    public static long subtractToLong(int first, int second) {
        return ((long) first) - second;
    }

    /**
     * 两个{@code int}值相乘，返回{@code long}，防止数据越界
     */
    public static long multiplyToLong(int first, int second) {
        return ((long) first) * second;
    }

    /**
     * 返回两个给定值的“差值”的“绝对值”
     * <p>计算差值时会直接进行“越界”检查，失败抛出异常
     * <p>想要最大可能的避免越界异常，使用{@link #diffLong(long, long)}
     *
     * @throws ArithmeticException 差值越界会直接抛异常
     */
    public static int diffInt(int value1, int value2) {
        int subResult = Math.subtractExact(value1, value2);
        return Math.abs(subResult);
    }

    /**
     * 返回两个给定值的“差值”的“绝对值”
     * <p>计算差值时会直接进行“越界”检查，失败抛出异常
     *
     * @throws ArithmeticException 差值越界会直接抛异常
     */
    public static long diffLong(long value1, long value2) {
        long subResult = Math.subtractExact(value1, value2);
        return Math.abs(subResult);
    }

    /**
     * 判断两个{@code float}类型的数值是否“相等”
     * <p>由于“JAVA浮点数”本身的特性和精度问题，需要容忍一些误差
     */
    public static boolean equalsFloat(float value1, float value2) {
        return Float.compare(value1, value2) == 0;
    }

    /**
     * 自定义误差，判断两个{@code float}类型的数值是否“相等”
     *
     * @param mistake 自定义的相对误差值(包含)
     * @return 只要两个数的差值绝对值在设置的误差值之内(包括)，返回{@code true}
     */
    public static boolean equalsFloat(float value1, float value2, float mistake) {
        return Math.abs(value1 - value2) <= mistake;
    }

    /**
     * 指定{@code value}是否是2的次幂
     *
     * @param value 正数才有计算的必要，“非正数”一定不是
     */
    public static boolean isPowerOf2(int value) {
        return (value > 0) && ((value & (value - 1)) == 0);
    }

    /**
     * 计算大于等于给定值的最小2的幂次方数
     *
     * <p>如果给定值本身就是2的次幂，直接返回原值
     * <p>如果给定值是“非正数”，则返回固定结果{@code 1}
     * <p>当计算结果超出整型的最大范围时，会抛出数据异常
     * <pre>
     *     input: -1, 0, 1, 2, 3, 7, 8, 9
     *     output: 1, 1, 1, 2, 4, 8, 8, 16
     * </pre>
     *
     * @param value 当大于"2^30"时，会有越界发生
     * @return 最接近且不小于给定值的2的次幂
     * @throws ArithmeticException 结果超出“整型范围”
     */
    public static int nextPowerOf2(int value) {
        if (value <= 0) {
            return 1;
        }

        int bound = 1 << 30;
        if (value > bound) {
            throw new ArithmeticException("Next power of two exceeds integer range.");
        }

        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return ++value;
    }
}
