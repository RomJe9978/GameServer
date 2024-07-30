package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumUnique;

/**
 * @author liu xuan jie
 */
public enum MultiRepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),

    THREE(1, "two"),
    ;

    @EnumUnique
    private final int code;

    @EnumUnique
    private final String describe;

    MultiRepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}