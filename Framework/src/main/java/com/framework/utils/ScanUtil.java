package com.framework.utils;

import com.romje.utils.EmptyUtil;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 包扫描相关工具，不允许实例化
 * 当前工具类中需要{@code org.reflections.reflections}依赖包
 *
 * @author liu xuan jie
 */
public class ScanUtil {

    private ScanUtil() {
    }

    /**
     * {@link #scanAnnotationAsSet(String, Class)}
     *
     * @return 任何错误或者异常返回{@code Collections.emptyList()}
     */
    public static List<Class<?>> scanAnnotationAsList(String packetName, Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = scanAnnotationAsSet(packetName, annotation);
        return EmptyUtil.nonEmpty(classSet) ? new ArrayList<>(classSet) : Collections.emptyList();
    }

    /**
     * 扫描指定包下的所有使用了指定注解的类
     *
     * @param packetName 指定包名，不允许为{@code null}和{@code empty}
     * @param annotation 指定注解，不允许为{@code null}
     * @return 任何错误或者异常返回{@code Collections.emptySet()}
     */
    public static Set<Class<?>> scanAnnotationAsSet(String packetName, Class<? extends Annotation> annotation) {
        if (EmptyUtil.isEmpty(packetName) || Objects.isNull(annotation)) {
            return Collections.emptySet();
        }

        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setUrls(ClasspathHelper.forPackage(packetName));
            configurationBuilder.setScanners(Scanners.TypesAnnotated);

            Reflections reflections = new Reflections(configurationBuilder);
            return reflections.getTypesAnnotatedWith(annotation);
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}