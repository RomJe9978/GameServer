package com.romje.utils;

import java.util.*;

/**
 * 数组操作的工具类
 *
 * @author liu xuan jie
 */
public class ArrayUtil {

    /**
     * 获取Array中指定位置的元素
     *
     * <p>包装该方法的用意是为了避免调用处直接使用{@code array[index]}可能出现的“数组越界异常”，
     * 统一“数组越界”检查，以返回值{@code null}代替{@code ArrayIndexOutOfBoundsException}
     *
     * @param array 如果为{@code null}或者长度为{@code 0},则直接返回{@code null}
     * @param index 指定位置索引，不在数组的范围内，则直接返回{@code null}
     * @return 注意当返回{@code null}时，也有可能是指定位置索引的元素的确就是{@code null}
     */
    public static <T> T getValid(T[] array, int index) {
        if (Objects.isNull(array) || array.length == 0) {
            return null;
        }
        return (index < 0 || index >= array.length) ? null : array[index];
    }

    /**
     * 将{@code byte[]}转换成等价的{@code List<Byte>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Byte> asList(final byte[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Byte> resultList = new ArrayList<>(array.length);
        for (byte value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code short[]}转换成等价的{@code List<Short>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Short> asList(final short[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Short> resultList = new ArrayList<>(array.length);
        for (short value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code char[]}转换成等价的{@code List<Character>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Character> asList(final char[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Character> resultList = new ArrayList<>(array.length);
        for (char value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code boolean[]}转换成等价的{@code List<Boolean>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Boolean> asList(final boolean[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Boolean> resultList = new ArrayList<>(array.length);
        for (boolean value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code int[]}转换成等价的{@code List<Integer>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Integer> asList(final int[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Integer> resultList = new ArrayList<>(array.length);
        for (int value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code long[]}转换成等价的{@code List<Long>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Long> asList(final long[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Long> resultList = new ArrayList<>(array.length);
        for (long value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code float[]}转换成等价的{@code List<Float>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Float> asList(final float[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Float> resultList = new ArrayList<>(array.length);
        for (float value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 将{@code double[]}转换成等价的{@code List<Double>}
     *
     * @param array 原始基本类型数组
     * @return 没有元素会返回{@code emptyList()},不会返回{@code null}
     */
    public static List<Double> asList(final double[] array) {
        if (EmptyUtil.isEmpty(array)) {
            return Collections.emptyList();
        }

        List<Double> resultList = new ArrayList<>(array.length);
        for (double value : array) {
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final byte[] array, byte target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (byte temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final byte[] array, byte target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final short[] array, short target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (short temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final short[] array, short target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final char[] array, char target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (char temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final char[] array, char target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final boolean[] array, boolean target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (boolean temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final boolean[] array, boolean target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final int[] array, int target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (int temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final int[] array, int target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final float[] array, float target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (float temp : array) {
                if (Float.compare(temp, target) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final float[] array, float target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final long[] array, long target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (long temp : array) {
                if (temp == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final long[] array, long target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定基本类型数组是否“包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 被检查的对应类型的值
     * @return 当数组包含指定值的时候，返回{@code true}
     */
    public static <T> boolean isContains(final double[] array, double target) {
        if (EmptyUtil.nonEmpty(array)) {
            for (double temp : array) {
                if (Double.compare(temp, target) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定基本类型数组是否“不包含”对应类型的指定值
     *
     * @param array  如果为{@code null}，则直接返回{@code true}
     * @param target 被检查的对应类型的值
     * @return 当数组不包含指定值的时候，返回{@code true}
     */
    public static <T> boolean nonContains(final double[] array, double target) {
        return Objects.isNull(array) || !isContains(array, target);
    }

    /**
     * 指定数组是否“包含”指定元素
     * <pre>
     *     “包含”的准则是：
     *     1.target为null，且数组内有任意元素A也为null
     *     2.target不为null，且数组内有任意元素A满足{@code target.equals(A)}
     * </pre>
     *
     * @param array  如果为{@code null}，则直接返回{@code false}
     * @param target 可以为{@code null}
     * @return 当集合包含指定元素的时候，返回{@code true}
     */
    public static <T> boolean isContains(final T[] array, T target) {
        if (Objects.isNull(array)) {
            return false;
        }

        boolean nonNull = Objects.nonNull(target);
        for (T element : array) {
            if (nonNull) {
                if (target.equals(element)) {
                    return true;
                }
            } else if (Objects.isNull(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定数组是否“不包含”指定元素
     * <p>与{@link #isContains(Object[], Object)}相反
     */
    public static <T> boolean nonContains(final T[] array, T target) {
        return Objects.isNull(array) || !isContains(array, target);
    }
}
