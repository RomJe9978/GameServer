package com.romje.component.clock;

/**
 * 时钟上下文
 *
 * @author RomJe
 */
public class ClockContext {
    /**
     * 组合具体的时钟实现
     */
    private static IClock clock;

    /**
     * 自定义设置
     *
     * @param clock 时钟实现类
     */
    public static void setClock(IClock clock) {
        ClockContext.clock = clock;
    }

    /**
     * 获得上下文的时钟
     *
     * @return 如果没有设置，返回{@code Null}
     */
    public static IClock getClock() {
        return ClockContext.clock;
    }
}
