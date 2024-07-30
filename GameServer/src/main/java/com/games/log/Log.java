package com.games.log;

import com.games.game.component.LoggerCheck;
import com.games.game.component.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录器统一管理,不允许初始化
 *
 * @author RomJe
 */
@LoggerRepository
public class Log {

    private Log() {
    }

    /**
     * 业务层通用的逻辑日志记录器
     */
    @LoggerCheck(name = "LogicLog")
    public final static Logger LOGIC = LoggerFactory.getLogger("LogicLog");
}
