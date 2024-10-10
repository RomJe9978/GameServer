package component.checker;

import com.romje.component.pool.object.Reusable;

/**
 * @author liuxuanjie
 */
public abstract class AbstractTestClearObject implements Reusable {

    protected int parentValue;

    private String parentString;

    @Override
    public void clear() {
        this.parentValue = 0;
        this.parentString = null;
    }
}
