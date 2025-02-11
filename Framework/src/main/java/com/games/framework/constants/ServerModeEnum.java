package com.games.framework.constants;

import lombok.Getter;
import lombok.NonNull;

/**
 * 统一管理“服务器模式”类型
 * <p> 服务器针对不同的模式可以做不同的状态统计，日志埋点，特殊处理等。
 *
 * @author liu xuan jie
 */
@Getter
public enum ServerModeEnum {

    /**
     * “调试模式”，当前模式可以认为是“开发者模式”
     * <p>例如:很多关键点高频日志可以在该模式下进行输出等
     */
    DEBUG("debug"),

    /**
     * “正常模式”，当前模式可以认为是“线上模式”
     * <p>例如:该模式下需要保证控制性能、正确性等服务器稳定性
     */
    NORMAL("normal"),

    /**
     * “暂停模式”，相当于“stop the world”
     * <p>例如:当热更的时候可以选择暂停后热更，然后恢复
     */
    STOP("stop"),
    ;

    /**
     * 模式描述信息
     */
    private final String describe;

    ServerModeEnum(@NonNull String describe) {
        this.describe = describe;
    }
}