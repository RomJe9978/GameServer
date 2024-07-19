package com.games.game.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 统一管理游戏内错误码
 *
 * <pre>
 *     1.{@code 0}唯一代表“无错误”
 *     2.所有代表“错误”的错误码均采用正数（负数有很多缺点，比如：ProtoBuf中的负数编码）
 *     3.所有业务模块注意区分自己模块的错误码区间
 *     4.应该有错误码重复性检查机制，避免重复错误码带来的BUG
 * </pre>
 *
 * @author liu xuan jie
 */
public enum ErrorCode {

    /**
     * 系统错误码【0，1000】
     */
    NON_ERROR(0, "non error"),
    COMMON_ERROR(1, "common error"),
    PARAM_INVALID(2, "参数无效"),
    ;

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误码简单描述文本
     * 不允许为{@code null}，减少使用处的空指针检查
     * 如果没有描述，请使用{@link com.romje.constants.StringConst#BLANK_STRING}
     */
    private final String describe;

    // todo:后续迁移出去，做成统一工具，不然每次都要实现
    private static final Map<Integer, ErrorCode> codeMap;

    static {
        ErrorCode[] codes = ErrorCode.values();
        codeMap = new HashMap<>(codes.length);
        for (ErrorCode errorCode : codes) {
            if (errorCode.code < 0 || Objects.isNull(errorCode.describe)) {
                throw new IllegalArgumentException("非法的错误码：" + errorCode.code + "，" + errorCode.describe);
            }

            ErrorCode oldValue = codeMap.put(errorCode.code, errorCode);
            if (Objects.nonNull(oldValue)) {
                throw new IllegalArgumentException("重复的错误码：" + oldValue + "，" + errorCode);
            }
        }
    }

    ErrorCode(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public final boolean isError() {
        return this != NON_ERROR;
    }

    public static boolean isError(int code) {
        return code != NON_ERROR.code;
    }

    public final boolean nonError() {
        return this == NON_ERROR;
    }

    public static boolean nonError(int code) {
        return code == NON_ERROR.code;
    }

    /**
     * @return 当code不存在时，返回通用错误，不会为{@code null}
     */
    public static ErrorCode valueOf(int code) {
        return codeMap.getOrDefault(code, COMMON_ERROR);
    }
}