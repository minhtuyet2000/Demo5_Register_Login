package pages;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pages.webui.WebUI;
import scripts.utils.CSVReaderUtilForList;
import utilities.ExtentTestManager;

import java.util.List;

public class LoginPage {
    private WebDriver driver;
    private RegisterPage registerPage;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        registerPage = new RegisterPage(driver);
    }
    private By buttonSignIn = By.xpath("//ul[contains(@class,'ul')]//a[contains(text(),'Sign in')]");
    private By titleLogin = By.xpath("//h2[text()='Sign In to Fiverr']");
    private By inputEmail = By.xpath("//input[@placeholder='Your Email']");
    private By inputPassword = By.xpath("//input[@placeholder='Your Password']");
    private By eyePassword = By.xpath("//input[@placeholder='Your Password']//following-sibling::*[contains(@class,'show')]");
    private By buttonLogin = By.xpath("//button[text()='Login']");
    private By buttonRegister = By.xpath("//a[text()='Register now ?']");
    private By pngLogin = By.xpath("//img");

    private By message = By.xpath("//div[@role='alert']");
    private By messageError = By.xpath("//span[contains(@class,'danger')]");

    private void setLogin(String email, String password) {
        do {
            driver.get("https://demo5.cybersoft.edu.vn/login");
            WebUI.setText(inputEmail, email);
            WebUI.setText(inputPassword, password);
            WebUI.clickElement(eyePassword);
        } while (!driver.getCurrentUrl().equals("https://demo5.cybersoft.edu.vn/login"));
        WebUI.clickElement(buttonLogin);
        WebUI.scrollToPosition(0,0);
    }
    public void displayLogin() {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/");
        WebUI.scrollToPosition(0,500);
        WebUI.clickElement(buttonSignIn);
        WebUI.assertDisplay(titleLogin);
        WebUI.assertDisplayForAttribute(inputEmail,"placeholder");
        WebUI.assertDisplayForAttribute(inputPassword,"placeholder");
        WebUI.assertDisplayForAttribute(eyePassword,"class");
        WebUI.assertDisplay(buttonLogin);
        WebUI.assertDisplay(buttonRegister);
        WebUI.assertDisplayForAttribute(pngLogin,"src");
        WebUI.scrollToPosition(0,0);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Login/dataTextLogin.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Display The LoginPage")
                .addScreenCaptureFromPath(shot1,"Display The LoginPage");
    }
    public void interactLogin() {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.clickElement(buttonRegister);
        WebUI.assertEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/register","Register now ? có sự điều hướng");
        String longText = "A".repeat(100);
        setLogin(longText,longText);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"text","Password hiển thị");
        WebUI.scrollToPosition(0,0);
        WebUI.clickElement(eyePassword);
        WebUI.scrollToPosition(0,0);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"password","Password được ẩn đi");
    }
    public void verifyByEnterKey(String email, String password, String messageLogin, String urlProfile) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.clickElement(inputPassword);
        WebUI.keysByAction(Keys.ENTER);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(message),messageLogin,messageLogin);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login điều hướng đến trang profile");
        test.log(Status.INFO, "📸 Message Success")
                .addScreenCaptureFromPath(shot1,"Message Success");
    }
    public void verifyValidName(String name, String password, String phone, String birthday,String urlProfile) {
        String emailLogin = registerPage.setEmail(name, password, phone, birthday);
        WebUI.logConsole("Submit điều hướng đến trang login");
        setLogin(emailLogin,password);
        WebUI.sleep(1);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login điều hướng đến trang profile");
    }
    public void verifyValid(String email, String password, String urlProfile) {
        setLogin(email,password);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login điều hướng đến trang profile");
    }
    public void verifyErrorEmpty() {
        setLogin("        ","        ");
        do {
            driver.get("https://demo5.cybersoft.edu.vn/login");
            WebUI.setText(inputEmail,"");
            WebUI.setText(inputPassword,"");
        } while (!driver.getCurrentUrl().equals("https://demo5.cybersoft.edu.vn/login"));
        WebUI.clickElement(buttonLogin);
        WebUI.scrollToPosition(0,0);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        for (int i = 0; i < WebUI.getWebElements(messageError).size(); i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Login/verifyErrorWhenAllFieldsEmpty.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Verify Error When All Fields Empty")
                .addScreenCaptureFromPath(shot1,"Verify Error When All Fields Empty");
    }
    public void verifyToolTip() {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.setText(inputPassword," Tyt @12 ");
        WebUI.clickElement(buttonLogin);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.toolTip(inputEmail);
        WebUI.setText(inputEmail,"minh123%@gmail.com");
        WebUI.getWebElement(inputPassword).clear();
        WebUI.getWebElement(inputPassword).sendKeys("");
        WebUI.sleep(1);
        WebUI.clickElement(buttonLogin);
        String shot2 = WebUI.captureScreenshot();
        WebUI.toolTip(inputPassword);
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Login/verifyToolTip.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Verify Tool Tip")
                .addScreenCaptureFromPath(shot1,"Email")
                .addScreenCaptureFromPath(shot2,"Password");
    }
    public void verifyInvalid(String email, String password,String errorEmail,String errorPassword) {
        setLogin(email,password);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        if (errorEmail.equals("Email không đúng định dạng !") && errorPassword.equals("Pass từ 6 - 32 ký tự !")) {
            WebUI.assertEquals(WebUI.getElementText(messageError,0),errorEmail,errorEmail);
            WebUI.assertEquals(WebUI.getElementText(messageError,1),errorPassword,errorPassword);
        } else if (errorEmail.equals("Email không đúng định dạng !")) {
            WebUI.assertEquals(WebUI.getElementText(messageError),errorEmail,errorEmail);
        } else if (errorEmail.equals("Email hoặc mật khẩu không đúng !") && errorPassword.equals("Email hoặc mật khẩu không đúng !")) {
            WebUI.assertEquals(WebUI.getElementText(message), errorPassword, errorPassword);
        }
        test.log(Status.INFO, "📸 Verify InValid")
                .addScreenCaptureFromPath(shot1,"Verify InValid");
    }
}
