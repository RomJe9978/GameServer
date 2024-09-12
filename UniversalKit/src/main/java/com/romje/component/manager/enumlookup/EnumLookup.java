package com.romje.component.manager.enumlookup;

import com.romje.constants.StringConst;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * “枚举实例”的映射查询类，单例
 *
 * <p>枚举中通常会有根据某一个成员变量值反查枚举，最简单的做法是使用{@code values()}
 * 获取枚举实例列表，遍历与“指定成员值”进行对比，从而返回。而每次调用{@code values()}
 * 其实都是产生一个克隆的数组，这点在高频、性能敏感、内存敏感等场景下，都是不可接受的。
 * 优化手段也比较简单，就是使用静态代码{@code static}在类加载时候遍历一次形成一个静态
 * 映射Map，缓存在内存中，从而在需要根据成员变量值反查询的时候直接从Map中获取。
 *
 * <p>上述优化手段非常实用，但是需要每个枚举类都自己实现一套逻辑完全一致的代码，所以这里
 * 实现了一个统一代理，进程启动的时候调用{@code registerEnums()}即可统一管理。开发人员需
 * 要做的仅仅是，“在需要反查的成员变量上标识注解”即可(该注解可自定义)。
 *
 * @author liu xuan jie
 */
public enum EnumLookup {

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
     * 获取指定枚举类中的所有“枚举实例”的数量。
     *
     * @param clazz 指定枚举类，不允许为{@code null}。
     * @return 如果没有对应枚举类，则返回{@code 0}。
     */
    public int sizeOf(Class<? extends Enum<?>> clazz) {
        Objects.requireNonNull(clazz);
        if (EmptyUtil.isEmpty(this.allEnumMap)) {
            return 0;
        }

        Map<Object, Enum<?>> keyInstanceMap = this.allEnumMap.get(clazz);
        return EmptyUtil.isEmpty(keyInstanceMap) ? 0 : keyInstanceMap.size();
    }

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
        if (EmptyUtil.isEmpty(this.allEnumMap)) {
            return defaultEnum;
        }

        Map<Object, Enum<?>> keyInstanceMap = this.allEnumMap.get(clazz);
        if (EmptyUtil.isEmpty(keyInstanceMap)) {
            return defaultEnum;
        }

        Object instanceObject = keyInstanceMap.get(enumKey);
        if (Objects.isNull(instanceObject)) {
            return defaultEnum;
        }

        @SuppressWarnings("unchecked")
        T enumInstance = (T) instanceObject;
        return enumInstance;
    }

    /**
     * 注册枚举类，建立每一个枚举类根据字段反查枚举实例的映射关系。
     *
     * <p>如果指定列表为{@code isEmpty}，则默认返回{@code true}，不认为是失败。
     * <p>每一个枚举类中标识{@link EnumKey}注解的字段会被作为反查枚举实例的键。
     *
     * @param enumClassList 枚举类列表，不允许为{@code null}
     * @return 如果失败，结果中包含失败信息{@link BoolResult#message()}
     * @throws IllegalAccessException    当访问字段失败时抛出此异常。
     * @throws InvocationTargetException 当调用方法失败时抛出此异常。
     * @throws NoSuchMethodException     当找不到方法时抛出此异常。
     */
    public BoolResult registerEnums(List<Class<? extends Enum<?>>> enumClassList)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // 构建一组枚举的反查询映射关系
        Objects.requireNonNull(enumClassList);
        Map<Class<? extends Enum<?>>, Map<Object, Enum<?>>> tempMap = new HashMap<>();
        for (Class<? extends Enum<?>> clazz : enumClassList) {
            List<Field> fieldList = ReflectionUtil.getFieldsWithAnnotation(clazz, EnumKey.class);
            if (EmptyUtil.isEmpty(fieldList)) {
                continue;
            }

            BoolResult boolResult = buildKeyToInstanceMap(clazz, fieldList.get(0));
            if (boolResult.isFail()) {
                return boolResult;
            }

            @SuppressWarnings("unchecked")
            Map<Object, Enum<?>> keyInstanceMap = (Map<Object, Enum<?>>) boolResult.data();
            tempMap.put(clazz, keyInstanceMap);
        }

        this.allEnumMap = tempMap;
        return BoolResult.success();
    }

    /**
     * 构建单个枚举类的，字段Key反查枚举实例的映射关系
     *
     * <p>成功结果为{@code Map<Object, Enum<?>>}，key为字段的值，value为枚举实例。
     * <p>如果枚举中没有任何实例，则返回成功，映射结果为{@code emptyMap()}。
     *
     * @param clazz     枚举类，不允许为{@code null}
     * @param keyField  枚举类中需要反查的键值字段，不允许为{@code null}
     * @return 如果失败，结果中包含失败信息{@link BoolResult#message()};
     * 如果成功，{@link BoolResult#data()}中获取{@code Map<Object, Enum<?>>}。
     */
    public BoolResult buildKeyToInstanceMap(Class<? extends Enum<?>> clazz, Field keyField)
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
                return BoolResult.fail(toRepeatString(clazz, oldValue, enumInstance));
            }
        }
        return BoolResult.success(StringConst.BLANK_STRING, keyInstanceMap);
    }

    private String toRepeatString(Class<? extends Enum<?>> clazz, Enum<?> firstInstance, Enum<?> secondInstance) {
        return "Enum key repeat! class name:" + clazz.getName() +
                ", first enum:" + firstInstance.name() +
                ", second enum:" + secondInstance.name();
    }
}