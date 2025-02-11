package com.games.framework.engine;

import com.games.framework.engine.context.FrameworkContext;
import com.games.framework.log.Log;
import com.romje.component.rate.TimeTicker;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 服务器进程底层处理引擎（进程内部的最底层驱动）
 * 该引擎唯一驱动进程内的所有任务，全局单例，不允许任何方式修改
 *
 * @author liu xuan jie
 */
@Getter
public enum ServerEngine implements Runnable {

    INSTANCE;

    /**
     * 引擎时间迭代器
     */
    @Setter
    private TimeTicker timeTicker;


    public boolean start() {
        if (Objects.isNull(this.timeTicker)) {
            Log.FRAME.warn("[Engine] Server engine time ticker is null！");
            return false;
        }

        // 启动引擎主线程即可
        new Thread(this).start();
        Log.FRAME.info("[Engine] Server engine thread start！");
        return true;
    }

    @Override
    public void run() {
        int count = 1;
        while (true) {
            long curTime = FrameworkContext.INSTANCE.getClock().currentTimeMillis();
            if (this.timeTicker.tick(curTime)) {
                // 驱动帧
                Log.FRAME.info("[Engine] Server engine:{} test logic tick, count:{}!", Thread.currentThread().getId(), count++);

            } else {
                long diffNext = this.timeTicker.diffNext(curTime);
                diffNext = Math.max(diffNext >> 1, 1);

                try {
                    Thread.sleep(diffNext);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}