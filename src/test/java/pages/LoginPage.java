package pages;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
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

    public void login(String email, String password) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.clickElement(eyePassword);
        WebUI.sleep(1);
        WebUI.clickElement(buttonLogin);
    }
    public void displayLogin() {
        WebUI.clearActualTexts();
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
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Login/dataTextLogin.csv");
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi <<<<<");
        for (int i=0; i< WebUI.sizeActualTexts(); i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Display Icon")
                .addScreenCaptureFromPath(shot1,"Display Icon");
    }
    public void interactLogin(String email, String password) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.clickElement(buttonRegister);
        WebUI.assertEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/register","Register now ? co su dieu huong");
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.clickElement(eyePassword);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"text","Password hien thi");
        WebUI.clickElement(eyePassword);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"password","Password duoc an di");
    }
    public void verifyByEnterKey(String email, String password, String messageLogin, String urlProfile) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/login");
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.logConsole("Submit dieu huong den trang login");
        WebUI.clickElement(inputPassword);
        WebUI.keysByAction(Keys.ENTER);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(message),messageLogin,messageLogin);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login dieu huong den trang profile");
        test.log(Status.INFO, "📸 Message Success")
                .addScreenCaptureFromPath(shot1,"Message Success");
    }
    public void verifyValidName(String name, String password, String phone, String birthday,String urlProfile) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        int count = 0;
        boolean isExisted;
        do {
            String randomEmail = RandomStringUtils.randomAlphabetic(6)+RandomStringUtils.randomNumeric(8)+"@gmail.com";
            registerPage.loginPage(name,randomEmail,password,phone,birthday);
            isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
            count++;
            if (count > 3) {
                break;
            }
            WebUI.setText(inputEmail,randomEmail);
            WebUI.setText(inputPassword,password);
        } while (isExisted);
        WebUI.logConsole("Submit dieu huong den trang login");
        WebUI.sleep(1);
        WebUI.clickElement(buttonLogin);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login dieu huong den trang profile");
    }
    public void verifyValid(String email, String password, String urlProfile) {
        login(email,password);
        WebUI.assertEquals(WebUI.getURL(),urlProfile,"Login dieu huong den trang profile");
    }
    public void verifyErrorEmpty() {
        WebUI.clearActualTexts();
        login("","");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.logConsole(">>>>> Kiem Tra So Luong Hien Thi - Length 2 <<<<<");
        for (int i = 0; i < 2; i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Login/verifyErrorWhenAllFieldsEmpty.csv");
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi <<<<<");
        for (int i=0; i< WebUI.sizeActualTexts(); i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Verify Error When All Fields Empty")
                .addScreenCaptureFromPath(shot1,"Verify Error When All Fields Empty");
    }
    public void verifyToolTip() {
        WebUI.clearActualTexts();
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
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi - Length 2 <<<<<");
        for (int i=0; i < 2; i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Verify Tool Tip")
                .addScreenCaptureFromPath(shot1,"Email")
                .addScreenCaptureFromPath(shot2,"Password");
    }
    public void verifyInvalidLogin(String email, String password,String errorEmail,String errorPassword) {
        login(email,password);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(messageError,0),errorEmail,errorEmail);
        WebUI.assertEquals(WebUI.getElementText(messageError,1),errorPassword,errorPassword);
        test.log(Status.INFO, "📸 Verify Error")
                .addScreenCaptureFromPath(shot1,"Verify Error");
    }
    public void verifyErrorSpaceEmail(String email, String password,String errorEmail) {
        login(email,password);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(messageError),errorEmail,errorEmail);
        test.log(Status.INFO, "📸 Verify Error")
                .addScreenCaptureFromPath(shot1,"Verify Error");
    }
    public void verifyInvalid(String email, String password,String error) {
        login(email,password);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(message),error,error);
        test.log(Status.INFO, "📸 Verify Error")
                .addScreenCaptureFromPath(shot1,"Verify Error");
    }
}
