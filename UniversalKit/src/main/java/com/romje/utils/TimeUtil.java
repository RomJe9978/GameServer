package com.romje.utils;

import com.romje.constants.TimeConst;

/**
 * This util is used to handle time.
 *
 * @author liu xuan jie
 */
public final class TimeUtil {

    private TimeUtil() {
    }

    /**
     * 将秒转换为毫秒。
     *
     * @param second 秒数
     * @return 对应的毫秒数
     */
    public static long millisOfSecond(long second) {
        return second * TimeConst.MILLIS_OF_SECOND;
    }

    /**
     * 安全地将秒转换为毫秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param second 秒数
     * @return 对应的毫秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long millisOfSecondSafe(long second) {
        return Math.multiplyExact(second, TimeConst.MILLIS_OF_SECOND);
    }

    /**
     * 将分钟转换为毫秒。
     *
     * @param minute 分钟数
     * @return 对应的毫秒数
     */
    public static long millisOfMinute(long minute) {
        return minute * TimeConst.MILLIS_OF_MINUTE;
    }

    /**
     * 安全地将分钟转换为毫秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param minute 分钟数
     * @return 对应的毫秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long millisOfMinuteSafe(long minute) {
        return Math.multiplyExact(minute, TimeConst.MILLIS_OF_MINUTE);
    }

    /**
     * 将小时转换为毫秒。
     *
     * @param hour 小时数
     * @return 对应的毫秒数
     */
    public static long millisOfHour(long hour) {
        return hour * TimeConst.MILLIS_OF_HOUR;
    }

    /**
     * 安全地将小时转换为毫秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param hour 小时数
     * @return 对应的毫秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long millisOfHourSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.MILLIS_OF_HOUR);
    }

    /**
     * 将天转换为毫秒。
     *
     * @param day 天数
     * @return 对应的毫秒数
     */
    public static long millisOfDay(long day) {
        return day * TimeConst.MILLIS_OF_DAY;
    }

    /**
     * 安全地将天转换为毫秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param day 天数
     * @return 对应的毫秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long millisOfDaySafe(long day) {
        return Math.multiplyExact(day, TimeConst.MILLIS_OF_DAY);
    }

    /**
     * 将周转换为毫秒。
     *
     * @param week 周数
     * @return 对应的毫秒数
     */
    public static long millisOfWeek(long week) {
        return week * TimeConst.MILLIS_OF_WEEK;
    }

    /**
     * 安全地将周转换为毫秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param week 周数
     * @return 对应的毫秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long millisOfWeekSafe(long week) {
        return Math.multiplyExact(week, TimeConst.MILLIS_OF_WEEK);
    }

    /**
     * 将分钟转换为秒。
     *
     * @param minute 分钟数
     * @return 对应的秒数
     */
    public static long secondsOfMinute(long minute) {
        return minute * TimeConst.SECONDS_OF_MINUTE;
    }

    /**
     * 安全地将分钟转换为秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param minute 分钟数
     * @return 对应的秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long secondsOfMinuteSafe(long minute) {
        return Math.multiplyExact(minute, TimeConst.SECONDS_OF_MINUTE);
    }

    /**
     * 将小时转换为秒。
     *
     * @param hour 小时数
     * @return 对应的秒数
     */
    public static long secondsOfHour(long hour) {
        return hour * TimeConst.SECONDS_OF_HOUR;
    }

    /**
     * 安全地将小时转换为秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param hour 小时数
     * @return 对应的秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long secondsOfHourSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.SECONDS_OF_HOUR);
    }

    /**
     * 将天转换为秒。
     *
     * @param day 天数
     * @return 对应的秒数
     */
    public static long secondsOfDay(long day) {
        return day * TimeConst.SECONDS_OF_DAY;
    }

    /**
     * 安全地将天转换为秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param day 天数
     * @return 对应的秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long secondsOfDaySafe(long day) {
        return Math.multiplyExact(day, TimeConst.SECONDS_OF_DAY);
    }

    /**
     * 将周转换为秒。
     *
     * @param week 周数
     * @return 对应的秒数
     */
    public static long secondsOfWeek(long week) {
        return week * TimeConst.SECONDS_OF_WEEK;
    }

    /**
     * 安全地将周转换为秒。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param week 周数
     * @return 对应的秒数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long secondsOfWeekSafe(long week) {
        return Math.multiplyExact(week, TimeConst.SECONDS_OF_WEEK);
    }

    /**
     * 将小时转换为分钟。
     *
     * @param hour 小时数
     * @return 对应的分钟数
     */
    public static long minutesOfHour(long hour) {
        return hour * TimeConst.MINUTES_OF_HOUR;
    }

    /**
     * 安全地将小时转换为分钟。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param hour 小时数
     * @return 对应的分钟数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long minutesOfHourSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.MINUTES_OF_HOUR);
    }

    /**
     * 将天转换为分钟。
     *
     * @param day 天数
     * @return 对应的分钟数
     */
    public static long minutesOfDay(long day) {
        return day * TimeConst.MINUTE_OF_DAY;
    }

    /**
     * 安全地将天转换为分钟。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param day 天数
     * @return 对应的分钟数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long minutesOfDaySafe(long day) {
        return Math.multiplyExact(day, TimeConst.MINUTE_OF_DAY);
    }

    /**
     * 将周转换为分钟。
     *
     * @param week 周数
     * @return 对应的分钟数
     */
    public static long minutesOfWeek(long week) {
        return week * TimeConst.MINUTES_OF_WEEK;
    }

    /**
     * 安全地将周转换为分钟。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param week 周数
     * @return 对应的分钟数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long minutesOfWeekSafe(long week) {
        return Math.multiplyExact(week, TimeConst.MINUTES_OF_WEEK);
    }

    /**
     * 将天转换为小时。
     *
     * @param day 天数
     * @return 对应的小时数
     */
    public static long hoursOfDay(long day) {
        return day * TimeConst.HOURS_OF_DAY;
    }

    /**
     * 安全地将天转换为小时。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param day 天数
     * @return 对应的小时数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long hoursOfDaySafe(long day) {
        return Math.multiplyExact(day, TimeConst.HOURS_OF_DAY);
    }

    /**
     * 将周转换为小时。
     *
     * @param week 周数
     * @return 对应的小时数
     */
    public static long hoursOfWeek(long week) {
        return week * TimeConst.HOURS_OF_WEEK;
    }

    /**
     * 安全地将周转换为小时。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param week 周数
     * @return 对应的小时数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long hoursOfWeekSafe(long week) {
        return Math.multiplyExact(week, TimeConst.HOURS_OF_WEEK);
    }

    /**
     * 将周转换为天。
     *
     * @param week 周数
     * @return 对应的天数
     */
    public static long daysOfWeek(long week) {
        return week * TimeConst.DAYS_OF_WEEK;
    }

    /**
     * 安全地将周转换为天。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param week 周数
     * @return 对应的天数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long daysOfWeekSafe(long week) {
        return Math.multiplyExact(week, TimeConst.DAYS_OF_WEEK);
    }
}