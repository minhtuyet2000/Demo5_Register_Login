package scripts;

import drivers.DriverManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.webui.WebUI;
import scripts.utils.CSVReaderUtil;

import static scripts.utils.CSVReaderUtil.getRandomFromCSV;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    @Test(priority = 1, description = "Kiểm tra hiển thị trang đăng nhập", groups = {"LoginTest"})
    public void testDisplayLogin() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.displayLogin();
        WebUI.assertAll();
    }

    @Test(priority = 2, description = "Kiểm tra tương tác trang đăng nhập", groups = {"LoginTest"})
    public void testInteractLogin() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.interactLogin();
        WebUI.assertAll();
    }

    @Test(priority = 3, description = "Kiểm tra đăng nhập thành công bằng phím 'enter'", groups = {"LoginTest"})
    public void testByEnterKey() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyByEnterKey("hinh123%@gmail.com"," Tyt @12 ","Đăng nhập tài khoản thành công !","https://demo5.cybersoft.edu.vn/profile");
        WebUI.assertAll();
    }

    @Test(priority = 4, description = "Kiểm tra đăng nhập hiển thị thông báo lỗi khi bỏ trống", groups = {"LoginTest"})
    public void testErrorEmpty() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyErrorEmpty();
        WebUI.assertAll();
    }

    @Test(priority = 5, description = "Kiểm tra đăng nhập hiển thị ToolTip khi bỏ trống dữ liệu", groups = {"LoginTest"})
    public void testToolTip() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyToolTip();
        WebUI.assertAll();
    }

    @Test(priority = 6, description = "Kiểm tra đăng nhập hiển thị thông báo lỗi khi nhập dấu cách vào trường email", groups = {"LoginTest"})
    public void testErrorSpaceEmail() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyInvalid("        "," Tyt @12 ","Email không đúng định dạng !","");
        WebUI.assertAll();
    }

    @Test(priority = 7, description = "Kiểm tra đăng nhập hiển thị thông báo lỗi khi nhập dấu cách vào trường password", groups = {"LoginTest"})
    public void testErrorSpacePassword() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyInvalid("minh123%@gmail.com","        ","Email hoặc mật khẩu không đúng !","Email hoặc mật khẩu không đúng !");
        WebUI.assertAll();
    }

    @Test(priority = 8, description = "Kiểm tra đăng nhập thành công với trường name hợp lệ", groups = {"LoginTest"})
    public void testValidName() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyValidName("Minh Tuyế't"," Tyt @12 ",
                "0840000000", "19082000","https://demo5.cybersoft.edu.vn/profile");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvValidEmail")
    public Object[][] getValidEmail() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyValidEmail");
    }
    @Test(priority = 9,dataProvider = "csvValidEmail",
            description = "Kiểm tra đăng nhập thành công với trường email hợp lệ", groups = {"LoginTest"})
    public void testValidEmail(String email) {
        //registerTest.testValidEmail
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyValid(email,"Minh084@","https://demo5.cybersoft.edu.vn/profile");
        WebUI.assertAll();
    }

    @Test(priority = 10, description = "Kiểm tra đăng nhập thành công với trường password hợp lệ", groups = {"LoginTest"})
    public void testValidPassword() {
        //registerTest.testValidPassword
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyValid("CfnEzg79089476@gmail.com","Tu ế[8,@𝔂𝓪𝓼𝓼正😊","https://demo5.cybersoft.edu.vn/profile");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidEmail")
    public Object[][] getInvalidEmail() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Login/verifyInvalidEmail.csv");
    }
    @Test(priority = 11,dataProvider = "csvInvalidEmail",
            description = "Kiểm tra đăng nhập hiển thị thông báo lỗi với email không chính xác", groups = {"LoginTest"})
    public void testInvalidEmail(String email) {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyInvalid(email," Tyt @12 ","Email hoặc mật khẩu không đúng !","Email hoặc mật khẩu không đúng !");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidPassword")
    public Object[][] getInvalidPassword() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Login/verifyInvalidPassword.csv");
    }
    @Test(priority = 12,dataProvider = "csvInvalidPassword",
            description = "Kiểm tra đăng nhập hiển thị thông báo lỗi với password không chính xác", groups = {"LoginTest"})
    public void testInvalidPassword(String password) {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyInvalid("hinh123%@gmail.com",password+" ","Email hoặc mật khẩu không đúng !","Email hoặc mật khẩu không đúng !");
        WebUI.assertAll();
    }

    @DataProvider(name = "randomInvalidLogin")
    public Object[][] getRandomInvalidLogin() {
        int numberOfTests = 5;
        Object[][] data = new Object[numberOfTests][2];
        for (int i = 0; i < numberOfTests; i++) {
            Object[] randomEmailRow = getRandomFromCSV("src/main/java/testdata/data_Register/verifyInvalidEmail.csv");
            Object[] randomPassRow = getRandomFromCSV("src/main/java/testdata/data_Register/verifyInvalidPassword.csv");

            data[i][0] = randomEmailRow[0].toString();
            data[i][1] = randomPassRow[0].toString();
        }
        return data;
    }
    @Test(priority = 13,dataProvider = "randomInvalidLogin",
            description = "Kiểm tra đăng nhập thất bại với email và password không hợp lệ", groups = {"LoginTest"})
    public void testInvalidLogin(String email, String password) {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.verifyInvalid(email,password,
                "Email không đúng định dạng !","Pass từ 6 - 32 ký tự !");
        WebUI.assertAll();
    }
}

