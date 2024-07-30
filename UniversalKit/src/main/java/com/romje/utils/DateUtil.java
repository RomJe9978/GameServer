package com.romje.utils;

import com.romje.constants.DateConst;
import com.romje.constants.StringConst;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * This util is used to handle date.
 *
 * @author liu xuan jie
 */
public final class DateUtil {

    private DateUtil() {
    }

    /**
     * 时间戳转换成默认日期格式的字符串
     * <p>默认格式为{@link DateConst#YYYY_MM_DD_SPACE_HH_MM_SS}
     *
     * @param millisTimestamp 时间戳（毫秒）
     * @return {@link StringConst#BLANK_STRING} if params is invalid
     */
    public static String parseDate(long millisTimestamp) {
        return parseDate(millisTimestamp, ZoneId.systemDefault(), DateConst.YYYY_MM_DD_SPACE_HH_MM_SS);
    }

    /**
     * 时间戳转换成指定日期格式的字符串
     *
     * @param millisTimestamp 时间戳（毫秒）
     * @param datePattern     时间格式，不允许为{@code null}
     * @return {@link StringConst#BLANK_STRING} if params is invalid
     */
    public static String parseDate(long millisTimestamp, String datePattern) {
        return parseDate(millisTimestamp, ZoneId.systemDefault(), datePattern);
    }

    /**
     * 时间戳转换成指定日期格式的字符串
     *
     * @param millisTimestamp 时间戳（毫秒）
     * @param zoneId          时区，不允许为{@code null}
     * @param datePattern     时间格式，不允许为{@code null}
     * @return {@link StringConst#BLANK_STRING} if params is invalid
     */
    public static String parseDate(long millisTimestamp, ZoneId zoneId, String datePattern) {
        if (Objects.isNull(zoneId) || Objects.isNull(datePattern)) {
            return StringConst.BLANK_STRING;
        }

        try {
            Instant instant = Instant.ofEpochMilli(millisTimestamp);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
            return localDateTime.format(DateTimeFormatter.ofPattern(datePattern));
        } catch (Exception e) {
            return StringConst.BLANK_STRING;
        }
    }

    /**
     * 计算两个时区之间的时间差值
     *
     * @param first  第一个时区，不允许为{@code null}
     * @param second 第二个时区，不允许为{@code null}
     * @return {@code null} if param is null
     */
    public static Duration diffZone(ZoneId first, ZoneId second) {
        if (Objects.isNull(first) || Objects.isNull(second)) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now.atZone(first).toInstant(), now.atZone(second).toInstant());
    }
}
