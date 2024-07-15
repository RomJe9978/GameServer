package com.games.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志门面类
 *
 * @author RomJe
 */
public class GameLoggers {

    private final static Logger SYSTEM = LoggerFactory.getLogger("LogicLog");

    public static Logger system() {
        return SYSTEM;
    }
}
