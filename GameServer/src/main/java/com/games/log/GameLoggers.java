package com.games.log;

import com.romje.component.log.CustomizeLoggerFactory;
import org.slf4j.Logger;

/**
 * 日志门面类
 *
 * @author RomJe
 */
public class GameLoggers {
    private final static Logger SYSTEM = CustomizeLoggerFactory.newInstance("LogicLog", "[SYSTEM]");

    public static Logger system() {
        return SYSTEM;
    }
}
