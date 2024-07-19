package component.checker;

import com.romje.component.checker.CheckResult;
import com.romje.component.checker.EnumChecker;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class EnumCheckerTest {

    @Test
    public void testCheckRepeat() {
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        map.put(1, List.of(1, 2, 3));
        map.put(2, List.of(4, 5, 6));
        System.out.println(map.toString());

        try {
            assertTrue(EnumChecker.hasRepeat(HasRepeatEnumTest.class, "code"));
            assertFalse(EnumChecker.hasRepeat(NonRepeatEnumTest.class, "code"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            CheckResult checkResult = EnumChecker.checkRepeat(HasRepeatEnumTest.class, "code");
            assertFalse(checkResult.result());
            assertEquals(checkResult.message(), "Enum repeat info: {2=[TWO, THREE]}");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}