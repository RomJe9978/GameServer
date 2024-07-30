package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumUnique;

/**
 * @author liu xuan jie
 */
public enum MultiNonRepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),

    THREE(3, "three"),
    ;

    @EnumUnique
    private final int code;

    @EnumUnique
    private final String describe;

    MultiNonRepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}