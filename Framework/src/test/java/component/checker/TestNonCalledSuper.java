package component.checker;

import lombok.Getter;

/**
 * @author liu xuan jie
 */
@Getter
public class TestNonCalledSuper extends AbstractTestNonClearObject {

    private long testValue;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void clear() {
        this.testValue = 0L;
    }
}