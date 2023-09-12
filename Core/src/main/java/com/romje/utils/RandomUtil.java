package com.romje.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * “随机数”工具
 *
 * <p>该类中的底层随机接口为{@link ThreadLocalRandom},线程安全。
 *
 * <p>该类中的算法为“线性同余算法”。JDK源生随机函数也是基于该算法，该算法只
 * 是大多数情况下提供足够的随机性，具有可控的初始状态和周期性等特点，其在某些
 * 情况下表现出可预测的状态，并且周期有限。总体而言，“线性同余”算法并不是高质
 * 量的随机算法，并且JDK也对该算法进行了优化和改进。
 *
 * <p>该类中的{@link #randomInt()}是所有随机方法的最原始随机方法，不同于
 * 源生JDK中的初始随机数，该方法限制了随机数为“非负数”。
 *
 * <p>该类中的所有类似{@code randomInt(int bound)}有边界随机的方法，都
 * 是在“随机数”的基础上直接对“bound”进行取模运算，这从严格意义上来说会丢失一
 * 部分公平性，当“随机边界值”不能被{@code Integer.maxValue}整除的时候，此
 * 时区间内的每一个值的概率并不完全相同，但是相差甚小。而JDK源生的“带边界随机”
 * 方法在随机上更加理想，并不是取模，而是采用循环随机，直到随机数落到边界范围内
 * 为止，保证了“公平性”，但是这些“循环”却是不可控的。
 *
 * <p>综上所述，结合游戏场景，绝大部分游戏业务对“随机数”的需求度远远不需要所谓
 * “绝对意义”上的“完全公平等概率不可控随机”，所以此处自定义了JDK的部分接口，组
 * 合了部分常用“随机方法”，以满足基本业务场景的使用。
 *
 * @author RomJe
 */
public class RandomUtil {
    private static final ThreadLocal<ThreadLocalRandom> RANDOM_INSTANCE = ThreadLocal.withInitial(ThreadLocalRandom::current);

    /**
     * 注意，该方法并不是绝对意义上的"等概率随机"，其中随机到
     * Integer.MaxValue的概率要比其他值小“2^32分之1”。由
     * 于本身随机算法就并不是高质量绝对随机，这部分偏差忽略。
     *
     * @return Returns {@code 0 <= result <= Integer.MaxValue}
     */
    protected static int randomInt() {
        int value = RANDOM_INSTANCE.get().nextInt();
        return value == Integer.MIN_VALUE ? 0 : Math.abs(value);
    }

    /**
     * 注意，该方法并不是绝对意义上的"等概率随机"，其中随机到
     * Long.MaxValue的概率要比其他值小“2^64分之1”。由于本
     * 身随机算法就并不是高质量绝对随机，这部分偏差忽略。
     *
     * @return Returns {@code 0 <= result <= Long.MaxValue}
     */
    protected static long randomLong() {
        long value = RANDOM_INSTANCE.get().nextLong();
        return value == Long.MIN_VALUE ? 0L : Math.abs(value);
    }

    /**
     * 指定边界(包含)，随机整数
     *
     * @param bound 负数为非法参数
     * @return Returns {@code 0 <= result <= max}.
     * @throws IllegalArgumentException 当参数不合法的时候
     */
    public static int randomInt(int bound) {
        if (bound < 0) {
            throw new IllegalArgumentException("Bad bound");
        }
        return randomInt() % (bound + 1);
    }

    /**
     * 指定边界(包含)，随机整数
     *
     * @param bound 负数为非法参数
     * @return Returns {@code 0 <= result <= max}.
     * @throws IllegalArgumentException 当参数不合法的时候
     */
    public static long randomLong(long bound) {
        if (bound < 0) {
            throw new IllegalArgumentException("Invalid param is" + bound);
        }
        return randomLong() % (bound + 1);
    }

    /**
     * 指定边界(不包含)，随机整数
     *
     * @param bound 只有正数才会被处理
     * @return Returns {@code 0 <= result < max}.
     * @throws IllegalArgumentException 当参数不合法的时候
     */
    public static int randomIntWithout(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Invalid param is" + bound);
        }
        return randomInt() % bound;
    }

    /**
     * 指定边界(不包含)，随机整数
     *
     * @param bound 只有正数才会被处理
     * @return Returns {@code 0 <= result < max}.
     * @throws IllegalArgumentException 当参数不合法的时候
     */
    public static long randomLongWithout(long bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Invalid param is" + bound);
        }
        return randomLong() % bound;
    }

    /**
     * 指定左右边界，随机整数{@code left <= result <= right}
     * <p>两个参数中小的为左边界，大的为右边界
     *
     * @param side1 包含边界，可以为任何整数
     * @param side2 包含边界，可以为任何整数
     */
    public static int randomInt(int side1, int side2) {
        int diff = Math.abs(side1 - side2);
        return randomInt(diff) + Math.min(side1, side2);
    }

    /**
     * 指定左右边界，随机整数{@code left <= result <= right}
     * <p>两个参数中小的为左边界，大的为右边界
     *
     * @param side1 包含边界，可以为任何整数
     * @param side2 包含边界，可以为任何整数
     */
    public static long randomLong(long side1, long side2) {
        long diff = Math.abs(side1 - side2);
        return randomLong(diff) + Math.min(side1, side2);
    }

    /**
     * 是否命中概率
     * <p>例，总概率为100，目标概率40，则为百分之四十
     *
     * @param total  总概率。最终随机范围为[1,total]
     * @param target 目标概率。最终命中范围为[1,target]
     * @return 当total或者target为“非正数”时，直接返回false。
     */
    public static boolean hitProbability(int total, int target) {
        if (total <= 0 || target <= 0) {
            return false;
        }
        return randomInt(1, total) <= target;
    }

    /**
     * 从list中随机一个元素
     *
     * @return {@code list}没有元素时会返回{@code null}
     */
    public static <T> T randomList(List<T> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return null;
        }
        int index = randomIntWithout(list.size());
        return list.get(index);
    }

    /**
     * 从数组中随机一个元素
     *
     * @return 没有元素时会返回{@code null}
     */
    public static <T> T randomArray(T[] array) {
        if (Objects.isNull(array) || array.length == 0) {
            return null;
        }
        return array[randomIntWithout(array.length)];
    }

    /**
     * 从List中随机选出指定个数的无重复位置元素
     * <p>随机思路为”扑克牌算法“，随机元素不重复，每一个位置元素只会被随机一次。注意
     * 保证的是“位置”不重复，如果列表内本身就有{@code a.equals(b)==true},最后结
     * 果也有可能同时包含a和b（a和b的位置索引不同）。随机后的结果中的元素相对顺序不保
     * 证和原数组一致，即{@code a，b，c}中随机3个，结果可能为{@code c，a，b}。
     *
     * <p>当List的长度不满足count时候，不进行随机，交由调用处自己控制是否需要随机。
     * 例如：{@code selectUnique(list, Math.min(list.size(), count))}。
     *
     * @param list  待随机的列表。null和empty均无效
     * @param count 需要随机的元素个数。不在区间[1,list.size]内的输入均无效
     * @return Returns {@code Collections.emptyList()} when param invalid.
     */
    public static <T> List<T> selectUnique(List<T> list, int count) {
        if (Objects.isNull(list) || count <= 0 || list.size() < count) {
            return Collections.emptyList();
        }

        int size = list.size();
        List<T> tempList = new ArrayList<>(list);
        if (size == count) {
            return tempList;
        }

        for (int index = size - 1; index >= size - count; index--) {
            int randomIndex = randomInt(index);
            Collections.swap(tempList, index, randomIndex);
        }

        return tempList.subList(size - count, size);
    }

    /**
     * 从指定的“概率列表”中随机命中一个概率
     *
     * <p>仅保证“概率和”在{@link Integer#MAX_VALUE}范围内的情况。如果传入列表数据
     * 之和超过{@link Integer#MAX_VALUE}，则会抛出异常(游戏开发没有对应场景，忽略)。
     *
     * <p>注意，如果概率列表所有值全为“非正数”，那么认为所有概率全是0。此时，会直接按照
     * 等概率随机。如有特殊需求，不要调用该接口。
     *
     * @param weightList 概率列表，允许概率为“非正数”，所有“非正数”概率按照“0”处理
     * @return 正常返回命中概率在list中的的index，任何非法情况返回{@code -1}。
     * @throws ArithmeticException 当列表概率之和超过{@code Integer.MaxValue}。
     */
    public static int randomWeight(List<Integer> weightList) {
        if (Objects.isNull(weightList) || weightList.isEmpty()) {
            return -1;
        }

        long sum = 0L;
        for (int i = 0, iSize = weightList.size(); i < iSize; i++) {
            // 考虑累加越界,抛出异常
            sum = Math.addExact(sum, Math.max(weightList.get(i), 0));
        }

        // 全是“非正数”，此时认为全是0，按概率一致处理
        if (sum == 0) {
            return randomIntWithout(weightList.size());
        }

        // 概率逐级递减即可，注意边界
        int randomValue = randomIntWithout((int) sum);
        for (int i = 0, iSize = weightList.size(); i < iSize; i++) {
            int weight = weightList.get(i);
            if (weight <= 0) {
                continue;
            }

            randomValue -= weight;
            if (randomValue < 0) {
                return i;
            }
        }

        // 正常不会走到这一步的
        return -1;
    }
}
