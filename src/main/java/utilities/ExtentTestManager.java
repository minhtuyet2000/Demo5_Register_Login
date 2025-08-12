package utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static void log(Status status, String message) {
        getTest().log(status, message);
    }
}
