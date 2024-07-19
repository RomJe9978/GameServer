package com.romje.constants;

/**
 * 时间单位转换的相关常量定义
 * <p>避免程序中出现大量的“魔数”，减少出错概率
 * <p>命名格式统一，“每天有多少小时”为{@link #HOURS_OF_DAY}
 * <P>注意{@link Integer#MAX_VALUE}常量数值是否越界
 *
 * @author liu xuan jie
 */
public class TimeConst {

    /**
     * 每毫秒多少纳秒
     */
    public final static int NANOS_OF_MILLI = 1000 * 1000;

    /**
     * 每秒多少毫秒
     */
    public final static int MILLIS_OF_SECOND = 1000;

    /**
     * 每分钟多少秒
     */
    public final static int SECONDS_OF_MINUTE = 60;

    /**
     * 每分钟多少毫秒
     */
    public final static int MILLIS_OF_MINUTE = 60 * 1000;

    /**
     * 每小时多少分钟
     */
    public final static int MINUTES_OF_HOUR = 60;

    /**
     * 每小时多少秒
     */
    public final static int SECONDS_OF_HOUR = 60 * 60;

    /**
     * 每小时多少毫秒
     */
    public final static int MILLIS_OF_HOUR = 60 * 60 * 1000;

    /**
     * 每天多少小时
     */
    public final static int HOURS_OF_DAY = 24;

    /**
     * 每天多少分钟
     */
    public final static int MINUTE_OF_DAY = 24 * 60;

    /**
     * 每天多少秒
     */
    public final static int SECONDS_OF_DAY = 24 * 60 * 60;

    /**
     * 每天多少毫秒
     */
    public final static int MILLIS_OF_DAY = 24 * 60 * 60 * 1000;

    /**
     * 每周多少天
     */
    public final static int DAYS_OF_WEEK = 7;

    /**
     * 每周多少小时
     */
    public final static int HOURS_OF_WEEK = 7 * 24;

    /**
     * 每周多少分钟
     */
    public final static int MINUTES_OF_WEEK = 7 * 24 * 60;

    /**
     * 每周多少秒
     */
    public final static int SECONDS_OF_WEEK = 7 * 24 * 60 * 60;

    /**
     * 每周多少毫秒
     */
    public final static int MILLIS_OF_WEEK = 7 * 24 * 60 * 60 * 1000;
}
