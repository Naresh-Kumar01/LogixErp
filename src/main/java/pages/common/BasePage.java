package pages.common;

import drivers.DriverFactory;
import helpers.WaitHelper;
import locators.LocatorRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebDriver driver() {
        return driver != null ? driver : DriverFactory.getDriver();
    }

    protected WebElement find(String locatorKey) {
        By[] candidates = LocatorRepository.all(locatorKey);
        if (candidates == null) {
            throw new IllegalArgumentException("Unknown locator: " + locatorKey);
        }
        for (By by : candidates) {
            try {
                return WaitHelper.waitForVisible(by);
            } catch (Exception ignored) {
                // auto-heal: try next candidate
            }
        }
        return WaitHelper.waitForVisible(candidates[0]);
    }

    protected void click(String locatorKey) {
        WaitHelper.waitForClickable(LocatorRepository.resolve(locatorKey)).click();
    }

    protected void type(String locatorKey, String value) {
        WebElement element = find(locatorKey);
        element.clear();
        element.sendKeys(value);
    }
}
