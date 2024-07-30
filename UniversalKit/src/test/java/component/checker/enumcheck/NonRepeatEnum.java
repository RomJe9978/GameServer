package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumUnique;

/**
 * @author liu xuan jie
 */
public enum NonRepeatEnum {

    ONE(1, "one"),

    TWO(2, "two"),
    ;

    @EnumUnique
    private final int code;
    
    private final String describe;

    NonRepeatEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}