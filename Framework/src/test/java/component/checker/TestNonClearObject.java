package component.checker;

/**
 * @author liuxuanjie
 */
public class TestNonClearObject extends AbstractTestClearObject {

    private int nonClear;

    @Override
    public void init() {
    }

    public void clear() {
        super.clear();
    }
}
