package com.romje.utils;

import java.util.*;

/**
 * This util is used to check if an object is empty or not.
 * Empty means {@code null} or {@code isEmpty()}.
 *
 * @author liuxuanjie
 */
public class EmptyUtil {
    /**
     * @return Returns {@code true} if the integer array is {@code null} or {@code isEmpty()}.
     */
    public static boolean isEmpty(int[] array) {
        return Objects.isNull(array) || array.length == 0;
    }

    /**
     * @return  Returns {@code true} if the integer array is not {@code null} and not {@code isEmpty()}.
     */
    public static boolean nonEmpty(int[] array) {
        return Objects.nonNull(array) && array.length > 0;
    }

    /**
     * @return Returns {@code true} if the long array is {@code null} or {@code isEmpty()}.
     */
    public static boolean isEmpty(long[] array) {
        return Objects.isNull(array) || array.length == 0;
    }

    /**
     * @return  Returns {@code true} if the long array is not {@code null} and not {@code isEmpty()}.
     */
    public static boolean nonEmpty(long[] array) {
        return Objects.nonNull(array) && array.length > 0;
    }

    /**
     * @return Returns {@code true} if the string is {@code null} or {@code isEmpty()}.
     */
    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    /**
     * @return  Returns {@code true} if the string is not {@code null} and not {@code isEmpty()}.
     */
    public static boolean nonEmpty(String str) {
        return Objects.nonNull(str) && !str.isEmpty();
    }

    /**
     * @return  @return Returns {@code true} if the collection is {@code null} or {@code isEmpty()}.
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    /**
     * @return  Returns {@code true} if the collection is not {@code null} and not {@code isEmpty()}.
     */
    public static <T> boolean nonEmpty(Collection<T> collection) {
        return Objects.nonNull(collection) && !collection.isEmpty();
    }

    /**
     * @return  Returns {@code true} if the map is {@code null} or {@code isEmpty()}.
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    /**
     * @return  Returns {@code true} if the map is not {@code null} and not {@code isEmpty()}.
     */
    public static <K, V> boolean nonEmpty(Map<K, V> map) {
        return Objects.nonNull(map) && !map.isEmpty();
    }
}
