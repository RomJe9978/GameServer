package com.games.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志门面类
 *
 * @author RomJe
 */
public class GameLog {
    /**
     * 所有业务模块统一使用的logger
     */
    private final static Logger LOGIC = LoggerFactory.getLogger("LogicLog");

    /**
     * 对外统一调用接口，方便管理和维护
     */
    public static Logger logic() {
        return LOGIC;
    }
}
