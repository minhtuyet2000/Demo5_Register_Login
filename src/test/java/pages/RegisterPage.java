package pages;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import pages.webui.WebUI;
import scripts.utils.CSVReaderUtilForList;
import utilities.ExtentTestManager;
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

    public void loginPage(String name, String email, String password, String phone, String birthday) {
        WebUI.setText(inputName,name);
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.setText(inputRepeatPassword,password);
        WebUI.clickElement(eyePassword);
        WebUI.setText(inputPhone,phone);
        WebUI.setText(inputBirthday,birthday);
        WebUI.clickElement(radioFemale);
        WebUI.clickElement(buttonAgree);
        WebUI.clickElement(buttonSubmit);
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
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi <<<<<");
        for (int i=0; i< WebUI.sizeActualTexts(); i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Display Icon")
                .addScreenCaptureFromPath(shot1,"Display Icon");
    }
    public void interactRegister(String name, String email, String password, String phone, String birthday) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        WebUI.clickElement(buttonTerms);
        WebUI.assertNotEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/register#","Terms of service co su dieu huong");
        WebUI.clickElement(buttonJoin);
        WebUI.clickElement(buttonLogin);
        WebUI.assertEquals(WebUI.getURL(),"https://demo5.cybersoft.edu.vn/login","I am already member dieu huong den trang login");
        WebUI.clickElement(buttonJoin);
        WebUI.setText(inputName,name);
        WebUI.setText(inputEmail,email);
        WebUI.setText(inputPassword,password);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"password","Password duoc an di");
        WebUI.clickElement(eyePassword);
        WebUI.assertEquals(WebUI.getAttributeText(inputPassword,"type"),"text","Password hien thi");
        WebUI.setText(inputRepeatPassword,password);
        WebUI.assertEquals(WebUI.getAttributeText(inputRepeatPassword,"type"),"text","RepeatPassword hien thi");
        WebUI.clickElement(eyeRepeatPassword);
        WebUI.assertEquals(WebUI.getAttributeText(inputRepeatPassword,"type"),"password","RepeatPassword duoc an di");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.clickElement(radioFemale);
        String shot2 = WebUI.captureScreenshot();
        WebUI.clickElement(buttonAgree);
        WebUI.setText(inputPhone,phone);
        WebUI.setText(inputBirthday,birthday);
        test.log(Status.INFO, "📸 Radio Male or Female")
                .addScreenCaptureFromPath(shot1,"Male is selected – Female is not selected")
                .addScreenCaptureFromPath(shot2,"Female is selected – Male is not selected");
    }
    public void verifyMessageSuccess(String name, String password, String phone, String birthday,String messageRegister, String urlLogin) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        int count = 0;
        boolean isExisted;
        do {
            String randomEmail = RandomStringUtils.randomAlphabetic(6)+RandomStringUtils.randomNumeric(8)+"@gmail.com";
            loginPage(name,randomEmail,password,phone,birthday);
            isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
            count++;
            if (count > 3) {
                break;
            }
        } while (isExisted);
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.assertEquals(WebUI.getElementText(message),messageRegister,messageRegister);
        WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit dieu huong den trang login");
        test.log(Status.INFO, "📸 Message Success")
                .addScreenCaptureFromPath(shot1,"Message Success");
    }
    public void verifyValid(String name, String password, String phone, String birthday,String urlLogin) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        int count = 0;
        boolean isExisted;
        do {
            String randomEmail = RandomStringUtils.randomAlphabetic(6)+RandomStringUtils.randomNumeric(8)+"@gmail.com";
            loginPage(name,randomEmail,password,phone,birthday);
            isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
            count++;
            if (count > 3) {
                break;
            }
        } while (isExisted);
        WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit dieu huong den trang login");
    }
    public void verifyValidEmail(String name,String email ,String password, String phone, String birthday, String urlRegister, String urlLogin) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        loginPage(name,email,password,phone,birthday);
        boolean isExisted = WebUI.getElementText(message).equals("Email đã tồn tại !");
        if (isExisted) {
            WebUI.assertEquals(WebUI.getURL(),urlRegister,"Email đã tồn tại !");
        } else {
            WebUI.assertEquals(WebUI.getURL(),urlLogin,"Submit dieu huong den trang login");
        }
    }
    public void verifyErrorEmpty() {
        WebUI.clearActualTexts();
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        loginPage("","","","","");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.logConsole(">>>>> Kiem Tra So Luong Hien Thi - Length 6 <<<<<");
        for (int i = 0; i < 6; i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/verifyErrorWhenAllFieldsEmpty.csv");
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi <<<<<");
        for (int i=0; i< WebUI.sizeActualTexts(); i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Verify Error When All Fields Empty")
                .addScreenCaptureFromPath(shot1,"Verify Error When All Fields Empty");
    }
    public void verifyErrorSpace() {
        WebUI.clearActualTexts();
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        loginPage("        ","        ","        ","        ","        ");
        ExtentTest test = ExtentTestManager.getTest();
        String shot1 = WebUI.captureScreenshot();
        WebUI.logConsole(">>>>> Kiem Tra So Luong Hien Thi - Length 6 <<<<<");
        for (int i = 0; i < 6; i++) {
            WebUI.assertDisplay(messageError,i);
        }
        List<String> expectedTexts = CSVReaderUtilForList.readCSVAsList("src/main/java/testdata/data_Register/verifyErrorWhenInputIsSpace.csv");
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi <<<<<");
        for (int i=0; i< WebUI.sizeActualTexts(); i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
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
        WebUI.logConsole(">>>>> Kiem Tra Noi Dung Hien Thi - Length 7 <<<<<");
        for (int i=0; i < 7; i++) {
            WebUI.assertEquals(WebUI.getActualText(i),expectedTexts.get(i),"Hien thi dung noi dung - "+expectedTexts.get(i));
        }
        test.log(Status.INFO, "📸 Verify Tool Tip")
                .addScreenCaptureFromPath(shot1,"Name")
                .addScreenCaptureFromPath(shot2,"Email")
                .addScreenCaptureFromPath(shot3,"Password")
                .addScreenCaptureFromPath(shot4,"RepeatPassword")
                .addScreenCaptureFromPath(shot5,"Phone")
                .addScreenCaptureFromPath(shot6,"Birthday")
                .addScreenCaptureFromPath(shot7,"Agree");
    }
    public void verifyRepeatPassword(String name, String email, String phone, String birthday, String pass,String repeatpass,String error) {
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

    public void verifyInValid(String name,String email ,String password, String phone, String birthday, String error) {
        WebUI.openURL("https://demo5.cybersoft.edu.vn/register");
        loginPage(name,email,password,phone,birthday);
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
