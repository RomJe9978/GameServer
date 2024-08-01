package component.proxy.enumproxy;

import com.romje.component.proxy.enumproxy.EnumProxy;
import com.romje.model.BoolResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class EnumProxyTest {

    @Test
    public void testNonAnnotationEnum() {
        try {
            List<Class<? extends Enum<?>>> enumClassList = Collections.singletonList(NonAnnotationEnum.class);
            BoolResult boolResult = EnumProxy.INSTANCE.registerEnums(enumClassList);
            assertTrue(boolResult.isSuccess());

            // 没有使用注解，所以不在代理类管理范围内
            NonAnnotationEnum instance = EnumProxy.INSTANCE.getEnum(NonAnnotationEnum.class, 1);
            assertTrue(Objects.isNull(instance));

            // 没在管理范围内，所以代理中的size应该是0
            assertEquals(0, EnumProxy.INSTANCE.sizeOf(NonAnnotationEnum.class));
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonInstanceEnum() {
        try {
            List<Class<? extends Enum<?>>> enumClassList = Collections.singletonList(NonInstanceEnum.class);
            BoolResult boolResult = EnumProxy.INSTANCE.registerEnums(enumClassList);
            assertTrue(boolResult.isSuccess());

            // 没有实例，所以获取结果也应该是null
            NonInstanceEnum instance = EnumProxy.INSTANCE.getEnum(NonInstanceEnum.class, 1);
            assertTrue(Objects.isNull(instance));

            // 没有任何实例，所以代理中的size应该是0
            assertEquals(0, EnumProxy.INSTANCE.sizeOf(NonInstanceEnum.class));
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonRepeatEnum() {
        try {
            List<Class<? extends Enum<?>>> enumClassList = Collections.singletonList(NonRepeatEnum.class);
            BoolResult boolResult = EnumProxy.INSTANCE.registerEnums(enumClassList);
            assertTrue(boolResult.isSuccess());

            // 有实例了，应该可以成功获取
            NonRepeatEnum instance = EnumProxy.INSTANCE.getEnum(NonRepeatEnum.class, 1);
            assertTrue(Objects.nonNull(instance));
            assertEquals(instance, NonRepeatEnum.ONE);

            // size也应该是真正的实际数量
            assertEquals(2, EnumProxy.INSTANCE.sizeOf(NonRepeatEnum.class));
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testRepeatEnum() {
        try {
            List<Class<? extends Enum<?>>> enumClassList = Collections.singletonList(RepeatEnum.class);
            BoolResult boolResult = EnumProxy.INSTANCE.registerEnums(enumClassList);
            // 有重复，应该是错误
            assertFalse(boolResult.isSuccess());

            // 注册错误，所以也应该获取不到
            RepeatEnum instance = EnumProxy.INSTANCE.getEnum(RepeatEnum.class, 1);
            assertTrue(Objects.isNull(instance));

            // 注册错误，所以代理中的size也是0
            assertEquals(0, EnumProxy.INSTANCE.sizeOf(RepeatEnum.class));
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }
}