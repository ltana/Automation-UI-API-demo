package project.mobile.listeners;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Set;

public class RetryCleanupListener extends TestListenerAdapter {
    @Override
    public void onFinish(ITestContext context) {
        Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
        for (ITestResult failedTest : failedTests) {
            ITestNGMethod method = failedTest.getMethod();
            if (context.getPassedTests().getResults(method).size() > 0) {
                context.getFailedTests().removeResult(method);
            }
        }
    }
}
