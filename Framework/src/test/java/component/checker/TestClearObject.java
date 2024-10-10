package component.checker;

import com.games.framework.component.checker.NonClear;
import lombok.Setter;

/**
 * @author liuxuanjie
 */
@Setter
public class TestClearObject extends AbstractTestClearObject {

    private static final String TEST_STATIC_FINAL = "static final";

    private static String TEST_STATIC = "static";

    @NonClear
    private final String TEST_FINAL = "final";

    private int value;

    private Integer intValue = 1024;

    private Object object = null;

    @Override
    public void init() {
    }

    @Override
    public void clear() {
        super.clear();

        this.value = 0;
        this.intValue = 1024;
        this.object = null;
    }
}
