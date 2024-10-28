package com.games.business.log;

import com.games.business.game.component.LoggerCheck;
import com.games.business.game.component.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录器统一管理,不允许初始化
 *
 * @author liu xuan jie
 */
@LoggerRepository
public final class Log {

    private Log() {
    }

    /**
     * 业务层通用的逻辑日志记录器
     */
    @LoggerCheck(name = "LogicLog")
    public static final Logger LOGIC = LoggerFactory.getLogger("LogicLog");
}
