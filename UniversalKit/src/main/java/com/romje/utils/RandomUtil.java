package com.romje.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
 * <p>该类中的所有类似{@code randomInt(int bound)}有边界随机的方法，都
 * 是在“随机数”的基础上直接对“bound”进行取模运算，这从严格意义上来说会丢失一
 * 部分公平性，当“随机边界值”不能被{@code Integer.maxValue}整除的时候，此
 * 时区间内的每一个值的概率并不完全相同，但是相差甚小。而JDK源生的“带边界随机”
 * 方法在随机上更加理想，并不是取模，而是采用循环随机，直到随机数落到边界范围内
 * 为止，保证了“公平性”，但是这些“循环”却是不可控的。
 *
 * <p>所有类似{@code randomInt(int bound)}的方法都默认“不包含边界”，类比
 * 的包含边界的方法统一命名为类似{@see randomIntClosed(int bound)}。
 *
 * <p>综上所述，结合游戏场景，绝大部分游戏业务对“随机数”的需求度远远不需要所谓
 * “绝对意义”上的“完全公平等概率不可控随机”，所以此处自定义了JDK的部分接口，组
 * 合了部分常用“随机方法”，以满足基本业务场景的使用。
 *
 * @author liu xuan jie
 */
public final class RandomUtil {

    private RandomUtil() {
    }

    /**
     * 随机{@code int}范围内的“非负数”
     *
     * @return Returns {@code 0 <= result <= Integer.MaxValue}
     */
    private static int randomIntNonNegative() {
        int value = ThreadLocalRandom.current().nextInt();
        return value == Integer.MIN_VALUE ? 0 : Math.abs(value);
    }

    /**
     * 指定边界(不包含)，随机整数
     *
     * @param bound 只有正数才会被处理
     * @return Returns {@code 0 <= result < bound}
     * @throws IllegalArgumentException 如果{@code bound}不是正数
     */
    public static int randomInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bad bound!");
        }
        return randomIntNonNegative() % bound;
    }

    /**
     * 指定边界(包含)，随机整数
     *
     * @param bound 只有"非负数"才会被处理
     * @return Returns {@code 0 <= result <= bound}
     * @throws IllegalArgumentException If {@code bound < 0}
     */
    public static int randomIntClosed(int bound) {
        return bound == Integer.MAX_VALUE ?
                randomIntNonNegative() : randomInt(bound + 1);
    }

    /**
     * 指定两个边界(都包含)，随机整数
     * <p>{@code int min = Math.min(bound1, bound2)}
     * <p>{@code int max = Math.max(bound1, bound2)}
     *
     * @param bound1 {@code abs(bound1 - bound2) <= Integer.MAX_VALUE}
     * @param bound2 {@code abs(bound1 - bound2) <= Integer.MAX_VALUE}
     * @return Returns {@code min <= result <= max}
     * @throws ArithmeticException If {@code max - min > Integer.MAX_VALUE}
     */
    public static int randomIntClosed(int bound1, int bound2) {
        if (bound1 == bound2) {
            return bound1;
        }
        int min = Math.min(bound1, bound2);
        int max = Math.max(bound1, bound2);
        int diff = Math.subtractExact(max, min);
        return randomIntClosed(diff) + min;
    }

    /**
     * 是否命中概率
     * <p>千分之三的概率的使用：{@code hitProbability(1000, 3)}
     *
     * @param total  总概率。最终随机范围为[0,total)，必须为正数
     * @param target 目标概率。最终命中范围为[0,target)，必须为正数
     * @return Returns {@code false} when param is not positive
     */
    public static boolean hitProbability(int total, int target) {
        if (total <= 0 || target <= 0) {
            return false;
        }
        return randomInt(total) < target;
    }

    /**
     * 从list中随机一个元素
     *
     * @return {@code list}没有元素时会返回{@code null}
     */
    public static <T> T randomList(List<T> list) {
        return EmptyUtil.isEmpty(list) ? null : list.get(randomInt(list.size()));
    }

    /**
     * 从数组中随机一个元素
     *
     * @return {@code array}没有元素时会返回{@code null}
     */
    public static <T> T randomArray(T[] array) {
        return EmptyUtil.isEmpty(array) ? null : array[randomInt(array.length)];
    }

    /**
     * 从List中随机选出指定个数的“不重复位置”元素
     *
     * <p>随机思路为”扑克牌算法“，随机元素不重复，每一个位置元素只会被随机一次。注意
     * 保证的是“位置”不重复，如果列表内本身就有{@code a.equals(b) == true},最后
     * 结果也有可能同时包含a和b（a和b的位置索引不同）。随机后的结果中的元素相对顺序不
     * 保证和原数组一致，即(a，b，c)中随机2个，结果可能为(c, a)
     *
     * <p>当{@code count > list.size()}，需要调用处设置{@code isAtLeast}来决
     * 定是否选择。当{@code count == list.size()}，会返回原List的一个浅拷贝List
     *
     * @param list      待随机的列表。没有元素的时候为无效输入
     * @param count     需要随机的元素个数。“负数”为无效输入
     * @param isAtLeast 当count > list.size()时，是否至少选择list.size()个元素
     *                  {@code false}表示“不需要”，此时认为count为无效参数
     *                  {@code true}表示”需要“，此时设置count = Math.min(count, list.size())
     * @return Returns {@code Collections.emptyList()} when param invalid.
     */
    public static <T> List<T> selectUnique(List<T> list, int count, boolean isAtLeast) {
        if (Objects.isNull(list) || list.isEmpty() || count <= 0) {
            return Collections.emptyList();
        }

        int size = list.size();
        if (size < count) {
            if (isAtLeast) {
                count = size;
            } else {
                return Collections.emptyList();
            }
        }

        List<T> tempList = new ArrayList<>(list);
        if (size == count) {
            return tempList;
        }

        for (int index = 0; index < count; index++) {
            int randomIndex = randomIntClosed(index, size - 1);
            if (randomIndex != index) {
                Collections.swap(tempList, index, randomIndex);
            }
        }

        return tempList.subList(0, count);
    }

    /**
     * 从指定的“概率列表”中随机命中一个概率
     *
     * <p>仅保证“概率和”在{@link Integer#MAX_VALUE}范围内的情况。如果传入列表数据
     * 之和超过{@link Integer#MAX_VALUE}，则会抛出异常(游戏开发没有对应场景，忽略)
     *
     * <p>注意，如果概率列表所有值全为“非正数”，那么认为所有概率全是0。此时，会直接按照
     * 等概率随机。如有特殊需求，不要调用该接口
     *
     * @param weightList 概率列表，允许概率为“非正数”，所有“非正数”概率按照“0”处理
     * @return 正常返回命中概率在list中的的index，任何非法情况返回{@code -1}
     * @throws ArithmeticException 当列表概率之和超过{@code Integer.MaxValue}
     */
    public static int randomWeight(List<Integer> weightList) {
        if (EmptyUtil.isEmpty(weightList)) {
            return -1;
        }

        long sum = 0L;
        for (Integer integer : weightList) {
            // 考虑累加越界,抛出异常
            sum = Math.addExact(sum, Math.max(integer, 0));
        }

        // 全是“非正数”，此时认为全是0，按概率一致处理
        if (sum == 0) {
            return randomInt(weightList.size());
        }

        // 概率逐级递减即可，注意随机数范围[0, sum)
        int randomValue = randomInt((int) sum);
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
