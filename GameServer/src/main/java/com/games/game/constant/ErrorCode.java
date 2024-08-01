package com.games.game.constant;

import com.romje.component.proxy.enumproxy.EnumKey;
import lombok.Getter;

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
@Getter
public enum ErrorCode {

    /**
     * 系统错误码【0，1000】
     */
    NON_ERROR(0, "没有错误"),
    COMMON_ERROR(1, "通用错误"),
    PARAM_INVALID(2, "参数无效"),
    ;

    /**
     * 错误码
     */
    @EnumKey
    private final int code;

    /**
     * 错误码简单描述文本，不允许为{@code null}，减少使用处的空指针检查
     * 如果没有描述，请使用{@link com.romje.constants.StringConst#BLANK_STRING}
     */
    private final String describe;

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
}