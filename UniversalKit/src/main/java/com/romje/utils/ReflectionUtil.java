package com.romje.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 反射相关工具接口的封装
 *
 * @author liu xuan jie
 */
public final class ReflectionUtil {

    /**
     * 枚举类中values方法的名称
     */
    private static final String ENUM_VALUES_METHOD_NAME = "values";

    private ReflectionUtil() {
    }

    /**
     * 获取指定类中使用了指定注解的所有字段（不包括继承的字段）。
     * <p>内部使用{@link Class#getDeclaredFields()}来首先获取所有字段。
     *
     * @param clazz           要检查的类。
     * @param annotationClazz 注解的类型。
     * @return 包含使用了指定注解的所有字段的列表。不会为{@code null}
     */
    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClazz)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    /**
     * 获取指定类中使用了指定注解的所有方法（不包括继承的方法）。
     * <p>内部使用{@link Class#getDeclaredMethods()}来首先获取所有方法。
     *
     * @param clazz           要检查的类。
     * @param annotationClazz 注解的类型。
     * @return 包含使用了指定注解的所有方法的列表。不会为{@code null}
     */
    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClazz)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    /**
     * 获取指定枚举类的所有枚举实例。
     * 该方法通过反射机制调用枚举类的{@code values()}方法来获取所有的枚举常量。
     *
     * @param clazz 指定的枚举类{@code Class}对象。
     * @return 返回指定枚举类的所有枚举值数组。
     * @throws NoSuchMethodException     如果指定枚举类中没有values方法，则抛出此异常。
     * @throws IllegalAccessException    如果无权访问values方法，则抛出此异常。
     * @throws InvocationTargetException 如果调用values方法时发生异常，则抛出此异常。
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T[] getEnumValues(Class<? extends Enum<?>> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Objects.requireNonNull(clazz);
        Method valuesMethod = clazz.getMethod(ENUM_VALUES_METHOD_NAME);
        return (T[]) valuesMethod.invoke(null);
    }

    /**
     * 检查指定字段是否为静态常量{@code static final}修饰
     *
     * @param field 不允许为{@code null}
     * @return 如果是“静态常量”，返回{@code true}
     */
    public static boolean isStaticFinalField(Field field) {
        Objects.requireNonNull(field);
        int modifier = field.getModifiers();
        return Modifier.isStatic(modifier) && Modifier.isFinal(modifier);
    }

    /**
     * 检查指定方法是否为静态方法，{@code static}修饰
     *
     * @param method 不允许为{@code null}
     * @return 如果是“静态方法”，返回{@code true}
     */
    public static boolean isStaticMethod(Method method) {
        Objects.requireNonNull(method);
        int modifier = method.getModifiers();
        return Modifier.isStatic(modifier);
    }

    /**
     * 检查指定方法是否为非静态方法，没有{@code static}修饰
     *
     * @param method 不允许为{@code null}
     * @return 如果是“非静态方法”，返回{@code ture}
     */
    public static boolean nonStaticMethod(Method method) {
        return !isStaticMethod(method);
    }
}