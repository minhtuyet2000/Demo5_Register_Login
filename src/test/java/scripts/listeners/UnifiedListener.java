package scripts.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import pages.webui.WebUI;
import utilities.ExtentManager;
import utilities.ExtentTestManager;


public class UnifiedListener implements ITestListener {
    public static ExtentReports extent = ExtentManager.createInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getDescription());
        test.set(extentTest);
        ExtentTestManager.setTest(extentTest);
    }
    @Override
    public void onTestSuccess(ITestResult result) {
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        test.get().assignDevice(browser);
        test.get().assignCategory(result.getMethod().getGroups());
        test.get().assignCategory(result.getTestClass().getRealClass().getSimpleName());

        test.get().log(Status.PASS, "✅ Test Passed - Method - " + result.getMethod().getMethodName());
        test.get().log(Status.PASS, "✅ Test Passed - Class - " + result.getTestClass().getName());
        long durationMillis = result.getEndMillis() - result.getStartMillis();
        long seconds = (durationMillis / 1000);
        test.get().log(Status.INFO, "⏱Duration: " + seconds + "s");

    }
    @Override
    public void onTestFailure(ITestResult result) {
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        test.get().assignDevice(browser);
        test.get().assignCategory(result.getMethod().getGroups());
        test.get().assignCategory(result.getTestClass().getRealClass().getSimpleName());

        test.get().log(Status.FAIL, "❌ Test Failed - Method - " + result.getMethod().getMethodName());
        test.get().log(Status.FAIL, "❌ Test Failed - Class - " + result.getTestClass().getName());
        long durationMillis = result.getEndMillis() - result.getStartMillis();
        long seconds = (durationMillis / 1000);
        test.get().log(Status.INFO, "⏱Duration: " + seconds + "s");

        String screenshotPath = WebUI.captureScreenshot();
        if (screenshotPath != null) {
            ExtentTestManager.getTest().addScreenCaptureFromPath(screenshotPath, "📷 Screenshot on Failure");
        }
    }
    @Override
    public void onTestSkipped(ITestResult result) {
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        test.get().assignDevice(browser);
        test.get().assignCategory(result.getMethod().getGroups());
        test.get().assignCategory(result.getTestClass().getRealClass().getSimpleName());

        test.get().log(Status.SKIP, "⚠ Test Skipped - Method - " + result.getMethod().getMethodName());
        test.get().log(Status.SKIP, "⚠ Test Skipped - Class - " + result.getTestClass().getName());
        long durationMillis = result.getEndMillis() - result.getStartMillis();
        long seconds = (durationMillis / 1000);
        test.get().log(Status.INFO, "⏱Duration: " + seconds + "s");
    }
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
