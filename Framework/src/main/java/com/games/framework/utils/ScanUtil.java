package com.games.framework.utils;

import com.romje.utils.EmptyUtil;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 扫描工具，涉及扫包，查找指定条件的类或资源，不允许实例化
 * 当前工具类中需要{@code org.reflections.reflections}依赖包
 *
 * @author liu xuan jie
 */
public class ScanUtil {

    private ScanUtil() {
    }

    /**
     * {@link #scanClassesAsSet(String)}。
     *
     * @return 任何错误返回{@code Collections.emptyList()}，不会为{@code null}。
     */
    public static List<Class<?>> scanClassesAsList(String packageName) {
        Set<Class<?>> classSet = scanClassesAsSet(packageName);
        return EmptyUtil.nonEmpty(classSet) ? new ArrayList<>(classSet) : Collections.emptyList();
    }

    /**
     * 扫描指定包下的所有{@link Class},相当于扫描{@link Object}的所有子类。
     *
     * @param packageName 指定包名，不允许为{@code null}和{@code empty}
     * @return 任何错误返回{@code Collections.emptySet()}，不会为{@code null}
     */
    public static Set<Class<?>> scanClassesAsSet(String packageName) {
        if (EmptyUtil.isEmpty(packageName)) {
            return Collections.emptySet();
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setUrls(ClasspathHelper.forPackage(packageName));
        configurationBuilder.setScanners(Scanners.SubTypes);

        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getSubTypesOf(Object.class);
    }

    /**
     * {@link #scanAnnotationAsSet(String, Class)}
     *
     * @return 任何错误返回{@code Collections.emptyList()}，不会为{@code null}
     */
    public static List<Class<?>> scanAnnotationAsList(String packageName, Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = scanAnnotationAsSet(packageName, annotation);
        return EmptyUtil.nonEmpty(classSet) ? new ArrayList<>(classSet) : Collections.emptyList();
    }

    /**
     * 扫描指定包下的所有使用了指定注解的类
     *
     * @param packageName 指定包名，不允许为{@code null}和{@code empty}
     * @param annotation  指定注解，不允许为{@code null}
     * @return 任何错误返回{@code Collections.emptySet()}，不会为{@code null}
     */
    public static Set<Class<?>> scanAnnotationAsSet(String packageName, Class<? extends Annotation> annotation) {
        if (EmptyUtil.isEmpty(packageName) || Objects.isNull(annotation)) {
            return Collections.emptySet();
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setUrls(ClasspathHelper.forPackage(packageName));
        configurationBuilder.setScanners(Scanners.TypesAnnotated);

        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * {@link #scanEnumsAsSet(String)}
     *
     * @return 任何错误返回{@code Collections.emptyList()}，不会为{@code null}
     */
    public static List<Class<? extends Enum<?>>> scanEnumsAsList(String packageName) {
        Set<Class<? extends Enum<?>>> classSet = scanEnumsAsSet(packageName);
        return EmptyUtil.nonEmpty(classSet) ? new ArrayList<>(classSet) : Collections.emptyList();
    }

    /**
     * 扫描指定包下所有的枚举类并返回为Set集合。
     *
     * @param packageName 要扫描的包名。
     * @return 任何错误返回{@code Collections.emptySet()}，不会为{@code null}。
     */
    public static Set<Class<? extends Enum<?>>> scanEnumsAsSet(String packageName) {
        if (EmptyUtil.isEmpty(packageName)) {
            return Collections.emptySet();
        }

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setUrls(ClasspathHelper.forPackage(packageName));
        configurationBuilder.setScanners(Scanners.SubTypes);
        Reflections reflections = new Reflections(configurationBuilder);

        // 工具扫描无法获取Enum<?>,因为Enum枚举超类就是代泛型的，此处进行一次强转
        return reflections.getSubTypesOf(Enum.class).stream().map(clazz -> {
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumClazz = (Class<? extends Enum<?>>) clazz;
            return enumClazz;
        }).collect(Collectors.toSet());
    }
}