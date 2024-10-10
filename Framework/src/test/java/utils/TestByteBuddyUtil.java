package utils;

import com.games.framework.utils.ByteBuddyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

/**
 * @author liu xuan jie
 */
public class TestByteBuddyUtil {

    @Test
    public void testListFieldInMethod() throws IOException {
        Set<String> resultSet = ByteBuddyUtil.listFieldsInMethod(AbstractTestByteBuddy.class, "clear");
        Assertions.assertEquals(resultSet.toString(), "[parentString, parentValue]");

        resultSet = ByteBuddyUtil.listFieldsInMethod(TestByteBuddy.class, "clear");
        Assertions.assertEquals(resultSet.toString(), "[intValue, testObject, value, object]");

        boolean isCalled = ByteBuddyUtil.checkSuperMethodCall(TestByteBuddy.class, AbstractTestByteBuddy.class, "clear");
        Assertions.assertFalse(isCalled);
    }
}
