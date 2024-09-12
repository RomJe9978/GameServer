package component.proxy.enumproxy;

import com.romje.component.manager.enumlookup.EnumKey;

/**
 * @author liu xuan jie
 */
public enum NonInstanceEnum {
    ;

    @EnumKey
    private final int code;

    private final String describe;

    NonInstanceEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}