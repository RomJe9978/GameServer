package component.checker.enumcheck;

import com.romje.component.checker.enumcheck.EnumChecker;
import com.romje.component.checker.enumcheck.EnumUnique;
import com.romje.model.BoolResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liu xuan jie
 */
public class EnumCheckerTest {

    @Test
    public void testNonAnnotationEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(NonAnnotationEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonRepeatEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(NonRepeatEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testNonInstanceEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(NonInstanceEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testRepeatEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(RepeatEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertFalse(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testMultiNonRepeatEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(MultiNonRepeatEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertTrue(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }

    @Test
    public void testMultiRepeatEnum() {
        List<Class<? extends Enum<?>>> enumClassList = new ArrayList<>();
        enumClassList.add(MultiRepeatEnum.class);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            assertFalse(boolResult.isSuccess());
        } catch (Exception e) {
            fail("No exceptions should be thrown");
        }
    }
}