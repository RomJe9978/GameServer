package com.romje.log;

//import org.apache.logging.log4j.LogManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏中的日志管理器
 *
 * @author liuxuanjie
 */
public class GameLog {

    public final static Logger LOGIC = LoggerFactory.getLogger("LogicLog");

    /**
     * 默认配置的logger：当找不到对应的logger时，会使用该logger
     */
    public final static Logger DEFAULT = LoggerFactory.getLogger(GameLog.class);
}
