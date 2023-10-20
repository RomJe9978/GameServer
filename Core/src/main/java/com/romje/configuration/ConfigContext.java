package com.romje.configuration;

import org.apache.commons.configuration2.ImmutableConfiguration;

/**
 * 配置上下文
 *
 * @author RomJe
 */
public class ConfigContext {
    /**
     * 组合具体的配置实现
     */
    private static ImmutableConfiguration configuration;

    /**
     * 自定义设置
     *
     * @param configuration 配置实现类
     */
    public static void setConfiguration(ImmutableConfiguration configuration) {
        ConfigContext.configuration = configuration;
    }

    /**
     * 获得游戏配置
     *
     * @return 如果没有设置，返回{@code Null}
     */
    public static ImmutableConfiguration getConfiguration() {
        return ConfigContext.configuration;
    }
}
