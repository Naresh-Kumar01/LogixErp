package retry;

import config.ConfigReader;
import constants.FrameworkConstants;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (!ConfigReader.getBoolean("retry.failed.tests", true)) {
            return false;
        }
        int maxRetry = ConfigReader.getInt("retry.count", FrameworkConstants.DEFAULT_RETRY_COUNT);
        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }
}
