package com.romje.component.checker.enumcheck;

import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 枚举检查类，不需要实例化
 *
 * @author liu xuan jie
 */
public final class EnumChecker {

    private EnumChecker() {
    }

    /**
     * 指定一组枚举类集合,检查每一个枚举类的实例在指定字段上的值是否存在重复。
     *
     * <p>如果指定列表为{@code isEmpty}，相当于不需要检查，直接返回成功。
     * <p>如果某一个枚举中没有任何字段使用了注解标识，代表该枚举类不需要检查。
     * <p>单个枚举类检查规则，详看{@link #checkFieldUnique(Class, String)}。
     *
     * @param enumClassList   指定检查的所有枚举类集合。
     * @param fieldAnnotation 指定检查重复的字段注解。
     * @return 如果有重复，返回{@code false}，并且返回提示信息{@link BoolResult#message()}
     * @throws NoSuchFieldException      如果枚举类中不存在指定字段。
     * @throws IllegalAccessException    如果无权访问枚举类的字段。
     * @throws InvocationTargetException 如果调用方法时发生异常。
     * @throws NoSuchMethodException     如果枚举类中不存在指定方法。
     */
    public static BoolResult checkFieldUnique(List<Class<? extends Enum<?>>> enumClassList,
                                              Class<? extends Annotation> fieldAnnotation)
            throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Objects.requireNonNull(enumClassList);

        // key:出现重复的类名，value:错误描述
        Map<String, List<String>> failMessageMap = new HashMap<>();
        for (Class<? extends Enum<?>> clazz : enumClassList) {
            List<Field> fieldList = ReflectionUtil.getFieldsWithAnnotation(clazz, fieldAnnotation);
            for (Field field : fieldList) {
                BoolResult result = checkFieldUnique(clazz, field.getName());
                if (result.isFail()) {
                    failMessageMap.computeIfAbsent(clazz.getName(), k -> new ArrayList<>()).add(result.message());
                }
            }
        }
        return EmptyUtil.isEmpty(failMessageMap) ? BoolResult.success() : BoolResult.fail(failMessageMap.toString());
    }

    /**
     * 检查给定枚举类，其指定的字段，是否存在值重复的情况。
     *
     * <p>如果枚举类没有任何实例，则会直接返回{@code true}，相当于不需要检查。
     * <p>重复的检查准则是{@link Object#equals(Object)}, 如果{@code true}，则认为重复。
     *
     * @param clazz     枚举类的Class对象，用于反射获取枚举值。
     * @param fieldName 需要检查重复性的字段名。
     * @return 如果存在重复，则返回失败，失败信息从{@link BoolResult#message()}获取。
     * @throws NoSuchFieldException      如果指定字段不存在。
     * @throws IllegalAccessException    如果访问指定字段被限制。
     * @throws InvocationTargetException 如果调用方法时发生异常。
     * @throws NoSuchMethodException     如果反射调用的方法不存在。
     */
    public static BoolResult checkFieldUnique(Class<? extends Enum<?>> clazz, String fieldName)
            throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object[] enumInstances = ReflectionUtil.getEnumValues(clazz);
        if (Objects.isNull(enumInstances) || enumInstances.length == 0) {
            return BoolResult.success();
        }

        Field checkField = clazz.getDeclaredField(fieldName);
        checkField.setAccessible(true);

        // key:重复field的值，value：所有这些重复值的实例列表
        Map<Object, List<Object>> valueToInstancesMap = new HashMap<>();
        for (Object instance : enumInstances) {
            Object fieldValue = checkField.get(instance);
            valueToInstancesMap.computeIfAbsent(fieldValue, k -> new ArrayList<>()).add(instance);
        }

        valueToInstancesMap.values().removeIf(list -> list.size() == 1);
        return EmptyUtil.isEmpty(valueToInstancesMap) ?
                BoolResult.success() : BoolResult.fail(valueToInstancesMap.toString());
    }
}