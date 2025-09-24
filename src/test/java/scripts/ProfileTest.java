package scripts;

import drivers.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import pages.LoginPage;
import pages.ProfilePage;
import pages.webui.WebUI;

public class ProfileTest extends BaseTest{
    private LoginPage loginPage;
    private ProfilePage profilePage;
    public void setUpPages() {
        loginPage = new LoginPage(DriverManager.getDriver());
        profilePage = loginPage.profilePage();
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("document.querySelector('header').style.display='none';");
        WebUI.sleep(1);
    }
}
