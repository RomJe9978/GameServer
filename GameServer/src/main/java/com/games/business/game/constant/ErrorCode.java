package com.games.business.game.constant;

import com.romje.component.manager.enumlookup.EnumKey;
import com.romje.component.manager.enumlookup.EnumLookup;
import lombok.Getter;

/**
 * 统一管理游戏内错误码
 *
 * <pre>
 *     1.{@code 0}唯一代表“无错误”
 *     2.所有代表“错误”的错误码均采用正数（负数有很多缺点，比如：ProtoBuf中的负数编码）
 *     3.可以采用区间的方式对业务使用错误码进行分割，业务自身更好的统一维护
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
    NON_ERROR(0, "Non Error"),
    COMMON_ERROR(1, "Common Error"),
    PARAM_INVALID(2, "Param Invalid"),
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

    /**
     * @return 如果没有对应错误码，返回{@code null}
     */
    public static ErrorCode valueOf(int code) {
        return EnumLookup.INSTANCE.getEnum(ErrorCode.class, code);
    }

    public final boolean isError() {
        return this != NON_ERROR;
    }

    public static boolean isError(ErrorCode errorCode) {
        return errorCode != NON_ERROR;
    }

    public static boolean isError(int code) {
        return code != NON_ERROR.code;
    }

    public final boolean nonError() {
        return this == NON_ERROR;
    }

    public static boolean nonError(ErrorCode errorCode) {
        return errorCode == NON_ERROR;
    }

    public static boolean nonError(int code) {
        return code == NON_ERROR.code;
    }
}