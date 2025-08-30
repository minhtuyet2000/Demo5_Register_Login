package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.Locale;

public class ExtentManager {
    private static ExtentReports extent;
    public static ExtentReports createInstance() {
        String reportPath =
                "./reports/ExtentReport.html";
        ExtentSparkReporter reporter = new
                ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Automation Test Report");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        Locale.setDefault(Locale.ENGLISH);
        reporter.config().setReportName("Test Results");
        reporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Minh Tuyet");
        extent.setSystemInfo("Environment", "Staging");
        return extent;
    }
}
