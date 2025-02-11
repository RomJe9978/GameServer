package com.games.framework.engine.processor;

import java.util.concurrent.Future;

/**
 * 任务执行者接口
 *
 * @author liuxuanjie
 */
public interface ITaskExecutor<T extends Runnable> {

    /**
     * 当前是否可提交任务执行
     */
    boolean canSubmit();

    /**
     * 提交任务执行
     */
    Future<?> submit(T task);

    /**
     * 任务执行结果
     */
    void handleFuture(Future<?> future);
}
