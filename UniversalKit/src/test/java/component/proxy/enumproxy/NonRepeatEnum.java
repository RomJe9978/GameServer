package component.proxy.enumproxy;

import com.romje.component.manager.enumlookup.EnumKey;

/**
 * @author liu xuan jie
 */
public enum NonRepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),
    ;

    @EnumKey
    private final int code;

    private final String describe;

    NonRepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}