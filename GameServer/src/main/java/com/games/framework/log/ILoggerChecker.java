package com.games.framework.log;

import org.slf4j.Logger;

import java.util.Objects;

/**
 * 日志检查
 *
 * @author RomJe
 */
public interface ILoggerChecker {

    /**
     * 检查器的初始化操作
     *
     * @param configFileName 配置文件名称
     * @return {@code false} if init fail
     */
    boolean init(String configFileName);

    /**
     * 指定名称的logger是否在配置文件中存在
     *
     * @param loggerName 指定的logger名称
     * @return {@code false} if logger is not exist
     */
    boolean isExist(String loggerName);

    /**
     * check logger name is root
     *
     * @param loggerName ignore case
     * @return {@code true} if name is “ROOT”
     */
    default boolean isRoot(String loggerName) {
        if (Objects.isNull(loggerName)) {
            return false;
        }
        return Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(loggerName);
    }
}
