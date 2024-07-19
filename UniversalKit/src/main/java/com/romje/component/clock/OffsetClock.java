package com.romje.component.clock;

import com.romje.constants.TimeConst;

/**
 * 偏移量时钟：可通过调整偏移量进行时间控制
 *
 * <p>例如，服务器运行在中国上海，游戏区为美国洛杉矶
 * 那么，设置时区差值，以保证服务器时间为美国玩家时间
 * （此处只是举例而已，实际海外服务器都在海外当地）
 *
 * <p>例如，服务器启动后，想要修改服务器时间，而又不
 * 想或者不能修改其物理机器的系统时间，则调整该值即可
 *
 * @author liu xuan jie
 */
public class OffsetClock implements IClock {

    /**
     * 时间偏移量(毫秒)，相对于“系统时间”
     */
    private long offsetMillis;

    private OffsetClock(long offsetMillis) {
        this.offsetMillis = offsetMillis;
    }

    /**
     * @param offsetMillis 偏移的毫秒数
     * @return 一个新的对象实例
     */
    public static OffsetClock newInstance(long offsetMillis) {
        return new OffsetClock(offsetMillis);
    }

    @Override
    public void updateTimeTo(long millisTimestamp) {
        long diffMillis = Math.subtractExact(millisTimestamp, this.currentTimeMillis());
        this.offsetMillis += diffMillis;
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis() + this.offsetMillis;
    }

    @Override
    public long secondTimestamp() {
        return currentTimeMillis() / TimeConst.MILLIS_OF_SECOND;
    }

    @Override
    public long nanoTime() {
        return System.nanoTime() + this.offsetMillis * TimeConst.NANOS_OF_MILLI;
    }

    @Override
    public String toString() {
        return "DefaultClock{" +
                "offsetMillis=" + offsetMillis +
                '}';
    }
}
