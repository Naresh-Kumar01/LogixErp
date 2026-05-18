package base;

import config.ConfigReader;
import config.EnvironmentManager;
import drivers.DriverFactory;
import enums.UserRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utilities.LogUtility;
import utilities.VideoRecorder;

import java.util.Properties;

/**
 * Enterprise base test – thread-safe driver, config, logging, and video hooks.
 */
public abstract class BaseTest {

    protected Logger logger;
    protected Properties config;

    @Parameters({"os", "browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("Windows") String os, @Optional("chrome") String browser) {
        logger = LogUtility.getLogger(getClass());
        config = ConfigReader.getAll();
        if (DriverFactory.getDriver() == null) {
            DriverFactory.initDriver(os, browser);
            WebDriver driver = DriverFactory.getDriver();
            driver.manage().deleteAllCookies();
            driver.get(EnvironmentManager.getAppUrl());
            driver.manage().window().maximize();
        }
        if (ConfigReader.getBoolean("video.recording.enabled", false)) {
            try {
                VideoRecorder.startRecording(getClass().getSimpleName(), VideoRecorder.getDefaultVideoDir());
            } catch (Exception ex) {
                logger.warn("Video recording not started: {}", ex.getMessage());
            }
        }
        logger.info("Test setup complete for {}", getClass().getSimpleName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (VideoRecorder.isRecording()) {
                String path = VideoRecorder.stopRecording();
                logger.info("Video saved: {}", path);
            }
        } catch (Exception ex) {
            logger.warn("Video stop failed: {}", ex.getMessage());
        }
        DriverFactory.quitDriver();
        logger.info("Driver closed for {}", getClass().getSimpleName());
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected String config(String key) {
        return ConfigReader.get(key);
    }

    protected String config(String key, String defaultValue) {
        return ConfigReader.get(key, defaultValue);
    }

    protected String credentialsForRole(UserRole role) {
        String user = ConfigReader.get(role.configUsernameKey(), ConfigReader.get("username"));
        String pass = ConfigReader.get(role.configPasswordKey(), ConfigReader.get("password"));
        return user + ":" + pass;
    }

    protected String randomAlpha(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    protected String randomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    protected String randomAlphanumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
