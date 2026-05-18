package assertions;

import helpers.AssertionHelper;
import org.testng.asserts.SoftAssert;

public final class SoftAssertHelper {

    private SoftAssertHelper() {
    }

    public static SoftAssert get() {
        return AssertionHelper.soft();
    }

    public static void assertAll() {
        AssertionHelper.assertAllSoft();
    }
}
