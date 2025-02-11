package com.games.framework.engine.processor;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 固定线程数量的任务执行者
 *
 * @author liuxuanjie
 */
public class FixedThreadExecutor implements ITaskExecutor<EngineTask> {

    /**
     * 线程池
     */
    private ExecutorService threadPool;

    /**
     * 剩余可用的线程数量
     */
    private int remainUsableCount;

    private FixedThreadExecutor(int threadCount) {
        threadCount = threadCount <= 0 ? 1 : threadCount;
        this.remainUsableCount = threadCount;
        this.threadPool = Executors.newFixedThreadPool(threadCount);
    }

    public static FixedThreadExecutor newInstance(int count) {
        return new FixedThreadExecutor(count);
    }


    @Override
    public boolean canSubmit() {
        return this.remainUsableCount > 0;
    }


    @Override
    public Future<?> submit(EngineTask engineTask) {
        this.remainUsableCount--;
        return this.threadPool.submit(engineTask);
    }


    @Override
    public void handleFuture(Future<?> future) {
        if (Objects.isNull(future)) {
            return;
        }

        if (future.isDone()) {
            this.remainUsableCount++;
        }
    }
}
