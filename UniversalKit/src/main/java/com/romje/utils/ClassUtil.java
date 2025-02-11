package com.romje.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 涉及{@link Class}的相关操作封装，包括反射
 *
 * @author liu xuan jie
 */
public final class ClassUtil {

    private static final String ENUM_VALUES_METHOD_NAME = "values";

    /**
     * 基本类型与其对应的包装类型的映射
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>(8);

    /**
     * 包装类型与其对应的基本类型的映射
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<>(8);

    static {
        PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
        PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);

        WRAPPER_TO_PRIMITIVE.put(Boolean.class, boolean.class);
        WRAPPER_TO_PRIMITIVE.put(Byte.class, byte.class);
        WRAPPER_TO_PRIMITIVE.put(Character.class, char.class);
        WRAPPER_TO_PRIMITIVE.put(Double.class, double.class);
        WRAPPER_TO_PRIMITIVE.put(Float.class, float.class);
        WRAPPER_TO_PRIMITIVE.put(Integer.class, int.class);
        WRAPPER_TO_PRIMITIVE.put(Long.class, long.class);
        WRAPPER_TO_PRIMITIVE.put(Short.class, short.class);
    }

    private ClassUtil() {
    }

    /**
     * 判断一个类型是否是基本数据类型
     *
     * @param clazz 要检查的类型
     * @return 如果类型是基本数据类型，则返回{@code true}；否则返回{@code false}
     */
    public static boolean isPrimitiveType(Class<?> clazz) {
        return PRIMITIVE_TO_WRAPPER.containsKey(clazz);
    }

    /**
     * 判断一个类型是否是基本数据类型的数组类
     *
     * @param clazz 要检查的类型
     * @return 如果类型是基本数据类型的数组类，则返回 true；否则返回 false
     */
    public static boolean isPrimitiveArrayType(Class<?> clazz) {
        if (!clazz.isArray()) {
            return false;
        }
        Class<?> componentType = clazz.getComponentType();
        return componentType.isPrimitive();
    }

    /**
     * 将基本类型转换为其对应的包装类型
     *
     * @param field 要检查的字段
     * @return 如果字段是基本数据类型，则返回其对应的包装类型；否则返回字段的原始类型
     */
    public static Class<?> convertToWrapperType(Field field) {
        Class<?> type = field.getType();
        return PRIMITIVE_TO_WRAPPER.getOrDefault(type, type);
    }

    /**
     * 将包装类型转换为其对应的基本类型
     *
     * @param field 要检查的字段
     * @return 如果字段是包装类型，则返回其对应的基本类型；否则返回字段的原始类型
     */
    public static Class<?> convertToPrimitiveType(Field field) {
        Class<?> type = field.getType();
        return WRAPPER_TO_PRIMITIVE.getOrDefault(type, type);
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

    /**
     * 检查指定类中是否有一个{@code public}的方法，继承父类或者实现接口也算
     *
     * @param clazz      指定类，不允许为{@code null}
     * @param methodName 指定方法名，不允许为{@code null}
     * @return 只有当包含的时候，才会返回{@code true}
     */
    public static boolean hasPublicMethod(Class<?> clazz, String methodName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(methodName);

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查指定类自身内部是否有一个指定名称的方法。
     * 明确表示，检查的是类自身显式声明的方法，而非隐式继承的方法。
     *
     * @param clazz      指定类，不允许为{@code null}
     * @param methodName 指定方法名，不允许为{@code null}
     * @return 只有当包含的时候，才会返回{@code true}
     */
    public static boolean hasDeclaredMethod(Class<?> clazz, String methodName) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(methodName);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }
}