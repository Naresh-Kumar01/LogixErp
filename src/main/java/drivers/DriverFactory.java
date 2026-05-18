package drivers;

import config.ConfigReader;
import config.EnvironmentManager;
import enums.BrowserType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver(String os, String browser) {
        if (DRIVER.get() != null) {
            return;
        }
        BrowserType browserType = BrowserType.from(browser);
        WebDriver driver = EnvironmentManager.isRemoteExecution()
                ? createRemoteDriver(os, browserType)
                : createLocalDriver(browserType);
        configureTimeouts(driver);
        DRIVER.set(driver);
    }

    private static WebDriver createLocalDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(buildChromeOptions());
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver(buildFirefoxOptions());
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver(buildEdgeOptions());
            }
        };
    }

    private static WebDriver createRemoteDriver(String os, BrowserType browserType) {
        String gridUrl = ConfigReader.get("gridHubURL", "http://localhost:4444/wd/hub");
        try {
            return switch (browserType) {
                case CHROME -> new RemoteWebDriver(new URL(gridUrl), buildChromeOptions());
                case FIREFOX -> new RemoteWebDriver(new URL(gridUrl), buildFirefoxOptions());
                case EDGE -> new RemoteWebDriver(new URL(gridUrl), buildEdgeOptions());
            };
        } catch (MalformedURLException e) {
            if (ConfigReader.getBoolean("fallbackToLocal", true)) {
                return createLocalDriver(browserType);
            }
            throw new IllegalStateException("Invalid grid hub URL: " + gridUrl, e);
        }
    }

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--disable-gpu", "--no-sandbox",
                "--disable-dev-shm-usage", "--start-maximized", "--disable-popup-blocking");
        if (EnvironmentManager.isHeadless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        String binary = ConfigReader.get("chromeBinary");
        if (binary != null && !binary.isBlank()) {
            options.setBinary(binary);
        }
        return options;
    }

    private static FirefoxOptions buildFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (EnvironmentManager.isHeadless()) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions buildEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (EnvironmentManager.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit_wait", 10)));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getInt("page_load_timeout", 60)));
        driver.manage().timeouts().scriptTimeout(
                Duration.ofSeconds(ConfigReader.getInt("script_timeout", 30)));
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
