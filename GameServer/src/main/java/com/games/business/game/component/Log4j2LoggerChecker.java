package com.games.business.game.component;

import com.romje.model.BoolResult;
import com.romje.utils.ClassUtil;
import com.romje.utils.CollectionUtil;
import com.romje.utils.EmptyUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 使用{@code log4j2}日志框架的检查器，不允许实例化
 *
 * @author RomJe
 */
public final class Log4j2LoggerChecker {

    private Log4j2LoggerChecker() {
    }

    /**
     * 获取所有在配置文件中被配置过的logger名称集合
     * <p>log4j2中的{@code rootLogger}的名称为“空字符串”
     *
     * @return 返回的一个LoggerContext中的配置集合的浅拷贝副本，不会为{@code null}
     */
    public static Set<String> listConfigLoggerNames() {
        // 强制拉起一次"LogManager.getRootLogger()"，避免LoggerContext没有初始化
        org.apache.logging.log4j.Logger logger = LogManager.getRootLogger();

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        return copyOf(configuration.getLoggers().keySet());
    }

    /**
     * 遍历检查类列表中所有使用了{@link LoggerCheck}注解的logger名称是否在“配置列表”中存在
     *
     * @param classList     需要检查的类列表，如果为空列表，代表不需要检查，返回成功
     * @param configNameSet 配置列表，{@link #listConfigLoggerNames()}
     * @return 如果有logger没有配置，则返回失败，失败信息从{@link BoolResult#message()}中获取
     */
    public static BoolResult checkLoggerConfigured(List<Class<?>> classList, Set<String> configNameSet) {
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }

        Map<String, String> classToNonConfigMap = new HashMap<>();
        for (Class<?> clazz : classList) {
            BoolResult boolResult = checkLoggerConfigured(clazz, configNameSet);
            if (boolResult.isFail()) {
                classToNonConfigMap.put(clazz.getName(), boolResult.message());
            }
        }

        return EmptyUtil.isEmpty(classToNonConfigMap) ?
                BoolResult.success() : BoolResult.fail(classToNonConfigMap.toString());
    }

    /**
     * 检查指定类中注解了{@link LoggerCheck}的logger名称是否在“配置列表”中存在
     * <p>例如：{@code getLogger("AAA")},则检查AAA这个名称是否在“配置列表”中存在
     *
     * @param clazz         需要检查的类，会利用反射获取所有{@code LoggerCheck}注解
     * @param configNameSet 配置列表，{@link #listConfigLoggerNames()}
     * @return 如果有logger没有配置，则返回失败，失败信息从{@link BoolResult#message()}中获取
     */
    public static BoolResult checkLoggerConfigured(Class<?> clazz, Set<String> configNameSet) {
        Objects.requireNonNull(clazz);
        List<Field> fieldList = ClassUtil.getFieldsWithAnnotation(clazz, LoggerCheck.class);
        if (EmptyUtil.isEmpty(fieldList)) {
            return BoolResult.success();
        }

        Set<String> nonConfigNameSet = new HashSet<>();
        for (Field field : fieldList) {
            LoggerCheck loggerCheckAnnotation = field.getAnnotation(LoggerCheck.class);
            String name = loggerCheckAnnotation.name();
            boolean required = loggerCheckAnnotation.required();
            if (required && CollectionUtil.nonContains(configNameSet, name)) {
                nonConfigNameSet.add(name);
            }
        }

        return EmptyUtil.isEmpty(nonConfigNameSet) ?
                BoolResult.success() : BoolResult.fail(nonConfigNameSet.toString());
    }

    /**
     * 浅拷贝，只新建集合，避免调用处对原集合的侵入
     */
    private static Set<String> copyOf(Set<String> set) {
        return EmptyUtil.isEmpty(set) ? Collections.emptySet() : new HashSet<>(set);
    }
}
