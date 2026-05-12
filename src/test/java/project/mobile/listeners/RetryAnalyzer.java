package project.mobile.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static project.plugins.InitializePlugin.logger;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    //Counter to keep track of retry attempts
    int maxRetryLimit = 2;

    //Method to attempt retries for failure tests
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryLimit) {
            retryCount++;
            logger.get().warn("Retrying test: {} (attempt {}/{})", result.getName(), retryCount + 1, maxRetryLimit + 1);
            return true;
        }
        return false;
    }
}
