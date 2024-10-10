package utils;

import com.romje.component.pool.object.Reusable;
import lombok.Setter;

/**
 * @author liu xuan jie
 */
@Setter
public abstract class AbstractTestByteBuddy implements Reusable {

    protected int parentValue;

    private String parentString;

    @Override
    public void init() {
    }

    @Override
    public void clear() {
        this.parentValue = 0;
        this.parentString = null;
    }
}
