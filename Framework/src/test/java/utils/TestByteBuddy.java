package utils;

import lombok.Setter;

/**
 * @author liu xuan jie
 */
@Setter
public class TestByteBuddy extends AbstractTestByteBuddy {

    private static final String TEST_STATIC_FINAL = "static final";

    private static String TEST_STATIC = "static";

    private final String TEST_FINAL = "final";

    private int value;

    private Integer intValue = 1024;

    private Object object = null;

    private TestByteBuddy testObject = null;

    @Override
    public void clear() {
//        super.clear();

        this.value = 0;
        this.intValue = 1024;
        this.object = null;
        this.testObject = null;
    }
}