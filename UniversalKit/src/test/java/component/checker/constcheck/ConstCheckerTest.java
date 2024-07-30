package component.checker.constcheck;

import com.romje.component.checker.constcheck.ConstChecker;
import com.romje.model.BoolResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class ConstCheckerTest {

    @Test
    public void testNonAnnotation() {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(NonAnnotationConst.class);

        try {
            BoolResult boolResult = ConstChecker.checkFieldUnique(classList);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonField() {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(NonFieldConst.class);

        try {
            BoolResult boolResult = ConstChecker.checkFieldUnique(classList);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonRepeat() {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(NonRepeatConst.class);

        try {
            BoolResult boolResult = ConstChecker.checkFieldUnique(classList);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testRepeat() {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(RepeatConst.class);

        try {
            BoolResult boolResult = ConstChecker.checkFieldUnique(classList);
            assertFalse(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }
}