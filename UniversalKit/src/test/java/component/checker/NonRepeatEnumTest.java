package component.checker;

/**
 * @author liu xuan jie
 */
public enum NonRepeatEnumTest {

    ONE(1),

    TWO(2),

    THREE(3);

    private final int code;

    NonRepeatEnumTest(int code) {
        this.code = code;
    }
}