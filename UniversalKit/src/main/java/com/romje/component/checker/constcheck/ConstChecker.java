package com.romje.component.checker.constcheck;

import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 常量类型检查器，不允许实例化
 *
 * @author liu xuan jie
 */
public final class ConstChecker {

    private ConstChecker() {
    }

    /**
     * 指定一组类集合，检查每一个类，其内部字段的值是否唯一(没有重复)。
     *
     * <p>如果指定类集合为{@code isEmpty},代表不需要检查，直接返回成功。
     * <p>单个类检查规则，详看{@link #checkFieldUnique(Class)}。
     *
     * @param classList 指定需要检查的一组类集合。不允许为{@code null}。
     * @return 失败时从{@link BoolResult#message()}中获取失败信息描述。
     * @throws IllegalAccessException 如果访问类或字段时发生权限异常。
     */
    public static BoolResult checkFieldUnique(List<Class<?>> classList) throws IllegalAccessException {
        Objects.requireNonNull(classList);

        // key：类名称，value：当前类下的重复信息描述
        Map<String, String> classToFailMessageMap = new HashMap<>();
        for (Class<?> clazz : classList) {
            BoolResult boolResult = checkFieldUnique(clazz);
            if (boolResult.isFail()) {
                classToFailMessageMap.put(clazz.getName(), boolResult.message());
            }
        }
        return EmptyUtil.isEmpty(classToFailMessageMap) ?
                BoolResult.success() : BoolResult.fail(classToFailMessageMap.toString());
    }

    /**
     * 检查指定类，其内部字段的值是否唯一(没有重复)。
     *
     * <p>只会检查{@code static final}字段。
     * <p>如果指定类中没有任何需要被检查的字段，则认为没有重复。
     *
     * @param clazz 指定被检查的类，不允许为{@code null}。
     * @return 如果有重复，返回失败。从{@link BoolResult#message()}中获取失败信息描述。
     * @throws IllegalAccessException 如果执行的时候发生权限异常。
     */
    public static BoolResult checkFieldUnique(Class<?> clazz) throws IllegalAccessException {
        Objects.requireNonNull(clazz);
        Field[] fields = clazz.getDeclaredFields();
        if (EmptyUtil.isEmpty(fields)) {
            return BoolResult.success();
        }

        Map<Object, List<String>> valueToFieldsMap = new HashMap<>();
        for (Field field : fields) {
            if (!ReflectionUtil.isStaticFinalField(field)) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(null);
            valueToFieldsMap.computeIfAbsent(value, k -> new ArrayList<>()).add(field.getName());
        }

        valueToFieldsMap.values().removeIf(list -> list.size() == 1);
        return EmptyUtil.isEmpty(valueToFieldsMap) ?
                BoolResult.success() : BoolResult.fail(valueToFieldsMap.toString());
    }
}