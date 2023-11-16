package com.romje.component.log;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 日志工厂
 *
 * @author RomJe
 */
public class CustomizeLoggerFactory {
    /**
     * {@link #newInstance(String, String, boolean)}
     */
    public static Logger newInstance(String loggerName, String prefix) {
        return newInstance(loggerName, prefix, true);
    }

    /**
     * @param loggerName      关联的配置中的logger的名称
     * @param prefix          定制化的logger输出前缀
     * @param includeLocation 是否需要打印位置信息
     * @return 一个新创建的实例
     */
    public static Logger newInstance(String loggerName, String prefix, boolean includeLocation) {
        checkExist(loggerName);
        includeLocation = includeLocation(loggerName);
        return CustomizeLogger.newInstance(LoggerFactory.getLogger(loggerName), prefix, includeLocation);
    }

    /**
     * 检查指定名称的logger是否存在
     */
    public static void checkExist(String loggerName) {
        Objects.requireNonNull(loggerName);
        ILoggerChecker loggerChecker = CustomizeLoggerContext.getInstance().getLoggerChecker();
        if (Objects.isNull(loggerChecker)) {
            return;
        }

        if (!loggerChecker.isExist(loggerName)) {
            Loggers.root().error("Logger:{} config is not exist, please check!", loggerName);
        }
    }

    /**
     * 检查指定名称的logger是否配置需要日志位置信息展示
     */
    public static boolean includeLocation(String loggerName) {
        Objects.requireNonNull(loggerName);
        LoggerContext loggerContext = LoggerContext.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        return configuration.getLoggerConfig(loggerName).isIncludeLocation();
    }
}
