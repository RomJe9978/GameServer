package com.romje.utils;

import com.romje.constants.TimeConst;

/**
 * This util is used to handle time.
 *
 * @author liu xuan jie
 */
public class TimeUtil {

    private TimeUtil() {
    }

    /**
     * 将秒转换为毫秒。
     *
     * @param second 秒数
     * @return 对应的毫秒数
     */
    public static long secondMillis(long second) {
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
    public static long secondMillisSafe(long second) {
        return Math.multiplyExact(second, TimeConst.MILLIS_OF_SECOND);
    }

    /**
     * 将分钟转换为毫秒。
     *
     * @param minute 分钟数
     * @return 对应的毫秒数
     */
    public static long minuteMillis(long minute) {
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
    public static long minuteMillisSafe(long minute) {
        return Math.multiplyExact(minute, TimeConst.MILLIS_OF_MINUTE);
    }

    /**
     * 将小时转换为毫秒。
     *
     * @param hour 小时数
     * @return 对应的毫秒数
     */
    public static long hourMillis(long hour) {
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
    public static long hourMillisSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.MILLIS_OF_HOUR);
    }

    /**
     * 将天转换为毫秒。
     *
     * @param day 天数
     * @return 对应的毫秒数
     */
    public static long dayMillis(long day) {
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
    public static long dayMillisSafe(long day) {
        return Math.multiplyExact(day, TimeConst.MILLIS_OF_DAY);
    }

    /**
     * 将周转换为毫秒。
     *
     * @param week 周数
     * @return 对应的毫秒数
     */
    public static long weekMillis(long week) {
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
    public static long weekMillisSafe(long week) {
        return Math.multiplyExact(week, TimeConst.MILLIS_OF_WEEK);
    }

    /**
     * 将秒转换为分钟。
     *
     * @param second 秒数
     * @return 对应的分钟数
     */
    public static long minuteSeconds(long second) {
        return second * TimeConst.SECONDS_OF_MINUTE;
    }

    /**
     * 安全地将秒转换为分钟。
     * 使用{@link Math#multiplyExact(long, int)}检查溢出。
     *
     * @param second 秒数
     * @return 对应的分钟数
     * @throws ArithmeticException 如果乘法结果溢出
     */
    public static long minuteSecondsSafe(long second) {
        return Math.multiplyExact(second, TimeConst.SECONDS_OF_MINUTE);
    }

    /**
     * 将小时转换为秒。
     *
     * @param hour 小时数
     * @return 对应的秒数
     */
    public static long hourSeconds(long hour) {
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
    public static long hourSecondsSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.SECONDS_OF_HOUR);
    }

    /**
     * 将天转换为秒。
     *
     * @param day 天数
     * @return 对应的秒数
     */
    public static long daySeconds(long day) {
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
    public static long daySecondsSafe(long day) {
        return Math.multiplyExact(day, TimeConst.SECONDS_OF_DAY);
    }

    /**
     * 将周转换为秒。
     *
     * @param week 周数
     * @return 对应的秒数
     */
    public static long weekSeconds(long week) {
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
    public static long weekSecondsSafe(long week) {
        return Math.multiplyExact(week, TimeConst.SECONDS_OF_WEEK);
    }

    /**
     * 将小时转换为分钟。
     *
     * @param hour 小时数
     * @return 对应的分钟数
     */
    public static long hourMinutes(long hour) {
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
    public static long hourMinutesSafe(long hour) {
        return Math.multiplyExact(hour, TimeConst.MINUTES_OF_HOUR);
    }

    /**
     * 将天转换为分钟。
     *
     * @param day 天数
     * @return 对应的分钟数
     */
    public static long dayMinutes(long day) {
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
    public static long dayMinutesSafe(long day) {
        return Math.multiplyExact(day, TimeConst.MINUTE_OF_DAY);
    }

    /**
     * 将周转换为分钟。
     *
     * @param week 周数
     * @return 对应的分钟数
     */
    public static long weekMinutes(long week) {
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
    public static long weekMinutesSafe(long week) {
        return Math.multiplyExact(week, TimeConst.MINUTES_OF_WEEK);
    }

    /**
     * 将天转换为小时。
     *
     * @param day 天数
     * @return 对应的小时数
     */
    public static long dayHours(long day) {
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
    public static long dayHoursSafe(long day) {
        return Math.multiplyExact(day, TimeConst.HOURS_OF_DAY);
    }

    /**
     * 将周转换为小时。
     *
     * @param week 周数
     * @return 对应的小时数
     */
    public static long weekHours(long week) {
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
    public static long weekHoursSafe(long week) {
        return Math.multiplyExact(week, TimeConst.HOURS_OF_WEEK);
    }

    /**
     * 将周转换为天。
     *
     * @param week 周数
     * @return 对应的天数
     */
    public static long weekDays(long week) {
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
    public static long weekDaysSafe(long week) {
        return Math.multiplyExact(week, TimeConst.DAYS_OF_WEEK);
    }
}