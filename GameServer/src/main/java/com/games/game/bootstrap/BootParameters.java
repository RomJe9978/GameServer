package com.games.game.bootstrap;

/**
 * 启动参数统一管理，不允许实例化
 *
 * <p>单独提取的原因：不是所有的参数都需要“动态配置（JVM参数，配置文件等）”的，
 * 有些固定静态常量直接使用“常量类”管理反而更好。并且，将所有“启动时相关”的参
 * 数使用单独文件更加直白易理解，不与任何其他常量类耦合，降低维护成本。
 *
 * @author liu xuan jie
 */
public final class BootParameters {

    private BootParameters() {
    }

    /**
     * 扫描"枚举类统一管理"的包名
     */
    public static final String SCAN_ENUM_MANAGE_PACKAGE_NAME = "com.games";

    /**
     * "枚举类字段重复性检查"的包名
     */
    public static final String ENUM_CHECKED_PACKAGE_NAME = "com.games";

    /**
     * 常量类检查重复性，扫描的包名
     */
    public static final String CONST_CHECK_UNIQUE_PACKAGE_NAME = "com.games";

    /**
     * 扫描所有日志记录器的包名
     */
    public static final String SCAN_LOGGER_PACKAGE_NAME = "com.games";

    /**
     * 日志配置文件名称
     */
    public static final String LOG_CONFIG_FILE_NAME = "log4j2.xml";

    /**
     * “事件监听者”，需要扫描的包名
     */
    public static final String SCAN_EVENT_LISTENER_PACKAGE_NAME = "com.games";
}