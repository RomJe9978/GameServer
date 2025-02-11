package com.games.framework.engine.context;

import com.romje.component.clock.IClock;
import com.romje.component.clock.OffsetClock;
import lombok.Getter;
import lombok.Setter;

/**
 * 框架底层运行时全局上下文，全局单例
 * <p> 内部组件有统一默认值，逻辑层可以自行修改
 *
 * @author liu xuan jie
 */
@Getter
public enum FrameworkContext {

    INSTANCE;

    /**
     * 统一时钟
     */
    @Setter
    private IClock clock = OffsetClock.of(0L);

    @Setter
    private int serviceHandleMessageCountPreTick = 1000;
}