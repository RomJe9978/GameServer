package com.romje.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心库的日志门面
 * <p>需要依赖该库的工程指定日志配置，名称为{@code CoreLog}
 *
 * @author RomJe
 */
public class CoreLog {
    /**
     * 核心库的日志
     */
    private final static Logger CORE = LoggerFactory.getLogger("CoreLog");

    /**
     * 对外统一调用接口，方便管理和维护
     */
    public static Logger core() {
        return CORE;
    }
}
