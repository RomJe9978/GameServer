package component.pool.object;

import com.romje.component.pool.object.IObjectFactory;

/**
 * @author liu xuan jie
 */
public class TestReusableObjectFactory implements IObjectFactory<TestReusableObject> {

    @Override
    public TestReusableObject create() {
        return new TestReusableObject();
    }
}