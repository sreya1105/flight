package flight;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Code to execute when a test starts
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Code to execute when a test succeeds
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Code to execute when a test fails
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Code to execute when a test is skipped
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Code to execute when a test fails but is within the success percentage
    }

    @Override
    public void onStart(ITestContext context) {
        // Code to execute before any test starts
    }

    @Override
    public void onFinish(ITestContext context) {
        // Code to execute after all tests have finished
    }
}
