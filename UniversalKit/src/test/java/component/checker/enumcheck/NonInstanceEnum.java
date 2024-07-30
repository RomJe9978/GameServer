package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumUnique;

/**
 * @author liu xuan jie
 */
public enum NonInstanceEnum {
    ;

    @EnumUnique
    private final int code;

    private final String describe;

    NonInstanceEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}