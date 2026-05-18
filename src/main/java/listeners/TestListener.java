package listeners;

import com.aventstack.extentreports.Status;
import drivers.DriverFactory;
import helpers.ScreenshotUtility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentManager;
import utilities.LogUtility;

public class TestListener implements ITestListener {

    private static final org.apache.logging.log4j.Logger LOG = LogUtility.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("Suite started: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.createTest(testName);
        LOG.info("Test started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Test passed");
        }
        LOG.info("Test passed: {}", result.getMethod().getMethodName());
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.FAIL, throwable);
        }
        try {
            if (DriverFactory.getDriver() != null) {
                String path = ScreenshotUtility.capture(testName);
                if (ExtentManager.getTest() != null) {
                    ExtentManager.getTest().addScreenCaptureFromPath(path);
                }
                LOG.error("Screenshot captured: {}", path);
            }
        } catch (Exception ex) {
            LOG.warn("Screenshot capture failed: {}", ex.getMessage());
        }
        LOG.error("Test failed: {}", testName, throwable);
        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, "Test skipped");
        }
        if (result.getThrowable() != null) {
            LOG.warn("Test skipped: {} - {}", result.getMethod().getMethodName(),
                    result.getThrowable().getMessage());
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
        LOG.info("Suite finished: {}", context.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.info("Retry window for: {}", result.getMethod().getMethodName());
    }
}
