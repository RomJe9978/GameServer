package component.checker;

import com.romje.component.checker.CheckResult;
import com.romje.component.checker.EnumChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class EnumCheckerTest {

    @Test
    public void testCheckRepeat() {
        try {
            CheckResult checkResult = EnumChecker.checkRepeat(HasRepeatEnumTest.class, "code");
            assertFalse(checkResult.result());
            assertEquals(checkResult.message(), "Enum repeat info: {2=[TWO, THREE]}");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}