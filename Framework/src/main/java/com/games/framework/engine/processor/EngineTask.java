package com.games.framework.engine.processor;

import com.games.framework.engine.context.FrameworkContext;
import com.games.framework.engine.service.AbstractService;
import lombok.Getter;

/**
 * 最终被提交执行的线程任务
 *
 * @author liuxuanjie
 */
public class EngineTask implements Runnable {

    /**
     * 服务器service
     */
    @Getter
    private AbstractService serverService;

    /**
     * 上一次任务开始执行的时间戳：ms
     */
    private long lastStartTimestamp;

    /**
     * 任务帧的帧率:毫秒
     */
    private final int frameRate;


    /**
     * @param frameRate     内部service的业务帧率
     * @param serverService 内部业务逻辑service
     */
    private EngineTask(int frameRate, AbstractService serverService) {
        this.frameRate = frameRate;
        this.lastStartTimestamp = FrameworkContext.INSTANCE.getClock().currentTimeMillis();
        this.serverService = serverService;
    }

    public static EngineTask newInstance(int frameRate, AbstractService serverService) {
        return new EngineTask(frameRate, serverService);
    }

    @Override
    public void run() {
        long curTimestamp = FrameworkContext.INSTANCE.getClock().currentTimeMillis();
        this.lastStartTimestamp = curTimestamp;
        this.serverService.tick(curTimestamp);
    }


    /**
     * 是否需要执行者去执行
     *
     * @param curTimestamp 毫秒时间戳
     */
    public boolean requireExe(long curTimestamp) {
        return (int) (curTimestamp - this.lastStartTimestamp) >= frameRate;
    }
}
