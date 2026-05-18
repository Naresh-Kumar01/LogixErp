package utilities;

import org.openqa.selenium.WebDriver;

/**
 * Legacy delegate – use {@link drivers.DriverFactory} in new code.
 */
@Deprecated
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        return drivers.DriverFactory.getDriver();
    }

    public static void initDriver(String os, String browser) {
        drivers.DriverFactory.initDriver(os, browser);
    }

    public static void quitDriver() {
        drivers.DriverFactory.quitDriver();
    }
}
