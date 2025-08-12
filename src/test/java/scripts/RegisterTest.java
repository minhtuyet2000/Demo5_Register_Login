package scripts;

import drivers.DriverManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegisterPage;
import pages.webui.WebUI;
import scripts.utils.CSVReaderUtil;

public class RegisterTest extends BaseTest {
    private RegisterPage registerPage;
    @Test(priority = 1, description = "Kiểm tra hiển thị trang đăng kí", groups = {"RegisterTest"})
    public void testDisplayRegister() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.displayRegister();
        WebUI.assertAll();
    }

    @Test(priority = 2, description = "Kiểm tra tương tác trang đăng kí", groups = {"RegisterTest"})
    public void testInteractRegister() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.interactRegister("Minh Tuyet","minh0842@gmail.com","Minh084@",
                "0840000000","19082000");
        WebUI.assertAll();
    }

    @Test(priority = 3, description = "Kiểm tra đăng kí thành công với dữ liệu hợp lệ", groups = {"RegisterTest"})
    public void testMessageSuccess() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyMessageSuccess("Minh Tuyet","Minh084@","0840000000","19082000",
                "Đăng kí tài khoản thành công !","https://demo5.cybersoft.edu.vn/login");
        WebUI.assertAll();
    }

    @Test(priority = 4, description = "Kiểm tra đăng kí hiển thị thông báo lỗi khi bỏ trống", groups = {"RegisterTest"})
    public void testErrorEmpty() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyErrorEmpty();
        WebUI.assertAll();
    }

    @Test(priority = 5, description = "Kiểm tra đăng kí hiển thị ToolTip khi bỏ trống dữ liệu", groups = {"RegisterTest"})
    public void testToolTip() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyToolTip("Minh Tuyet","minh0842@gmail.com","Minh084@",
                "0840000000","19082000");
        WebUI.assertAll();
    }

    @Test(priority = 6, description = "Kiểm tra đăng kí hiển thị thông báo lỗi khi nhập dấu cách vào trường", groups = {"RegisterTest"})
    public void testErrorSpace() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyErrorSpace();
        WebUI.assertAll();
    }

    @DataProvider(name = "csvRepeatPassword")
    public Object[][] getRepeatPassword() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyRepeatPassword");
    }
    @Test(priority = 7,dataProvider = "csvRepeatPassword",
            description = "Kiểm tra đăng kí hiển thị thông báo lỗi khi password khong trung nhau", groups = {"RegisterTest"})
    public void testRepeatPassword(String pass,String repeatpass) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyRepeatPassword("Minh Tuyet","minh0842@gmail.com","0840000000",
                "19082000", pass,repeatpass,"Password phải trùng nhau");
        WebUI.assertAll();
    }

    @Test(priority = 8, description = "Kiểm tra đăng kí thành công với trường name hợp lệ", groups = {"RegisterTest"})
    public void testValidName() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyValid("Minh Tuyế't","Minh084@",
                "0840000000", "19082000","https://demo5.cybersoft.edu.vn/login");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvValidEmail")
    public Object[][] getValidEmail() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyValidEmail");
    }
    @Test(priority = 9,dataProvider = "csvValidEmail",
            description = "Kiểm tra đăng kí thành công với trường email hợp lệ", groups = {"RegisterTest"})
    public void testValidEmail(String email) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyValidEmail("Minh Tuyet",email,"Minh084@","0840000000","19082000",
                "https://demo5.cybersoft.edu.vn/register","https://demo5.cybersoft.edu.vn/login");
        WebUI.assertAll();
    }

    @Test(priority = 10, description = "Kiểm tra đăng kí thành công với trường password hợp lệ", groups = {"RegisterTest"})
    public void testValidPassword() {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyValid("Minh Tuyet","Tu ế[8,@𝔂𝓪𝓼𝓼正😊",
                "0840000000", "19082000","https://demo5.cybersoft.edu.vn/login");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvValidPhone")
    public Object[][] getValidPhone() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyValidPhone.csv");
    }
    @Test(priority = 11,dataProvider = "csvValidPhone",
            description = "Kiểm tra đăng kí thành công với trường phone hợp lệ", groups = {"RegisterTest"})
    public void testValidPhone(String phone) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyValid("Minh Tuyet","Minh084@",
                phone, "19082000","https://demo5.cybersoft.edu.vn/login");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidName")
    public Object[][] getInvalidName() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyInvalidName.csv");
    }
    @Test(priority = 12,dataProvider = "csvInvalidName",
            description = "Kiểm tra trường name hiển thị thông báo lỗi với dữ liệu không hợp lệ", groups = {"RegisterTest"})
    public void testInvalidName(String name) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyInValid(name,"minh0842@gmail.com", "Minh084@",
                "0840000000","19082000","Name Không đúng định dạng");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidEmail")
    public Object[][] getInvalidEmail() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyInvalidEmail.csv");
    }
    @Test(priority = 13,dataProvider = "csvInvalidEmail",
            description = "Kiểm tra trường email hiển thị thông báo lỗi với dữ liệu không hợp lệ", groups = {"RegisterTest"})
    public void testInvalidEmail(String email) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyInValid("Minh Tuyet",email, "Minh084@",
                "0840000000","19082000","Email không đúng định dạng");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidPassword")
    public Object[][] getInvalidPassword() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyInvalidPassword.csv");
    }
    @Test(priority = 14,dataProvider = "csvInvalidPassword",
            description = "Kiểm tra trường password hiển thị thông báo lỗi với dữ liệu không hợp lệ", groups = {"RegisterTest"})
    public void testInvalidPassword(String password) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyInValid("Minh Tuyet","minh0842@gmail.com", password,
                "0840000000","19082000","");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidPhone")
    public Object[][] getInvalidPhone() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyInvalidPhone.csv");
    }
    @Test(priority = 15,dataProvider = "csvInvalidPhone",
            description = "Kiểm tra trường phone hiển thị thông báo lỗi với dữ liệu không hợp lệ", groups = {"RegisterTest"})
    public void testInvalidPhone(String phone) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyInValid("Minh Tuyet","minh0842@gmail.com", "Minh084@",
                phone,"19082000","Phone phải từ 03 05 07 08 09 và có 10 số");
        WebUI.assertAll();
    }

    @DataProvider(name = "csvInvalidBirthday")
    public Object[][] getInvalidBirthday() {
        return CSVReaderUtil.readCSV("src/main/java/testdata/data_Register/verifyInvalidBirthday.csv");
    }
    @Test(priority = 16,dataProvider = "csvInvalidBirthday",
            description = "Kiểm tra trường birthday hiển thị thông báo lỗi với dữ liệu không hợp lệ", groups = {"RegisterTest"})
    public void testInvalidBirthday(String birthday) {
        registerPage = new RegisterPage(DriverManager.getDriver());
        registerPage.verifyInValid("Minh Tuyet","minh0842@gmail.com", "Minh084@",
                "0840000000",birthday,"Birthday không được bỏ trống");
        WebUI.assertAll();
    }
}
