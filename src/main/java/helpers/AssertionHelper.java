package helpers;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public final class AssertionHelper {

    private static final ThreadLocal<SoftAssert> SOFT_ASSERT = ThreadLocal.withInitial(SoftAssert::new);

    private AssertionHelper() {
    }

    public static SoftAssert soft() {
        return SOFT_ASSERT.get();
    }

    public static void assertAllSoft() {
        SOFT_ASSERT.get().assertAll();
        SOFT_ASSERT.remove();
    }

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }
}
