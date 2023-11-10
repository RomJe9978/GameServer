package com.romje.component.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工厂
 *
 * @author RomJe
 */
public class CustomizeLoggerFactory {
    /**
     * @param logName 关联的配置中的logger的名称
     * @param prefix  定制化的logger输出前缀
     * @return 一个新创建的实例
     */
    public static Logger newInstance(String logName, String prefix) {
        return CustomizeLogger.newInstance(LoggerFactory.getLogger(logName), prefix);
    }
}
