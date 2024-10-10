package component.checker;

import com.games.framework.component.checker.CleanUpChecker;
import com.romje.model.BoolResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxuanjie
 */
public class TestCleanUpChecker {

    @Test
    public void checkCleanUp() throws IOException {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(AbstractTestClearObject.class);
        classList.add(TestClearObject.class);

        BoolResult boolResult = CleanUpChecker.checkCleanUp(classList, "clear");
        Assertions.assertTrue(boolResult.isSuccess());

        // 检查有不清理的字段
        classList.add(TestNonClearObject.class);
        boolResult = CleanUpChecker.checkCleanUp(classList, "clear");
        Assertions.assertFalse(boolResult.isSuccess());
        System.out.println(boolResult.message());
        Assertions.assertEquals(boolResult.message(), "component.checker.TestNonClearObject has non clear field:[nonClear]");

        // 检查抽象类中有不清理的字段,同时验证NonClear注解的作用
        classList.remove(TestNonClearObject.class);
        classList.add(AbstractTestNonClearObject.class);
        boolResult = CleanUpChecker.checkCleanUp(classList, "clear");
        Assertions.assertFalse(boolResult.isSuccess());
        System.out.println(boolResult.message());
        Assertions.assertEquals(boolResult.message(), "component.checker.AbstractTestNonClearObject has non clear field:[value]");

        // 检查子类不调用父类的清理方法
        classList.remove(AbstractTestNonClearObject.class);
        classList.add(TestNonCalledSuper.class);
        boolResult = CleanUpChecker.checkCleanUp(classList, "clear");
        Assertions.assertFalse(boolResult.isSuccess());
        System.out.println(boolResult.message());
        Assertions.assertEquals(boolResult.message(), "component.checker.TestNonCalledSuper non called super clean up method!");
    }
}
