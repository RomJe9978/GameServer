package com.romje.component.log;

import org.slf4j.Logger;

/**
 * 日志仓库，统一管理的地方
 *
 * @author RomJe
 */
public class Loggers {

    private final static Logger CORE = CustomizeLoggerFactory.newInstance("CoreLog", "[CORE]");

    public static Logger core() {
        return CORE;
    }
}
