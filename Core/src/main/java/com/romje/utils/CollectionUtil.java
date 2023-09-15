package com.romje.utils;

import java.util.*;

/**
 * 集合相关工具类
 *
 * @author RomJe
 */
public class CollectionUtil {
    /**
     * 集合的长度是否等于指定值
     *
     * @param collection 允许为{@code null},此时逻辑上等价于长度为0
     * @param checkSize  “负数”为“非法参数”，直接返回{@code false}
     * @return 当集合的长度恰好等于指定长度的时候，返回{@code true}
     */
    public static boolean sizeEqualTo(Collection<?> collection, int checkSize) {
        if (checkSize < 0) {
            return false;
        }
        return checkSize == (Objects.isNull(collection) ? 0 : collection.size());
    }

    /**
     * 集合的长度是否“大于或等于”给定的最小长度(注意，包含等于)
     *
     * @param collection   允许为{@code null},此时逻辑上等价于长度为0
     * @param checkMinSize “负数”为“有效参数”，直接返回{@code true}
     * @return 当集合的长度"大于等于"给定的长度时，返回{@code true}
     */
    public static <T> boolean sizeMin(Collection<T> collection, int checkMinSize) {
        if (checkMinSize <= 0) {
            return true;
        }

        if (Objects.isNull(collection) || collection.isEmpty()) {
            return false;
        }

        return collection.size() >= checkMinSize;
    }

    /**
     * 获取List中指定位置的元素
     *
     * <p>包装该方法的用意是为了避免调用处直接使用{@code list.get()}可能出现的“数组越界异常”，
     * 统一“数组越界”检查，以返回值{@code null}代替{@code ArrayIndexOutOfBoundsException}
     *
     * @param list  如果为{@code null}或者{@code isEmpty()},则直接返回{@code null}
     * @param index 指定位置索引，不在List的长度范围内，则直接返回{@code null}
     * @return 注意当返回{@code null}时，也有可能是指定位置索引的元素的确就是{@code null}
     */
    public static <T> T getValid(List<T> list, int index) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return null;
        }
        return (index < 0 || index >= list.size()) ? null : list.get(index);
    }

    /**
     * 指定集合是否“不包含”指定元素
     *
     * @param collections 如果为{@code null}，则直接返回{@code true}
     * @param target      对{@code null}的处理取决于具体的集合
     * @return 当集合不包含指定元素的时候，返回{@code true}
     */
    public static <T> boolean nonContains(Collection<T> collections, T target) {
        return Objects.isNull(collections) || !collections.contains(target);
    }

    /**
     * 指定Map是否“不包含”指定的Key
     *
     * @param map 如果为{@code null}，则直接返回{@code true}
     * @param key 对{@code null}的处理取决于具体的map
     * @return 当集合不包含指定元素的时候，返回{@code true}
     */
    public static <K, V> boolean nonContains(Map<K, V> map, K key) {
        return Objects.isNull(map) || !map.containsKey(key);
    }
}
