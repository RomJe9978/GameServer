package component.proxy.enumproxy;

import com.romje.component.manager.enumlookup.EnumKey;

/**
 * @author liu xuan jie
 */
public enum RepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),

    THREE(1, "three"),
    ;

    @EnumKey
    private final int code;

    private final String describe;

    RepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}