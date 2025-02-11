package com.games.framework.engine.module;

/**
 * 关于具体业务逻辑的统一接口
 * 开发者最终面向{@code Module}进行业务逻辑开发
 *
 * @author liu xuan jie
 */
public interface IModule {

    /**
     * 业务模块初始化操作，该方法对于模块本身优先级最高
     *
     * @return 所有失败或者异常返回{@code false}
     */
    public abstract boolean init();

    /**
     * 业务模块驱动帧
     *
     * @param millisTimestamp 本次驱动时间戳：毫秒
     */
    public abstract void tick(long millisTimestamp);
}