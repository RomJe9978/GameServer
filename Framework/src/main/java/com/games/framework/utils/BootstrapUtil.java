package com.games.framework.utils;

import com.games.framework.log.Log;

/**
 * “启动”时候一些处理的工具封装
 *
 * @author liu xuan jie
 */
public final class BootstrapUtil {

    /**
     * 默认系统退出码
     */
    private static final int DEFAULT_EXIT_CODE = -1;

    private BootstrapUtil() {
    }

    /**
     * {@link #exitOnFailure(boolean, int, String)}
     */
    public static void exitOnFailure(boolean result) {
        exitOnFailure(result, DEFAULT_EXIT_CODE, "check log");
    }

    /**
     * {@link #exitOnFailure(boolean, int, String)}
     */
    public static void exitOnFailure(boolean result, String message) {
        exitOnFailure(result, DEFAULT_EXIT_CODE, message);
    }

    /**
     * 当失败的时候，退出进程。如果成功不进行任何处理。
     *
     * @param result   是否成功，{@code false}代表失败
     * @param exitCode 进程退出码，调用处控制
     * @param message  错误信息,一般通过该描述可以反查出问题的位置即可
     */
    public static void exitOnFailure(boolean result, int exitCode, String message) {
        if (!result) {
            Log.FRAME.error("[Boot] Application boot error: [ {} ]", message);
            System.exit(exitCode);
        }
    }
}