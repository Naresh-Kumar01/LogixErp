package helpers;

import config.ConfigReader;
import drivers.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public final class WaitHelper {

    private WaitHelper() {
    }

    private static Duration explicitTimeout() {
        return Duration.ofSeconds(ConfigReader.getInt("explicit_wait", 20));
    }

    public static WebDriverWait webDriverWait() {
        return new WebDriverWait(DriverFactory.getDriver(), explicitTimeout());
    }

    public static WebElement waitForVisible(By locator) {
        return webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return webDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisible(By locator) {
        return webDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForPageLoad() {
        WebDriver driver = DriverFactory.getDriver();
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(explicitTimeout())
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(Exception.class);
        fluentWait.until((Function<WebDriver, Boolean>) d -> {
            String state = ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").toString();
            return "complete".equals(state);
        });
    }

    public static void clickWithRetry(By locator, int attempts) {
        for (int i = 1; i <= attempts; i++) {
            try {
                waitForClickable(locator).click();
                return;
            } catch (StaleElementReferenceException ex) {
                if (i == attempts) {
                    throw ex;
                }
            }
        }
    }

    public static <T> T waitUntil(ExpectedCondition<T> condition) {
        return webDriverWait().until(condition);
    }
}
