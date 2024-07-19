package com.romje.component.checker;

/**
 * 检查器返回结果类封装
 *
 * @author liu xuan jie
 */
public class CheckResult {

    /**
     * 检查结果
     */
    private final boolean result;

    /**
     * 当检查失败时，包含的一些错误信息
     */
    private final String message;

    private CheckResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * 检查失败，生成描述信息
     *
     * @param message 使用处自定义描述信息
     * @return 不会为{@code null}
     */
    public static CheckResult fail(String message) {
        return new CheckResult(false, message);
    }

    /**
     * 检查成功。不包含描述信息
     *
     * @return 不会为{@code null}
     */
    public static CheckResult success() {
        return new CheckResult(true, null);
    }

    /**
     * 检查成功，包含描述信息
     *
     * @param message 使用处自定义描述信息
     * @return 不会为{@code null}
     */
    public static CheckResult success(String message) {
        return new CheckResult(true, message);
    }

    public boolean result() {
        return result;
    }

    public String message() {
        return message;
    }
}