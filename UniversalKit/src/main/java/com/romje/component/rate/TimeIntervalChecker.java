package com.romje.component.rate;

/**
 * 时间间隔检查器（非线程安全）
 *
 * <p>使用者可以设置时间间隔。内部会维护一个{@link #lastTimestamp}，记录
 * “上一次”的时间戳，以此来检查“指定的时间戳”是否与“上一次”满足间隔时间。
 *
 * @author liu xuan jie
 */
public class TimeIntervalChecker {

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

    private TimeIntervalChecker(long interval) {
        this.interval = (interval <= 0) ? DEFAULT_RATE_INTERVAL_MILLIS : interval;
    }

    /**
     * @param interval 间隔时间：ms
     */
    public static TimeIntervalChecker newInstance(long interval) {
        return new TimeIntervalChecker(interval);
    }

    /**
     * @param interval       间隔时间：ms
     * @param startTimeMills 开始时间戳：ms，到达该时间后，满足第一次频率
     */
    public static TimeIntervalChecker newInstance(long interval, long startTimeMills) {
        TimeIntervalChecker timeIntervalChecker = newInstance(interval);
        timeIntervalChecker.lastTimestamp = startTimeMills - timeIntervalChecker.interval;
        return timeIntervalChecker;
    }

    /**
     * 检查时间间隔
     *
     * <p>检查指定的{@code curTimestamp}和记录的上一次的时
     * 间戳的间隔是否满足(大于或等于算作满足)设定的频率间隔
     *
     * @param curTimestamp 使用者指定检查的时间戳：ms
     * @return 满足间隔的时候，会返回{@code true}
     */
    public boolean checkInterval(long curTimestamp) {
        return Math.subtractExact(curTimestamp, this.lastTimestamp) >= this.interval;
    }

    /**
     * 检查时间间隔。如果{@link #checkInterval(long)}满足间隔
     * 要求，则同时更新记录{@code curTimestamp}为“上一次”时间
     *
     * @param curTimestamp 使用者指定检查的时间戳：ms
     * @return 满足间隔的时候，会返回{@code true}
     */
    public boolean checkAndUpdate(long curTimestamp) {
        if (this.checkInterval(curTimestamp)) {
            this.lastTimestamp = curTimestamp;
            return true;
        }
        return false;
    }

    /**
     * @param interval 间隔时间：ms
     */
    public TimeIntervalChecker setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    /**
     * @param timestamp 时间戳：ms
     */
    public TimeIntervalChecker setLastTimestamp(long timestamp) {
        this.lastTimestamp = timestamp;
        return this;
    }
}
