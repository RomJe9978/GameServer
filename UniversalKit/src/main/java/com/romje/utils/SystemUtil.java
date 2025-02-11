package com.romje.utils;

import com.romje.model.OSType;

/**
 * This util is used to handle system related matters
 *
 * @author liu xuan jie
 */
public final class SystemUtil {

    private SystemUtil() {
    }

    /**
     * 当遇到失败的时候，直接{@code System.exit()}
     *
     * @param correctValue 期望的正确值
     * @param checkValue   当前需要检查的结果
     * @param exitCode     系统退出码
     */
    public static void exitOnFailure(int correctValue, int checkValue, int exitCode) {
        exitOnFailure(correctValue == checkValue, exitCode);
    }

    /**
     * 当遇到失败的时候，直接{@code System.exit()}
     *
     * @param result   布尔结果，{@code false}代表失败
     * @param exitCode 系统退出码
     */
    public static void exitOnFailure(boolean result, int exitCode) {
        if (!result) {
            System.exit(exitCode);
        }
    }

    /**
     * 获取“操作系统”类型
     *
     * @return 不会为{@code null}
     */
    public static OSType getOsType() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OSType.WINDOWS;
        } else if (osName.contains("mac")) {
            return OSType.MAC;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return OSType.LINUX;
        } else {
            return OSType.OTHER;
        }
    }

    /**
     * @return 如果当前操作系统是“Windows”，返回{@code true}
     */
    public static boolean isWindowsOs() {
        return getOsType() == OSType.WINDOWS;
    }

    /**
     * @return 如果当前操作系统不是“Windows”，返回{@code true}
     */
    public static boolean nonWindowsOs() {
        return !isWindowsOs();
    }

    /**
     * @return 如果当前操作系统是“Mac”，返回{@code true}
     */
    public static boolean isMacOs() {
        return getOsType() == OSType.MAC;
    }

    /**
     * @return 如果当前操作系统不是“Mac”，返回{@code true}
     */
    public static boolean nonMacOs() {
        return !isMacOs();
    }

    /**
     * @return 如果当前操作系统是“Linux”，返回{@code true}
     */
    public static boolean isLinuxOs() {
        return getOsType() == OSType.LINUX;
    }

    /**
     * @return 如果当前操作系统不是“Linux”，返回{@code true}
     */
    public static boolean nonLinuxOs() {
        return !isLinuxOs();
    }
}