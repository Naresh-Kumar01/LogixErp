package pages.auth;

import helpers.WaitHelper;
import locators.LocatorRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.common.BasePage;
import pageobjects.HomePage;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        type("login.username", username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type("login.password", password);
        return this;
    }

    public HomePage clickSignIn() {
        click("login.submit");
        return new HomePage(driver());
    }

    public boolean isOnLoginPage() {
        return driver().getCurrentUrl().toLowerCase().contains("login");
    }

    public boolean isPasswordMasked() {
        WebElement password = WaitHelper.waitForVisible(LocatorRepository.resolve("login.password"));
        return "password".equalsIgnoreCase(password.getAttribute("type"));
    }

    public String getErrorMessage() {
        try {
            return WaitHelper.waitForVisible(
                    By.xpath("//motion.div[contains(@class,'text-red-600')] | //div[contains(@class,'error') or contains(@class,'alert')]"))
                    .getText().trim();
        } catch (Exception ex) {
            return "";
        }
    }

    public LoginPage logoutIfLoggedIn() {
        try {
            driver().manage().deleteAllCookies();
            driver().navigate().refresh();
        } catch (Exception ignored) {
        }
        return this;
    }
}
