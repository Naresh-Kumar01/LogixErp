package helpers;

import drivers.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Map;

/**
 * Lightweight keyword-driven executor for hybrid automation.
 * Keywords: OPEN_URL, CLICK, TYPE, WAIT_VISIBLE, ASSERT_TEXT
 */
public final class KeywordEngine {

    private KeywordEngine() {
    }

    public static void execute(Map<String, String> step) {
        String action = step.getOrDefault("action", "").toUpperCase();
        switch (action) {
            case "OPEN_URL" -> DriverFactory.getDriver().get(step.get("value"));
            case "CLICK" -> WaitHelper.waitForClickable(By.xpath(step.get("locator"))).click();
            case "TYPE" -> {
                WebElement el = WaitHelper.waitForVisible(By.xpath(step.get("locator")));
                el.clear();
                el.sendKeys(step.get("value"));
            }
            case "WAIT_VISIBLE" -> WaitHelper.waitForVisible(By.xpath(step.get("locator")));
            case "ASSERT_TEXT" -> AssertionHelper.assertTrue(
                    DriverFactory.getDriver().getPageSource().contains(step.get("value")),
                    "Expected text not found: " + step.get("value"));
            default -> throw new IllegalArgumentException("Unsupported keyword action: " + action);
        }
    }
}
