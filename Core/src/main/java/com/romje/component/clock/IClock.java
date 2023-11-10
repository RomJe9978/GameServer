package com.romje.component.clock;

import java.time.ZoneId;

/**
 * 时钟接口
 *
 * @author RomJe
 */
public interface IClock {
    /**
     * 将时钟时间更新到指定时间戳
     *
     * @param millisTimestamp 时间戳(毫秒)
     */
    void updateTimeTo(long millisTimestamp);

    /**
     * 获取时钟的“当前毫秒时间戳”
     *
     * @return 时间戳（毫秒）
     */
    long currentTimeMillis();

    /**
     * 获取时钟的“当前秒时间戳”
     *
     * @return 时间戳（秒）
     */
    long secondTimestamp();

    /**
     * 获取时钟的“当前纳秒时间戳”
     *
     * @return 时间戳（毫秒）
     */
    long nanoTime();
}
