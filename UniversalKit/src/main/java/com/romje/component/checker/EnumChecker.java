package com.romje.component.checker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 枚举检查器
 *
 * @author liu xuan jie
 */
public class EnumChecker {

    /**
     * 检查给定枚举类中是否存在指定字段重复的情况。
     *
     * @param clazz     枚举类的Class对象，用于反射获取枚举值。
     * @param fieldName 需要检查重复性的字段名。
     * @return {@link CheckResult}，如果存在重复，则返回失败结果，包含重复信息；否则返回成功结果。
     * @throws NoSuchFieldException      如果指定字段不存在。
     * @throws IllegalAccessException    如果访问指定字段被限制。
     * @throws InvocationTargetException 如果调用方法时发生异常。
     * @throws NoSuchMethodException     如果反射调用的方法不存在。
     */
    public static <T extends Enum<T>> CheckResult checkRepeat(Class<T> clazz, String fieldName)
            throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // 参数检查
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(fieldName);

        Object[] enumInstances = getEnumValues(clazz);
        if (Objects.isNull(enumInstances) || enumInstances.length == 0) {
            return CheckResult.fail("The current class does not contain any instances！");
        }

        Field checkField = clazz.getDeclaredField(fieldName);
        checkField.setAccessible(true);

        // key:重复field的值，value：所有这些重复值的实例列表
        boolean hasRepeat = false;
        Map<Object, List<Object>> repeatInstanceMap = new HashMap<>();
        for (Object instance : enumInstances) {
            Object fieldValue = checkField.get(instance);
            repeatInstanceMap.computeIfAbsent(fieldValue, k -> new ArrayList<>()).add(instance);
            if (!hasRepeat && repeatInstanceMap.get(fieldValue).size() > 1) {
                hasRepeat = true;
            }
        }

        if (hasRepeat) {
            repeatInstanceMap.values().removeIf(list -> list.size() == 1);
            return CheckResult.fail("Enum repeat info: " + repeatInstanceMap);
        } else {
            return CheckResult.success();
        }
    }

    /**
     * 获取指定枚举类的所有枚举值数组。
     * 该方法通过反射机制调用枚举类的{@code values()}方法来获取所有的枚举常量。
     *
     * @param clazz 指定的枚举类{@code Class}对象。
     * @return 返回指定枚举类的所有枚举值数组。
     * @throws NoSuchMethodException     如果指定枚举类中没有values方法，则抛出此异常。
     * @throws IllegalAccessException    如果无权访问values方法，则抛出此异常。
     * @throws InvocationTargetException 如果调用values方法时发生异常，则抛出此异常。
     */
    public static <T extends Enum<T>> T[] getEnumValues(Class<T> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Objects.requireNonNull(clazz);
        Method valuesMethod = clazz.getMethod("values");

        @SuppressWarnings("unchecked")
        T[] enumValues = (T[]) valuesMethod.invoke(null);
        return enumValues;
    }
}