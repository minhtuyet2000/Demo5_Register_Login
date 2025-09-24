package pages;

import org.openqa.selenium.WebDriver;

public class ProfilePage {
    private WebDriver driver;
    private LoginPage loginPage;
    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        loginPage = new LoginPage(driver);
    }
}
