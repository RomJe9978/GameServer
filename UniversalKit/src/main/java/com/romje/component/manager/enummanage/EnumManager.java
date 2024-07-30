package com.romje.component.manager.enummanage;

import com.romje.constants.StringConst;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 枚举类的全局管理者，单例
 * 该类对枚举常见操作做统一管理，避免每次手写一个枚举类的时候都对重复逻辑重新编写
 *
 * <p>枚举中通常会有根据某一个成员变量值反查枚举，最简单的做法是使用{@code values()}
 * 获取枚举实例列表，遍历与“指定成员值”进行对比，从而返回。而每次调用{@code values()}
 * 其实都是产生一个克隆的数组，这点在高频、性能敏感、内存敏感等场景下，都是不可接受的。
 * 优化手段也比较简单，就是使用静态代码{@code static}在类加载时候遍历一次形成一个静态
 * 映射Map，缓存在内存中，从而在需要根据成员变量值反查询的时候直接从Map中获取。
 *
 * <p>上述优化手段非常实用，但是需要每个枚举类都自己实现一套逻辑完全一致的代码，所以这里
 * 实现了一个统一管理，进程启动的时候调用{@code registerEnums()}即可统一管理。开发人员需
 * 要做的仅仅是，“在需要反查的成员变量上标识注解”即可(该注解可自定义)。
 *
 * @author liu xuan jie
 */
public enum EnumManager {

    /**
     * 单例
     */
    INSTANCE;

    /**
     * 所有注册枚举管理的映射信息
     * key:指定枚举类，value:{key:枚举类中的字段值，value:字段值对应的枚举实例}
     * 例如：AAA.class,1,AAA.ONE,其中{@code AAA}是一个枚举类
     */
    private Map<Class<? extends Enum<?>>, Map<Object, Enum<?>>> allEnumMap;

    /**
     * {@link #getEnum(Class, Object, Enum)}
     */
    public <T extends Enum<T>> T getEnum(Class<T> clazz, Object enumKey) {
        return getEnum(clazz, enumKey, null);
    }

    /**
     * 根据"枚举类型"和"注册的键值"获取枚举常量。
     *
     * @param clazz       枚举类的Class对象，用于指定要检索的枚举类型。
     * @param enumKey     用于标识特定枚举常量的键，注册的枚举字段的值。
     * @param defaultEnum 默认的枚举常量，当找不到指定的枚举常量时返回。
     * @param <T>         枚举类的类型，必须是枚举类型。
     * @return 键对应的枚举常量，如果不存在则返回默认枚举常量。
     */
    public <T extends Enum<T>> T getEnum(Class<T> clazz, Object enumKey, T defaultEnum) {
        if (this.allEnumMap.isEmpty()) {
            return defaultEnum;
        }

        Map<Object, Enum<?>> keyInstanceMap = this.allEnumMap.get(clazz);
        if (EmptyUtil.isEmpty(keyInstanceMap)) {
            return defaultEnum;
        }

        @SuppressWarnings("unchecked")
        T enumInstance = (T) keyInstanceMap.get(enumKey);
        return Objects.isNull(enumInstance) ? defaultEnum : enumInstance;
    }

    /**
     * 注册枚举类，构造枚举类根据字段反查映射关系
     * <p>如果指定列表为{@code isEmpty}，则默认返回{@code true}，不认为是失败。
     *
     * @param enumClassList   枚举类列表，不允许为{@code null}
     * @param fieldAnnotation 枚举字段注解，表示该字段可以映射实例。
     * @return 如果失败，结果中包含失败信息{@link BoolResult#message()}
     * @throws IllegalAccessException    当访问字段失败时抛出此异常。
     * @throws InvocationTargetException 当调用方法失败时抛出此异常。
     * @throws NoSuchMethodException     当找不到方法时抛出此异常。
     */
    public BoolResult registerEnums(List<Class<? extends Enum<?>>> enumClassList,
                                    Class<? extends Annotation> fieldAnnotation)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // 构建一组枚举的反查询映射关系
        Objects.requireNonNull(enumClassList);
        BoolResult boolResult = buildGroupEnums(enumClassList, fieldAnnotation);
        if (boolResult.isFail()) {
            return boolResult;
        }

        @SuppressWarnings("unchecked")
        Map<Class<? extends Enum<?>>, Map<Object, Enum<?>>> tempMap =
                (Map<Class<? extends Enum<?>>, Map<Object, Enum<?>>>) boolResult.data();
        this.allEnumMap = tempMap;
        return BoolResult.success();
    }

    /**
     * 构建指定枚举列表，所有枚举类中字段反查枚举实例的映射关系
     * <p>如果某一个枚举类没有任何字段标识指定注解，则代表该枚举不需要构建
     */
    private BoolResult buildGroupEnums(List<Class<? extends Enum<?>>> classList,
                                       Class<? extends Annotation> fieldAnnotation)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<Class<? extends Enum<?>>, Map<Object, Enum<?>>> tempMap = new HashMap<>();
        for (Class<? extends Enum<?>> clazz : classList) {
            List<Field> fieldList = ReflectionUtil.getFieldsWithAnnotation(clazz, fieldAnnotation);
            if (EmptyUtil.isEmpty(fieldList)) {
                continue;
            }

            BoolResult boolResult = buildSingleEnum(clazz, fieldList.get(0));
            if (boolResult.isFail()) {
                return boolResult;
            }

            @SuppressWarnings("unchecked")
            Map<Object, Enum<?>> keyInstanceMap = (Map<Object, Enum<?>>) boolResult.data();
            tempMap.put(clazz, keyInstanceMap);
        }
        return BoolResult.success(StringConst.BLANK_STRING, tempMap);
    }

    /**
     * 构建单个枚举类的，字段反查枚举实例的映射关系
     */
    private BoolResult buildSingleEnum(Class<? extends Enum<?>> clazz, Field keyField)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Enum<?>[] enumInstances = ReflectionUtil.getEnumValues(clazz);
        if (EmptyUtil.isEmpty(enumInstances)) {
            return BoolResult.success(StringConst.BLANK_STRING, Collections.emptyMap());
        }

        keyField.setAccessible(true);

        Map<Object, Enum<?>> keyInstanceMap = new HashMap<>(enumInstances.length);
        for (Enum<?> enumInstance : enumInstances) {
            Enum<?> oldValue = keyInstanceMap.put(keyField.get(enumInstance), enumInstance);
            if (Objects.nonNull(oldValue)) {
                return BoolResult.fail(buildRepeatInfo(clazz, oldValue, enumInstance));
            }
        }
        return BoolResult.success(StringConst.BLANK_STRING, keyInstanceMap);
    }

    private String buildRepeatInfo(Class<? extends Enum<?>> clazz, Enum<?> firstInstance, Enum<?> secondInstance) {
        return "Enum key repeat! class name:" + clazz.getName() +
                ", first enum:" + firstInstance.name() +
                ", second enum:" + secondInstance.name();
    }
}