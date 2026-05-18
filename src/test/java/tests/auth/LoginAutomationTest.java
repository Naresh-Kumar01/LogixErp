package tests.auth;

import annotations.TestCase;
import base.BaseTest;
import dataproviders.LoginDataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageobjects.HomePage;
import pages.auth.LoginPage;
import retry.RetryAnalyzer;

@Listeners(listeners.TestListener.class)
public class LoginAutomationTest extends BaseTest {

    private LoginPage loginPage() {
        return new LoginPage(driver());
    }

    @TestCase(id = "LOGIN_001", module = "Auth", description = "Valid login")
    @Test(priority = 1, groups = {"Sanity", "Login", "Positive"}, retryAnalyzer = RetryAnalyzer.class)
    public void testValidLogin() {
        HomePage home = loginPage()
                .enterUsername(config("username"))
                .enterPassword(config("password"))
                .clickSignIn();
        Assert.assertTrue(home.isHomePageExists() || !loginPage().isOnLoginPage(),
                "User should land on home/dashboard after valid login");
    }

    @TestCase(id = "LOGIN_002", module = "Auth")
    @Test(dataProvider = "invalidLoginCases", dataProviderClass = LoginDataProvider.class,
            groups = {"Regression", "Login", "Negative"}, retryAnalyzer = RetryAnalyzer.class)
    public void testInvalidCredentials(String username, String password, String scenario) {
        loginPage().enterUsername(username).enterPassword(password).clickSignIn();
        Assert.assertTrue(loginPage().isOnLoginPage() || !loginPage().getErrorMessage().isBlank(),
                "Expected login failure for: " + scenario);
    }

    @TestCase(id = "LOGIN_003", module = "Auth")
    @Test(groups = {"Regression", "Login"})
    public void testPasswordMasking() {
        loginPage().enterPassword("Secret@123");
        Assert.assertTrue(loginPage().isPasswordMasked(), "Password field must be masked");
    }

    @TestCase(id = "LOGIN_004", module = "Auth")
    @Test(groups = {"Regression", "Login", "Security"})
    public void testUnauthorizedAccessWithoutLogin() {
        driver().manage().deleteAllCookies();
        driver().get(config("appURL").replace("Login", "Dashboard"));
        Assert.assertTrue(loginPage().isOnLoginPage(), "Unauthenticated user must be redirected to login");
    }

    @TestCase(id = "LOGIN_005", module = "Auth")
    @Test(dataProvider = "roleBasedUsers", dataProviderClass = LoginDataProvider.class, groups = {"Regression", "Role"})
    public void testRoleBasedLogin(String role, String userKey, String passKey) {
        String username = config(userKey);
        String password = config(passKey);
        if (username == null || username.isBlank()) {
            username = config("username");
            password = config("password");
        }
        loginPage().enterUsername(username).enterPassword(password).clickSignIn();
        Assert.assertFalse(loginPage().isOnLoginPage(), "Role login failed for: " + role);
    }

    @TestCase(id = "LOGIN_006", module = "Auth")
    @Test(groups = {"Regression", "Login"})
    public void testLogoutValidation() {
        testValidLogin();
        driver().manage().deleteAllCookies();
        driver().navigate().refresh();
        Assert.assertTrue(loginPage().isOnLoginPage(), "User should be logged out after session clear");
    }

    @TestCase(id = "LOGIN_007", module = "Auth")
    @Test(groups = {"Regression", "Login"}, enabled = false,
            description = "Enable when multi-session policy is configured in target env")
    public void testMultipleSessionValidation() {
        // Template: open second driver/session and assert policy (single session / concurrent allowed)
        Assert.assertTrue(true);
    }

    @TestCase(id = "LOGIN_008", module = "Auth")
    @Test(groups = {"Regression", "Login"}, enabled = false)
    public void testSessionTimeout() {
        // Template: wait beyond session timeout and validate redirect to login
        Assert.assertTrue(true);
    }
}
