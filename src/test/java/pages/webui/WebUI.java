package pages.webui;

import com.aventstack.extentreports.Status;
import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import utilities.ExtentTestManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class WebUI {
    private static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
    public static void assertAll() {
        softAssert.get().assertAll();
        softAssert.remove();
    }
    public static void resetSoftAssert() {
        softAssert.remove();
    }

    private static int TIMEOUT = 5;
    private static double STEP_TIME = 0;
    private static int PAGE_LOAD_TIMEOUT = 10;
    static Logger logger = LogManager.getLogger(WebUI.class);
    public static void logConsole(String message) {
        logger.info(message);
        ExtentTestManager.log(Status.INFO, message);
    }
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            logConsole("Hết thời gian sleep");
        }
    }
    public static void waitForElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            logConsole("Hết thời gian chờ hiển thị. Element - " + by);
        }
    }
    public static void waitForPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
            wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        } catch (Throwable error) {
            logConsole("Hết thời gian chờ tải trang");
        }
    }
    public static void scrollToPosition(int X, int Y) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }
    public static void toolTip(By by) {
        WebElement element = getWebElement(by);
        if (element == null) {
            logConsole("Không tìm thấy phần tử để lấy tooltip: " + by);
        } else {
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            String message = (String) js.executeScript("return arguments[0].validationMessage;", getWebElement(by));
            addActualText(message.trim());
            logConsole("Validation message: " + message);
            js.executeScript("arguments[0].removeAttribute('required')", getWebElement(by));
        }
    }
    public static WebElement getWebElement(By by) {
        try {
            return DriverManager.getDriver().findElement(by);
        } catch (Throwable error) {
            String log = error.getMessage();
            if (log.length() > 300) {
                log = log.substring(0, 300) + "...";
            }
            logConsole("❌ Lý do: " + log);
            return null;
        }
    }
    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }
    public static void openURL(String url) {
        DriverManager.getDriver().get(url);
        waitForPageLoaded();
        sleep(1);
        logConsole("Mở URL: " + url);
    }
    public static void setText(By by, String value) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(STEP_TIME);
        WebElement element = getWebElement(by);
        if (element == null) {
            return;
        }
        logConsole("Set text: " + value + " on element -" + element.getText() + " " + by);
        try {
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            element.clear();
            element.sendKeys(value);
        } catch (Exception e) {
            logConsole("Set text by js: " + value + " on element -" + element.getText() + " " + by);
            try {
                JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
                js.executeScript("arguments[0].value = '" + value + "';", element);
                element.sendKeys("tester");
            } catch (Exception ejs) {
                logConsole("❌ Không thể set text. Element - " + by);
                String log = ejs.getMessage();
                if (log.length() > 300) {
                    log = log.substring(0, 300) + "...";
                }
                logConsole("❌ Lý do: " + log);

            }
        }
    }
    public static void clickElement(By by) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(STEP_TIME);
        WebElement element = getWebElement(by);
        if (element == null) {
            return;
        }
        logConsole("Click element -" + element.getText() + " " + by);
        try {
            element.click();
        } catch (Exception e) {
            logConsole("Click element by action-" + element.getText() + " " + by);
            try {
                Actions actions = new Actions(DriverManager.getDriver());
                actions.moveToElement(element).click().build().perform();
            } catch (Exception ex) {
                logConsole("Click element by js-" + element.getText() + " " + by);
                try {
                    JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
                    js.executeScript("arguments[0].scrollIntoView(true);", element);
                    js.executeScript("arguments[0].click();", element);
                } catch (Exception ejs) {
                    logConsole("❌ Không thể click. Element - " + by);
                    String log = ejs.getMessage();
                    if (log.length() > 300) {
                        log = log.substring(0, 300) + "...";
                    }
                    logConsole("❌ Lý do: " + log);
                }
            }
        }
    }
    public static String getElementText(By by, int index) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(STEP_TIME);
        List<WebElement> elements = getWebElements(by);
        if (index < 0 || index >= elements.size()) {
            logConsole("❌ Không tìm thấy element -" + " " + by + ".get(" + index + ")");
            return "";
        }
        WebElement element = elements.get(index);
        return element.getText().trim();
    }
    public static String getElementText(By by) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(STEP_TIME);
        WebElement element = getWebElement(by);
        if (element == null) {
            return "";
        } else {
            return element.getText().trim();
        }
    }
    public static String getAttributeText(By by, String attribute) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(STEP_TIME);
        WebElement element = getWebElement(by);
        if (element == null) {
            return "";
        } else {
            return element.getAttribute(attribute).trim();
        }
    }
    public static void keysByAction(Keys action) {
        waitForPageLoaded();
        sleep(STEP_TIME);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(action).build().perform();
        logConsole("🧩 Đã nhấn phím: " + action.name());
    }
    public static String getURL() {
        waitForPageLoaded();
        sleep(1);
        return DriverManager.getDriver().getCurrentUrl();
    }
    public static void assertEquals(String actual, String expected, String messagePass) {
        try {
            boolean assertEquals = actual.trim().equals(expected.trim());
            if (assertEquals) {
                logConsole("✅ Assert Passed: " + messagePass);
            } else {
                logConsole("❌ Assert Failed - Expected: " + expected + " - Actual: " + actual);
            }
            softAssert.get().assertTrue(assertEquals);
        } catch (Exception e) {
            logConsole("❌ Assert Failed - Message Pass: " + messagePass);
            logConsole("Lý do: " + e.getMessage());
        }
    }
    public static void assertNotEquals(String actual, String notEquals, String messagePass) {
        try {
            boolean assertEquals = actual.trim().equals(notEquals.trim());
            if (!assertEquals) {
                logConsole("✅ Assert Passed: " + messagePass);
            } else {
                logConsole("❌ Assert Failed - Equals: " + notEquals);
            }
            softAssert.get().assertFalse(assertEquals);
        } catch (Exception e) {
            logConsole("❌ Assert Failed - Message Pass: " + messagePass);
            logConsole("Lý do: " + e.getMessage());
        }
    }
    public static void compareTwoLists(List<String> listA, List<String> listB) {
        int maxSize = Math.max(listA.size(), listB.size());
        for (int i = 0; i < maxSize; i++) {
            String a = (i < listA.size()) ? listA.get(i) : "";
            String b = (i < listB.size()) ? listB.get(i) : "";
            assertEquals(a,b,b);
        }
    }
    public static void compareActualText(List<String> expected) {
        List<String> actual = actualTexts.get();
        WebUI.logConsole(">>>>> Kiểm Tra Nội Dung Hiển Thị <<<<<");
        compareTwoLists(actual,expected);
    }
    private static ThreadLocal<List<String>> actualTexts = ThreadLocal.withInitial(ArrayList::new);
    public static void addActualText(String text) {
        actualTexts.get().add(text);
    }
    public static void clearActualTexts() {
        actualTexts.get().clear();
    }
    public static void assertDisplay(By by, int index) {
        boolean actual = false;
        try {
            waitForPageLoaded();
            sleep(1);
            WebElement element = getWebElements(by).get(index);
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].scrollIntoView(true);", element);
                if (element != null && element.isDisplayed()) {
                    if (element.getText().equals("")) {
                        logConsole("❌ Assert Failed. Element - " + by + " co gia tri rong");
                        addActualText("");
                    } else {
                        actual = true;
                        logConsole("✅ Assert Passed: " + element.getText());
                        addActualText(element.getText().trim());
                    }
                } else {
                    //chap nhan loi isDisplayed va thuc thi catch
                    logConsole("❌ Assert Failed. Element - " + by + ".get(" + index + ") khong hien thi");
                    addActualText("");
                }
        } catch (Exception e) {
            logConsole("❌ Assert Failed. Element - " + by + ".get(" + index + ") khong ton tai");
            addActualText("");
        }
        softAssert.get().assertTrue(actual);
    }
    public static void assertDisplay(By by) {
        boolean actual = false;
        try {
            waitForPageLoaded();
            sleep(1);
            WebElement element = getWebElement(by);
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].scrollIntoView(true);", element);
                if (element != null && element.isDisplayed()) {
                    if (element.getText().equals("")) {
                        logConsole("❌ Assert Failed. Element - " + by + " co gia tri rong");
                        addActualText("");
                    } else {
                        actual = true;
                        logConsole("✅ Assert Passed: " + element.getText());
                        addActualText(element.getText().trim());
                    }
                } else {
                    logConsole("❌ Assert Failed. Element - " + by + " khong hien thi");
                    addActualText("");
                }
        } catch (Exception e) {
            logConsole("❌ Assert Failed. Element - " + by + " khong ton tai");
            addActualText("");
        }
        softAssert.get().assertTrue(actual);
    }
    public static void assertDisplayForAttribute(By by, String attribute) {
        boolean actual = false;
        try {
            waitForPageLoaded();
            sleep(1);
            WebElement element = getWebElement(by);
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            if (element != null && element.isDisplayed()) {
                if (element.getAttribute(attribute).equals("")) {
                    logConsole("❌ Assert Failed. Element - " + by + " co gia tri rong");
                    addActualText("");
                } else {
                    actual = true;
                    logConsole("✅ Assert Passed: " + element.getAttribute(attribute));
                    addActualText(element.getAttribute(attribute).trim());
                }
            } else {
                logConsole("❌ Assert Failed. Element - " + by + " khong hien thi");
                addActualText("");
            }
        } catch (Exception e) {
            logConsole("❌ Assert Failed. Element - " + by + " khong ton tai");
            addActualText("");
        }
        softAssert.get().assertTrue(actual);
    }
    public static String captureScreenshot() {
        try {
            waitForPageLoaded();
            sleep(1);
            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = getCurrentTestMethodName() + "_" + timestamp + ".png";
            String fullPath = "reports/screenshots/" + fileName;
            File destFile = new File(fullPath);
            destFile.getParentFile().mkdirs();
            FileHandler.copy(srcFile, destFile);
            return "screenshots/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getCurrentTestMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().startsWith("test")) {
                return element.getMethodName();
            }
        }
        return "unknownMethod";
    }
}
