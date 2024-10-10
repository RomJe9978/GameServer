package component.checker;

import com.games.framework.component.checker.NonClear;
import com.romje.component.pool.object.Reusable;

/**
 * @author liuxuanjie
 */
public class AbstractTestNonClearObject implements Reusable {

    private int value;

    @NonClear
    private int testAnnotation;

    @Override
    public void init() {
    }

    @Override
    public void clear() {
    }
}
