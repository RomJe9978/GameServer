package com.romje.component.log;

/**
 * 自定义日志的上下文
 * <p>管理所有实际日志配置的地方，如有修改，更新此处即可
 *
 * @author RomJe
 */
public class CustomizeLoggerContext {
    /**
     * 使用的配置文件名称
     */
    private final static String LOG_CONFIG_FILE = "log4j2.xml";

    /**
     * 单例
     */
    private final static CustomizeLoggerContext INSTANCE = new CustomizeLoggerContext();

    /**
     * 日志检查器
     */
    private final ILoggerChecker loggerChecker;

    private CustomizeLoggerContext() {
        System.setProperty("log4j.configurationFile", LOG_CONFIG_FILE);
        this.loggerChecker = Log4j2LoggerChecker.newInstance();
        this.loggerChecker.init(LOG_CONFIG_FILE);
    }

    public static CustomizeLoggerContext getInstance() {
        return INSTANCE;
    }

    public ILoggerChecker getLoggerChecker() {
        return this.loggerChecker;
    }
}
