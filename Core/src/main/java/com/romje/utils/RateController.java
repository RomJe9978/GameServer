package com.romje.utils;

/**
 * 频率控制器（非线程安全）
 * <p>控制器要求使用者设置一个频率，然后聚合检查两次时间
 * 是否满足频率要求，以及在满足频率时同步更新时间的功能。
 *
 * @author RomJe
 */
public class RateController {
    /**
     * 默认间隔时间：ms
     */
    private final static int DEFAULT_RATE_INTERVAL_MILLIS = 100;

    /**
     * 上一次的时间戳：ms
     */
    private long lastTimestamp;

    /**
     * 相邻两次之间的时间间隔：ms
     */
    private final int interval;

    public RateController(int interval) {
        this.interval = (interval <= 0) ? DEFAULT_RATE_INTERVAL_MILLIS : interval;
    }

    /**
     * 检查频率
     * <p>检查{@code curTimestamp}和记录的上一次的时
     * 间戳的间隔是否满足(大于或等于算作满足)设定的频率间隔
     *
     * @param curTimestamp 新一次的时间戳：ms
     * @return 新旧两次间隔满足频率要求时，会返回{@code true}
     */
    public boolean checkRate(long curTimestamp) {
        return Math.subtractExact(curTimestamp, this.lastTimestamp) >= this.interval;
    }

    /**
     * 检查频率，并在满足频率要求时更新
     *
     * @param curTimestamp 新一次的时间戳：ms
     * @return 新旧两次间隔满足频率要求时，会返回{@code true}
     */
    public boolean checkAndUpdate(long curTimestamp) {
        if (this.checkRate(curTimestamp)) {
            this.setLastTimestamp(curTimestamp);
            return true;
        }
        return false;
    }

    /**
     * @param timestamp 时间戳：ms
     */
    public void setLastTimestamp(long timestamp) {
        this.lastTimestamp = timestamp;
    }
}
