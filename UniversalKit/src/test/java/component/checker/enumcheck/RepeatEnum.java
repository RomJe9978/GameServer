package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumUnique;

/**
 * @author liu xuan jie
 */
public enum RepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),

    THREE(1, "three"),
    ;

    @EnumUnique
    private final int code;

    private final String describe;

    RepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}