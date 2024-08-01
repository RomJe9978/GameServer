package component.proxy.enumproxy;

/**
 * @author liu xuan jie
 */
public enum NonAnnotationEnum {

    ONE(1, "one"),

    TWO(2, "two"),
    ;

    private final int code;

    private final String describe;

    NonAnnotationEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }
}