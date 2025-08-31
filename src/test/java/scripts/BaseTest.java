package scripts;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import pages.webui.WebUI;

public class BaseTest {
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        WebDriver driverInstance = createDriver(browser);
        DriverManager.setDriver(driverInstance);
        WebUI.clearActualTexts();
        WebUI.resetSoftAssert();
    }

    @AfterMethod
    public void tearDown() {
        WebUI.assertAll();
        DriverManager.quitDriver();
    }

    public WebDriver createDriver(String browserName) {
        WebDriver driver;
        switch (browserName.trim().toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                System.out.println("Browser không hợp lệ → chạy Chrome mặc định.");
                driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        return driver;
    }
}
