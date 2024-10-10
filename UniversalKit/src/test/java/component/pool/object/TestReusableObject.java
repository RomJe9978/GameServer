package component.pool.object;

import com.romje.component.pool.object.Reusable;

/**
 * 测试可复用对象
 *
 * @author liu xuan jie
 */
public class TestReusableObject implements Reusable {

    private int value;

    @Override
    public void init() {
    }

    @Override
    public void clear() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}