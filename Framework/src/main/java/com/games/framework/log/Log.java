package com.games.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 框架层日志使用统一管理
 *
 * @author liu xuan jie
 */
public class Log {

    private Log() {
    }

    /**
     * 底层框架层使用的统一日志，具体配置由业务层指定
     * 如果业务层配置文件不指定该日志，则默认走{@code root}配置
     */
    public static final Logger FRAME = LoggerFactory.getLogger("frame");
}