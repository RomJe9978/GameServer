package com.romje.component.rate;

/**
 * 时间帧迭代器（非线程安全）
 *
 * <p>使用者可以设置时间间隔。内部会维护一个{@link #lastTimestamp}，记录
 * “上一次”的时间戳，以此来检查“指定的时间戳”是否与“上一次”满足间隔时间。
 *
 * @author liu xuan jie
 */
public class TimeTicker {

    /**
     * 默认间隔时间：ms
     */
    private static final int DEFAULT_RATE_INTERVAL_MILLIS = 100;

    /**
     * “上一次”的时间戳：ms
     */
    private long lastTimestamp;

    /**
     * 频率时间间隔：ms
     */
    private long interval;

    private TimeTicker(long interval) {
        this.interval = (interval <= 0) ? DEFAULT_RATE_INTERVAL_MILLIS : interval;
    }

    /**
     * @param interval 间隔时间：ms
     */
    public static TimeTicker of(long interval) {
        return new TimeTicker(interval);
    }

    /**
     * @param interval       间隔时间：ms
     * @param startTimeMills 开始时间戳：ms，到达该时间后，满足第一次频率
     */
    public static TimeTicker of(long interval, long startTimeMills) {
        TimeTicker timeTicker = of(interval);
        timeTicker.lastTimestamp = startTimeMills - timeTicker.interval;
        return timeTicker;
    }

    /**
     * 检查时间帧
     *
     * <p>检查指定的{@code curTimestamp}和记录的上一次的时
     * 间戳的间隔是否满足(大于或等于算作满足)设定的频率间隔
     *
     * @param millisTimestamp 使用者指定检查的时间戳：ms
     * @return 满足间隔的时候，会返回{@code true}
     */
    public boolean check(long millisTimestamp) {
        return Math.subtractExact(millisTimestamp, this.lastTimestamp) >= this.interval;
    }

    /**
     * 迭代时间帧（检查间隔，满足间隔会自动更新）
     *
     * <p>检查时间间隔。如果{@link #check(long)}满足间隔
     * 要求，则同时更新记录{@code curTimestamp}为“上一次”时间
     *
     * @param millisTimestamp 使用者指定检查的时间戳：ms
     * @return 满足间隔的时候，会返回{@code true}
     */
    public boolean tick(long millisTimestamp) {
        if (this.check(millisTimestamp)) {
            this.lastTimestamp = millisTimestamp;
            return true;
        }
        return false;
    }

    /**
     * @return 下一次周期迭代的时间戳：ms
     */
    public long next() {
        return this.lastTimestamp + this.interval;
    }

    /**
     * 指定时间距离“下一次时间迭代”还有多长时间
     *
     * @param millisTimestamp 使用者指定检查的时间戳：ms
     * @return 时间差值：ms
     */
    public long diffNext(long millisTimestamp) {
        return next() - millisTimestamp;
    }

    /**
     * @param interval 间隔时间：ms
     */
    public TimeTicker setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    /**
     * @param timestamp 时间戳：ms
     */
    public TimeTicker setLastTimestamp(long timestamp) {
        this.lastTimestamp = timestamp;
        return this;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public long getInterval() {
        return interval;
    }
}
