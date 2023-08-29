package com.romje.utils;

import java.util.*;

/**
 * “随机数”工具
 * <p>该类中的随机接口默认均为“伪随机数”，且“种子”是全局共享的，以
 * 系统时间作为“初始种子”。
 * <p>该类中的算法为“线性同余算法”。JDK源生随机函数也是基于该算法，
 * 该算法只是大多数情况下提供足够的随机性，具有可控的初始状态和周期性
 * 等特点，其在某些情况下表现出可预测的状态，并且周期有限。
 * <p>综上所述，在全局共享“种子”的情况下，游戏场景基本适用。
 *
 * @author RomJe
 */
public class RandomUtil {
    private static final int A = 48271;
    private static final int M = 2147483647;
    private static final int Q = M / A;
    private static final int R = M % A;
    private static int STATE = -1;
    private static final Random RANDOM_INSTANCE = new Random(System.currentTimeMillis());

    public static int randomInt() {
        if (STATE < 0) {
            STATE = RANDOM_INSTANCE.nextInt();
        }

        int tmpState = A * (STATE % Q) - R * (STATE / Q);
        if (tmpState >= 0) {
            STATE = tmpState;
        } else {
            STATE = tmpState + M;
        }
        return STATE;
    }

    /**
     * 指定右边界，随机整数{@code 0 <= result <= max}
     *
     * @param max 包含该值，负数为非法数值
     * @return Returns 0 if invalid param.
     */
    public static int randomInt(int max) {
        return max >= 0 ? randomInt() % (max + 1) : 0;
    }

    /**
     * 指定右边界，随机整数{@code 0 <= result < max}
     *
     * @param max 不包含该值，只有正数为有效参数
     * @return Returns 0 if invalid param.
     */
    public static int randomIntWithout(int max) {
        return max > 0 ? randomInt() % max : 0;
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
     * 从List中随机选出指定个数的无重复元素
     * <p>随机思路为”扑克牌算法“，保证元素不被重复随机
     * <p>随机元素不重复，每一个元素只会被随机一次
     * <p>当List的长度不满足count时候，不进行随机
     * <p>随机后的返回结果不保证和原列表的顺序一致
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
        for (int index = size - 1; index >= size - count; index--) {
            int randomIndex = randomInt(index);
            Collections.swap(tempList, index, randomIndex);
        }

        return tempList.subList(size - count, size);
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
     * 从指定的“概率列表”中随机命中一个概率
     * <p>仅保证“概率和”在{@link Integer#MAX_VALUE}范围内的情况
     * <p>所有概率取和后随机数，然后看随机数命中哪一个概率
     * <p>注意，如果概率列表所有值全为“非正数”，那么认为所有概率全是0
     * 此时，会直接按照等概率随机。如有特殊需求，不要调用该接口
     *
     * @param weightList 概率列表，允许概率为“非正数”，所有“非正数”概率按照“0”处理
     * @return 正常返回命中概率在list中的的index。如果参数为Empty，返回-1，代表无效随机
     */
    public static int randomWeight(List<Integer> weightList) {
        if (Objects.isNull(weightList) || weightList.isEmpty()) {
            return -1;
        }

        // 不考虑累加越界
        int sum = 0;
        for (int i = 0, iSize = weightList.size(); i < iSize; i++) {
            sum += Math.max(weightList.get(i), 0);
        }

        int randomValue = randomInt(1, sum);
        for (int i = 0, iSize = weightList.size(); i < iSize; i++) {
            int weight = weightList.get(i);
            if (weight <= 0) {
                continue;
            }

            randomValue -= weight;
            if (randomValue <= 0) {
                return i;
            }
        }
        // 正常不会随不到，除非全是“非正数”，此时认为全是0，按概率一致处理
        return randomIntWithout(weightList.size());
    }
}
