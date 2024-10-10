package component.pool.object;

import com.romje.component.pool.object.DefaultObjectPool;
import com.romje.component.pool.object.IObjectPool;
import com.romje.component.pool.object.ObjectPoolConfig;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class ObjectPoolTest {

    @Test
    public void testDefaultObjectPool() {
        IObjectPool<TestReusableObject> objectPool =
                new DefaultObjectPool<>(ObjectPoolConfig.newInstance(3), new TestReusableObjectFactory());
        assertEquals(0, objectPool.size());
        assertEquals(4, objectPool.getConfig().getMaxCount());
        assertEquals(0, objectPool.getConfig().getMinIdle());
        assertEquals(4, objectPool.getConfig().getMaxIdle());

        TestReusableObject instance1 = objectPool.borrowObject();
        TestReusableObject instance2 = objectPool.borrowObject();
        assertEquals(0, objectPool.size());
        assertTrue(Objects.nonNull(instance1));
        assertTrue(Objects.nonNull(instance2));
        assertNotEquals(instance1, instance2);

        objectPool.returnObject(instance1);
        objectPool.returnObject(instance2);
        assertEquals(2, objectPool.size());

        TestReusableObject instance3 = objectPool.borrowObject();
        assertEquals(1, objectPool.size());
        TestReusableObject instance4 = objectPool.borrowObject();
        assertEquals(0, objectPool.size());
        assertEquals(instance1, instance3);
        assertEquals(instance2, instance4);

        instance3.setValue(100);
        objectPool.returnObject(instance3);
        assertEquals(1, objectPool.size());
        TestReusableObject instance5 = objectPool.borrowObject();
        assertEquals(0, instance5.getValue());
        assertEquals(0, objectPool.size());

        // 非池化对象不允许放入
        TestReusableObject instance6 = new TestReusableObject();
        objectPool.returnObject(instance6);
        assertEquals(0, objectPool.size());
    }
}