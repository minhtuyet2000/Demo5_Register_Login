package pages;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import pages.webui.WebUI;
import scripts.utils.CSVReaderUtilForList;
import utilities.ExtentTestManager;

import javax.swing.*;
import java.util.List;

public class RegisterPage {
    private WebDriver driver;
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }
    private By buttonJoin = By.xpath("//ul[contains(@class,'ul')]//a[contains(text(),'Join')]");
    private By titleRegister = By.xpath("//h2[text()='REGISTER']");
    private By inputName = By.xpath("//input[@placeholder='Your Name']");
    private By inputEmail = By.xpath("//input[@placeholder='Your Email']");
    private By inputPassword = By.xpath("//input[@placeholder='Your Password']");
    private By eyePassword = By.xpath("//input[@placeholder='Your Password']//following-sibling::*[contains(@class,'show')]");
    private By inputRepeatPassword = By.xpath("//input[@placeholder='Repeat your password']");
    private By eyeRepeatPassword = By.xpath("//input[@placeholder='Repeat your password']//following-sibling::*[contains(@class,'show')]");
    private By inputPhone = By.xpath("//input[@placeholder='Your Phone']");
    private By inputBirthday = By.xpath("//input[@placeholder='Your birthday']");
    private By radioMale = By.xpath("//label[@for='male']");
    private By radioFemale = By.xpath("//label[@for='female']");
    private By textAgree = By.xpath("//label[text()='I agree all statements in']");
    private By buttonAgree = By.xpath("//input[@type='checkbox']");
    private By buttonTerms = By.xpath("//a[text()='Terms of service']");
    private By buttonSubmit = By.xpath("//button[text()='Submit']");
    private By buttonLogin = By.xpath("//a[text()='I am already member']");
    private By pngRegister = By.xpath("//img");

    private By message = By.xpath("//div[@role='alert']");
    private By messageError = By.xpath("//span[contains(@class,'danger')]");

    public void setRegister(String name, String email, String password, String phone, String birthday) {
        do {
            driver.get("https://demo5.cybersoft.edu.vn/register");
            WebUI.setText(inputName,name);
            WebUI.setText(inputEmail,email);
            WebUI.setText(inputPassword,password);
            WebUI.setText(inputRepeatPassword,password);
            WebUI.clickElement(eyePassword);
            WebUI.setText(inputPhone,phone);
            WebUI.setText(inputBirthday,birthday);
            WebUI.clickElement(radioFemale);
            WebUI.clickElement(buttonAgree);
        } while (!driver.getCurrentUrl().equals("https://demo5.cybersoft.edu.vn/register"));
        WebUI.clickElement(buttonSubmit);
        WebUI.scrollToPosition(0,0);
    }
    public String setEmail(String name, String password, String phone, String birthday) {
        int count = 0;
        boolean isExisted;
        String email;
        do {
            String randomEmail = RandomStringUtils.randomAlphabetic(6)+RandomStringUtils.randomNumeric(8)+"@gmail.com";
            setRegister(name,randomEmail,password,phone,birthday);
            email = randomEmail;
            isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
            count++;
            if (count > 3) {
                email = "";
                break;
            }
        } while (isExisted);
        return email;
    }
    public void displayRegister() {
        WebUI.clearActualTexts();
        WebUI.openURL("https://demo5.cybersoft.edu.vn/");
        WebUI.scrollToPosition(0,500);
        WebUI.clickElement(buttonJoin);
        WebUI.assertDisplay(titleRegister);
        WebUI.assertDisplayForAttribute(inputName,"placeholder");
        WebUI.assertDisplayForAttribute(inputEmail,"placeholder");
        WebUI.assertDisplayForAttribute(inputPassword,"placeholder");
        WebUI.assertDisplayForAttribute(eyePassword,"class");
        WebUI.assertDisplayForAttribute(inputRepeatPassword,"placeholder");
        WebUI.assertDisplayForAttribute(eyeRepeatPassword,"class");
        WebUI.assertDisplayForAttribute(inputPhone,"placeholder");
        WebUI.assertDisplayForAttribute(inputBirthday,"placeholder");
        WebUI.assertDisplay(radioMale);
        WebUI.assertDisplay(radioFemale);
        WebUI.assertDisplay(textAgree);
        WebUI.assertDisplayForAttribute(buttonAgree,"name");
        WebUI.assertDisplay(buttonTerms);
        WebUI.assertDisplay(buttonSubmit);
        WebUI.assertDisplay(buttonLogin);
        WebUI.assertDisplayForAttribute(pngRegister,"src");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/dataTextRegister.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Display Icon")
                .addScreenCaptureFromPath(shot1,"Display Icon");
    }
    public void interactRegister() {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        WebUI.clickElement(buttonTerms);
        WebUI.assertNotEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/register#","Terms of service có sự điều hướng");
        WebUI.clickElement(buttonJoin);
        WebUI.clickElement(buttonLogin);
        WebUI.assertEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/login","I am already member điều hướng đến trang login");
        String longText = "A".repeat(200);
        setRegister(longText,longText,longText,longText,longText);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"text","Password hiển thị");
        WebUI.assertEquals(WebUI.getAttributeText(inputRepeatPassword,"type"),"text","RepeatPassword hiển thị");
        WebUI.clickElement(eyePassword);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"password","Password được ẩn đi");
        WebUI.assertEquals(WebUI.getAttributeText(inputRepeatPassword,"type"),"password","RepeatPassword được ẩn đi");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.clickElement(radioMale);
        String shot2 = WebUI.captureScreenshot();
        test.log(Status.INFO, "📸 Radio Male or Female")
                .addScreenCaptureFromPath(shot1,"Female is selected – Male is not selected")
                .addScreenCaptureFromPath(shot2,"Male is selected – Female is not selected");
    }
    public void verifyMessageSuccess(String name, String password, String phone, String birthday,String messageSuccess, String urlLogin) {
        setEmail(name, password, phone, birthday);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(message),messageSuccess,messageSuccess);
        WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit điều hướng đến trang login");
        test.log(Status.INFO, "📸 Message Success")
                .addScreenCaptureFromPath(shot1,"Message Success");
    }
    public void verifyRepeatEmail(String name, String password, String phone, String birthday, String urlLogin, String messageRepeatEmail) {
        String emailLogin = setEmail(name, password, phone, birthday);
        setRegister(name.repeat(1),emailLogin,password.repeat(1),"0850000000","19092000");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.sleep(1);
        if (WebUI.getWebElements(message).size() == 2) {
            WebUI.assertEquals(WebUI.getElementText(message,1),messageRepeatEmail,messageRepeatEmail);
        } else {
            WebUI.assertEquals(WebUI.getElementText(message,0),messageRepeatEmail,messageRepeatEmail);
        }
        test.log(Status.INFO, "📸 Message Repeat Email")
                .addScreenCaptureFromPath(shot1,"Message Repeat Email");
    }
    public void verifyErrorEmpty() {
        WebUI.clearActualTexts();
        driver.get("https://demo5.cybersoft.edu.vn/register");
        WebUI.setText(inputName,"");
        WebUI.setText(inputEmail,"");
        WebUI.setText(inputPassword,"");
        WebUI.setText(inputRepeatPassword,"");
        WebUI.setText(inputPhone,"");
        WebUI.setText(inputBirthday,"");
        WebUI.clickElement(radioFemale);
        WebUI.clickElement(buttonAgree);
        WebUI.clickElement(buttonSubmit);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.logConsole(">>>>> Kiểm Tra Số Lượng Hiển Thị <<<<<");
        for (int i = 0; i < 6; i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/verifyErrorWhenAllFieldsEmpty.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Verify Error When All Fields Empty")
                .addScreenCaptureFromPath(shot1,"Verify Error When All Fields Empty");
    }
    public void verifyErrorSpace() {
        WebUI.clearActualTexts();
        setRegister("        ","        ","        ","        ","        ");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.logConsole(">>>>> Kiểm Tra Số Lượng Hiển Thị <<<<<");
        for (int i = 0; i < 6; i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/verifyErrorWhenInputIsSpace.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Verify Error When Input Is Space")
                .addScreenCaptureFromPath(shot1,"Verify Error When Input Is Space");
    }
    public void verifyToolTip(String name, String email, String password, String phone, String birthday) {
        WebUI.clearActualTexts();
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.setText(inputRepeatPassword,password);
        WebUI.setText(inputPhone,phone);
        WebUI.setText(inputBirthday,birthday);
        WebUI.clickElement(radioFemale);
        WebUI.clickElement(buttonAgree);
        WebUI.clickElement(buttonSubmit);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.toolTip(inputName);
        WebUI.setText(inputName,name);
        WebUI.setText(inputEmail,"");
        WebUI.clickElement(buttonSubmit);
        String shot2 = WebUI.captureScreenshot();
        WebUI.toolTip(inputEmail);
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,"");
        WebUI.clickElement(buttonSubmit);
        String shot3 = WebUI.captureScreenshot();
        WebUI.toolTip(inputPassword);
        WebUI.setText(inputPassword,password);
        WebUI.setText(inputRepeatPassword,"");
        WebUI.clickElement(buttonSubmit);
        String shot4 = WebUI.captureScreenshot();
        WebUI.toolTip(inputRepeatPassword);
        WebUI.setText(inputRepeatPassword,password);
        WebUI.setText(inputPhone,"");
        WebUI.clickElement(buttonSubmit);
        String shot5 = WebUI.captureScreenshot();
        WebUI.toolTip(inputPhone);
        WebUI.setText(inputPhone,phone);
        WebUI.setText(inputBirthday,"");
        WebUI.clickElement(buttonSubmit);
        String shot6 = WebUI.captureScreenshot();
        WebUI.toolTip(inputBirthday);
        WebUI.setText(inputBirthday,birthday);
        WebUI.clickElement(buttonAgree);
        WebUI.clickElement(buttonSubmit);
        String shot7 = WebUI.captureScreenshot();
        WebUI.toolTip(buttonAgree);
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/verifyToolTip.csv");
        WebUI.compareActualText(expectedTexts);
        test.log(Status.INFO, "📸 Verify Tool Tip")
                .addScreenCaptureFromPath(shot1,"Name")
                .addScreenCaptureFromPath(shot2,"Email")
                .addScreenCaptureFromPath(shot3,"Password")
                .addScreenCaptureFromPath(shot4,"RepeatPassword")
                .addScreenCaptureFromPath(shot5,"Phone")
                .addScreenCaptureFromPath(shot6,"Birthday")
                .addScreenCaptureFromPath(shot7,"Agree");
    }
    public void verifyRepeatPassword(String name, String email, String phone, String birthday, String pass, String repeatpass, String error) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        WebUI.setText(inputName,name);
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,pass);
        WebUI.setText(inputRepeatPassword,repeatpass);
        WebUI.clickElement(eyePassword);
        WebUI.setText(inputPhone,phone);
        WebUI.setText(inputBirthday,birthday);
        WebUI.clickElement(radioFemale);
        WebUI.clickElement(buttonAgree);
        WebUI.clickElement(buttonSubmit);
        WebUI.assertEquals(WebUI.getElementText(messageError),error,error);
    }
    public void verifyValid(String name, String password, String phone, String birthday, String urlLogin) {
        setEmail(name, password, phone, birthday);
        WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit điều hướng đến trang login");
    }
    public void verifyValidEmail(String name,String email ,String password, String phone, String birthday, String urlRegister, String urlLogin) {
        setRegister(name,email,password,phone,birthday);
        boolean isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
        if (isExisted) {
            WebUI.assertEquals(WebUI.getURL(),urlRegister,"Email đã tồn tại !");
        } else {
            WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit điều hướng đến trang login");
        }
    }
    public void verifyInValid(String name,String email ,String password, String phone, String birthday, String error) {
        setRegister(name,email,password,phone,birthday);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        if (password.length()>32) {
            WebUI.assertEquals(WebUI.getElementText(messageError),"Pass từ 6 - 32 ký tự !","Pass từ 6 - 32 ký tự !");
        } else if (password.length()<6) {
            WebUI.assertEquals(WebUI.getElementText(messageError),"Password từ 6 - 32 ký tự","Password từ 6 - 32 ký tự");
        } else {
            WebUI.assertEquals(WebUI.getElementText(messageError),error,error);
        }
        test.log(Status.INFO, "📸 Verify Error")
                .addScreenCaptureFromPath(shot1,"Verify Error");
    }
}
