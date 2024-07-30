package com.romje.model;

/**
 * "布尔类型"返回结果封装
 * <p>聚合布尔结果，描述信息，附加数据。使用的地方自行决定是否赋值
 *
 * @author liu xuan jie
 */
public class BoolResult {

    /**
     * 结果表示，逻辑调用处自行决定
     */
    private final boolean result;

    /**
     * 描述信息，逻辑调用处自行决定
     */
    private final String message;

    /**
     * 附加传递数据，逻辑调用处自行决定
     */
    private final Object data;

    private BoolResult(boolean result, String message, Object data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static BoolResult create(boolean result, String message, Object data) {
        return new BoolResult(result, message, data);
    }

    /**
     * {@code result == false}
     */
    public static BoolResult fail() {
        return create(false, null, null);
    }

    /**
     * {@code result == false}，使用描述信息
     */
    public static BoolResult fail(String message) {
        return create(false, message, null);
    }

    /**
     * {@code result == false}，使用描述信息，使用附加数据
     */
    public static BoolResult fail(String message, Object data) {
        return create(false, message, data);
    }

    /**
     * {@code result == true}
     */
    public static BoolResult success() {
        return create(true, null, null);
    }

    /**
     * {@code result == true}，使用描述信息
     */
    public static BoolResult success(String message) {
        return create(true, message, null);
    }

    /**
     * {@code result == true}，使用描述信息，使用附加数据
     */
    public static BoolResult success(String message, Object data) {
        return create(true, message, data);
    }

    public boolean isSuccess() {
        return this.result;
    }

    public boolean isFail() {
        return !this.result;
    }

    public String message() {
        return this.message;
    }

    public Object data() {
        return this.data;
    }

    @Override
    public String toString() {
        return "BoolResult{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}