package helpers;

import constants.FrameworkConstants;
import drivers.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtility {

    private ScreenshotUtility() {
    }

    public static String capture(String testName) throws IOException {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IOException("WebDriver is null; cannot capture screenshot");
        }
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new IOException("Driver does not support screenshots");
        }
        FrameworkConstants.SCREENSHOTS_DIR.toFile().mkdirs();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File destination = FrameworkConstants.SCREENSHOTS_DIR
                .resolve(testName + "_" + timestamp + ".png").toFile();
        File source = screenshotDriver.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(source, destination);
        return destination.getAbsolutePath();
    }
}
