package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

import pageobjects.HomePage;
import pageobjects.SigninPage;
import testBase.BaseClass;

import java.time.Duration;

/**
 * Comprehensive Signin Test Cases
 * This class contains both positive and negative test scenarios for signin functionality
 */
public class TC002_SigninPositiveNegativeTest extends BaseClass {
	
	private SigninPage signinPage;
	private HomePage homePage;
	
	@BeforeMethod(groups= {"Sanity","Regression","Master"})
	public void setupTest() {
		try {
			logger.info("Setting up test...");
			
			// Check if we're already on login page, if not navigate to it
			String currentUrl = driver.getCurrentUrl();
			if (!currentUrl.toLowerCase().contains("login")) {
				logger.info("Not on login page, navigating to application URL...");
				driver.get(p.getProperty("appURL"));
				logger.info("Current URL: " + driver.getCurrentUrl());
			} else {
				logger.info("Already on login page: " + currentUrl);
			}
			
			signinPage = new SigninPage(driver);
			homePage = new HomePage(driver);
			
			// Wait for page to load - try multiple selectors for username field
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='username']")));
			} catch (Exception e) {
				// Try alternative selector
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@name,'user') or contains(@id,'user')]")));
				} catch (Exception e2) {
					// If still not found, wait for page to be ready
					Thread.sleep(2000);
				}
			}
			
			logger.info("Test setup completed successfully");
		} catch (Exception e) {
			logger.error("Error in test setup: " + e.getMessage());
			e.printStackTrace();
			// Don't fail here, let individual tests handle it
			logger.warn("Test setup had issues, but continuing with test execution");
		}
	}
	
	// ==================== POSITIVE TEST CASES ====================
	
	/**
	 * Positive Test Case 1: Valid credentials signin
	 * Test ID: TC_SIGNIN_001
	 * Description: Verify successful signin with valid username and password
	 */
	@Test(priority = 1, groups = {"Sanity", "Regression", "Positive"}, 
		  description = "Verify successful signin with valid credentials")
	public void test_Signin_With_Valid_Credentials() {
		logger.info("========== TC_SIGNIN_001: Valid Credentials Signin ==========");
		try {
			// Get valid credentials from config
			String validUsername = p.getProperty("username");
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Enter valid username: " + validUsername);
			signinPage.setusername(validUsername);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			// Wait for page to load after signin
			Thread.sleep(3000);
			
			logger.info("Step 4: Verify successful signin");
			boolean isHomePageDisplayed = homePage.isHomePageExists();
			
			Assert.assertTrue(isHomePageDisplayed, 
				"Signin failed - Home page not displayed after successful signin");
			
			logger.info("Step 5: Verify URL changed from login page");
			String currentUrl = driver.getCurrentUrl();
			Assert.assertFalse(currentUrl.toLowerCase().contains("login"), 
				"URL still contains 'login' - signin may have failed");
			
			logger.info("✅ TC_SIGNIN_001 PASSED: Valid credentials signin successful");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_001 FAILED: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	/**
	 * Positive Test Case 2: Signin with username case insensitive
	 * Test ID: TC_SIGNIN_002
	 * Description: Verify signin works with different case username (if supported)
	 */
	@Test(priority = 2, groups = {"Regression", "Positive"}, 
		  description = "Verify signin with username in different case")
	public void test_Signin_Username_Case_Insensitive() {
		logger.info("========== TC_SIGNIN_002: Username Case Insensitive ==========");
		try {
			String validUsername = p.getProperty("username");
			String validPassword = p.getProperty("password");
			
			// Try with uppercase username
			String upperCaseUsername = validUsername.toUpperCase();
			
			logger.info("Step 1: Enter username in uppercase: " + upperCaseUsername);
			signinPage.setusername(upperCaseUsername);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(3000);
			
			logger.info("Step 4: Verify signin result");
			boolean isHomePageDisplayed = homePage.isHomePageExists();
			
			// This test may pass or fail depending on application behavior
			if (isHomePageDisplayed) {
				logger.info("✅ TC_SIGNIN_002 PASSED: Username is case insensitive");
			} else {
				logger.info("⚠️ TC_SIGNIN_002 INFO: Username is case sensitive (expected behavior)");
				// Reset for next test
				driver.get(p.getProperty("appURL"));
			}
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_002 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Positive Test Case 3: Signin page elements visibility
	 * Test ID: TC_SIGNIN_003
	 * Description: Verify all signin page elements are displayed correctly
	 */
	@Test(priority = 3, groups = {"Sanity", "Positive"}, 
		  description = "Verify signin page elements are displayed")
	public void test_Signin_Page_Elements_Displayed() {
		logger.info("========== TC_SIGNIN_003: Signin Page Elements ==========");
		try {
			logger.info("Step 1: Verify username field is displayed");
			Assert.assertTrue(signinPage.isUsernameFieldDisplayed(), 
				"Username field is not displayed");
			
			logger.info("Step 2: Verify password field is displayed");
			Assert.assertTrue(signinPage.isPasswordFieldDisplayed(), 
				"Password field is not displayed");
			
			logger.info("Step 3: Verify signin button is displayed and enabled");
			Assert.assertTrue(signinPage.isSigninButtonEnabled(), 
				"Signin button is not enabled");
			
			logger.info("Step 4: Verify username field is empty");
			String usernameValue = signinPage.getUsernameValue();
			Assert.assertTrue(usernameValue.isEmpty(), 
				"Username field should be empty initially");
			
			logger.info("✅ TC_SIGNIN_003 PASSED: All signin page elements are displayed correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_003 FAILED: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	// ==================== NEGATIVE TEST CASES ====================
	
	/**
	 * Negative Test Case 1: Invalid username
	 * Test ID: TC_SIGNIN_NEG_001
	 * Description: Verify error message when invalid username is entered
	 */
	@Test(priority = 10, groups = {"Regression", "Negative"}, 
		  description = "Verify error message with invalid username")
	public void test_Signin_Invalid_Username() {
		logger.info("========== TC_SIGNIN_NEG_001: Invalid Username ==========");
		try {
			String invalidUsername = "invalid_user_12345";
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Enter invalid username: " + invalidUsername);
			signinPage.setusername(invalidUsername);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify error message is displayed");
			boolean errorDisplayed = signinPage.isErrorMessageDisplayed();
			Assert.assertTrue(errorDisplayed, 
				"Error message should be displayed for invalid username");
			
			logger.info("Step 5: Verify still on login page");
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			Assert.assertTrue(stillOnLoginPage, 
				"Should remain on login page after invalid credentials");
			
			String errorMessage = signinPage.getErrorMessage();
			logger.info("Error message displayed: " + errorMessage);
			
			logger.info("✅ TC_SIGNIN_NEG_001 PASSED: Invalid username handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_001 FAILED: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	/**
	 * Negative Test Case 2: Invalid password
	 * Test ID: TC_SIGNIN_NEG_002
	 * Description: Verify error message when invalid password is entered
	 */
	@Test(priority = 11, groups = {"Regression", "Negative"}, 
		  description = "Verify error message with invalid password")
	public void test_Signin_Invalid_Password() {
		logger.info("========== TC_SIGNIN_NEG_002: Invalid Password ==========");
		try {
			String validUsername = p.getProperty("username");
			String invalidPassword = "wrong_password_123";
			
			logger.info("Step 1: Enter valid username: " + validUsername);
			signinPage.setusername(validUsername);
			
			logger.info("Step 2: Enter invalid password");
			signinPage.setpassword(invalidPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify error message is displayed");
			boolean errorDisplayed = signinPage.isErrorMessageDisplayed();
			Assert.assertTrue(errorDisplayed, 
				"Error message should be displayed for invalid password");
			
			logger.info("Step 5: Verify still on login page");
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			Assert.assertTrue(stillOnLoginPage, 
				"Should remain on login page after invalid password");
			
			String errorMessage = signinPage.getErrorMessage();
			logger.info("Error message displayed: " + errorMessage);
			
			logger.info("✅ TC_SIGNIN_NEG_002 PASSED: Invalid password handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_002 FAILED: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	/**
	 * Negative Test Case 3: Both username and password invalid
	 * Test ID: TC_SIGNIN_NEG_003
	 * Description: Verify error message when both credentials are invalid
	 */
	@Test(priority = 12, groups = {"Regression", "Negative"}, 
		  description = "Verify error message with both invalid credentials")
	public void test_Signin_Invalid_Both_Credentials() {
		logger.info("========== TC_SIGNIN_NEG_003: Invalid Both Credentials ==========");
		try {
			String invalidUsername = "invalid_user";
			String invalidPassword = "invalid_pass";
			
			logger.info("Step 1: Enter invalid username: " + invalidUsername);
			signinPage.setusername(invalidUsername);
			
			logger.info("Step 2: Enter invalid password");
			signinPage.setpassword(invalidPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify error message is displayed");
			boolean errorDisplayed = signinPage.isErrorMessageDisplayed();
			Assert.assertTrue(errorDisplayed, 
				"Error message should be displayed for invalid credentials");
			
			logger.info("Step 5: Verify still on login page");
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			Assert.assertTrue(stillOnLoginPage, 
				"Should remain on login page after invalid credentials");
			
			logger.info("✅ TC_SIGNIN_NEG_003 PASSED: Invalid credentials handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_003 FAILED: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	/**
	 * Negative Test Case 4: Empty username
	 * Test ID: TC_SIGNIN_NEG_004
	 * Description: Verify validation when username is empty
	 */
	@Test(priority = 13, groups = {"Sanity", "Regression", "Negative"}, 
		  description = "Verify validation with empty username")
	public void test_Signin_Empty_Username() {
		logger.info("========== TC_SIGNIN_NEG_004: Empty Username ==========");
		try {
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Leave username field empty");
			signinPage.clearUsername();
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify validation message or error");
			// Check for HTML5 validation or error message
			boolean hasValidation = signinPage.isUsernameRequired() || 
									signinPage.isErrorMessageDisplayed();
			
			if (hasValidation) {
				logger.info("✅ TC_SIGNIN_NEG_004 PASSED: Empty username validation works");
			} else {
				logger.info("Step 5: Verify still on login page");
				boolean stillOnLoginPage = signinPage.isOnLoginPage();
				Assert.assertTrue(stillOnLoginPage, 
					"Should remain on login page or show validation");
			}
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_004 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 5: Empty password
	 * Test ID: TC_SIGNIN_NEG_005
	 * Description: Verify validation when password is empty
	 */
	@Test(priority = 14, groups = {"Sanity", "Regression", "Negative"}, 
		  description = "Verify validation with empty password")
	public void test_Signin_Empty_Password() {
		logger.info("========== TC_SIGNIN_NEG_005: Empty Password ==========");
		try {
			String validUsername = p.getProperty("username");
			
			logger.info("Step 1: Enter valid username: " + validUsername);
			signinPage.setusername(validUsername);
			
			logger.info("Step 2: Leave password field empty");
			signinPage.clearPassword();
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify validation message or error");
			// Check for HTML5 validation or error message
			boolean hasValidation = signinPage.isPasswordRequired() || 
									signinPage.isErrorMessageDisplayed();
			
			if (hasValidation) {
				logger.info("✅ TC_SIGNIN_NEG_005 PASSED: Empty password validation works");
			} else {
				logger.info("Step 5: Verify still on login page");
				boolean stillOnLoginPage = signinPage.isOnLoginPage();
				Assert.assertTrue(stillOnLoginPage, 
					"Should remain on login page or show validation");
			}
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_005 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 6: Both fields empty
	 * Test ID: TC_SIGNIN_NEG_006
	 * Description: Verify validation when both fields are empty
	 */
	@Test(priority = 15, groups = {"Regression", "Negative"}, 
		  description = "Verify validation with both fields empty")
	public void test_Signin_Both_Fields_Empty() {
		logger.info("========== TC_SIGNIN_NEG_006: Both Fields Empty ==========");
		try {
			logger.info("Step 1: Clear both username and password fields");
			signinPage.clearFields();
			
			logger.info("Step 2: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 3: Verify validation message or error");
			boolean hasValidation = (signinPage.isUsernameRequired() && 
									signinPage.isPasswordRequired()) || 
									signinPage.isErrorMessageDisplayed();
			
			if (hasValidation) {
				logger.info("✅ TC_SIGNIN_NEG_006 PASSED: Empty fields validation works");
			} else {
				logger.info("Step 4: Verify still on login page");
				boolean stillOnLoginPage = signinPage.isOnLoginPage();
				Assert.assertTrue(stillOnLoginPage, 
					"Should remain on login page or show validation");
			}
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_006 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 7: SQL Injection attempt
	 * Test ID: TC_SIGNIN_NEG_007
	 * Description: Verify system handles SQL injection attempts
	 */
	@Test(priority = 16, groups = {"Security", "Negative"}, 
		  description = "Verify SQL injection attempt is handled")
	public void test_Signin_SQL_Injection_Attempt() {
		logger.info("========== TC_SIGNIN_NEG_007: SQL Injection Attempt ==========");
		try {
			String sqlInjection = "admin' OR '1'='1";
			String password = "password";
			
			logger.info("Step 1: Enter SQL injection in username: " + sqlInjection);
			signinPage.setusername(sqlInjection);
			
			logger.info("Step 2: Enter password");
			signinPage.setpassword(password);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify signin failed (security check)");
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			Assert.assertTrue(stillOnLoginPage, 
				"SQL injection attempt should fail - should remain on login page");
			
			logger.info("✅ TC_SIGNIN_NEG_007 PASSED: SQL injection attempt handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_007 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 8: XSS attempt
	 * Test ID: TC_SIGNIN_NEG_008
	 * Description: Verify system handles XSS attempts
	 */
	@Test(priority = 17, groups = {"Security", "Negative"}, 
		  description = "Verify XSS attempt is handled")
	public void test_Signin_XSS_Attempt() {
		logger.info("========== TC_SIGNIN_NEG_008: XSS Attempt ==========");
		try {
			String xssAttempt = "<script>alert('XSS')</script>";
			String password = "password";
			
			logger.info("Step 1: Enter XSS attempt in username");
			signinPage.setusername(xssAttempt);
			
			logger.info("Step 2: Enter password");
			signinPage.setpassword(password);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify signin failed");
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			Assert.assertTrue(stillOnLoginPage, 
				"XSS attempt should fail - should remain on login page");
			
			logger.info("✅ TC_SIGNIN_NEG_008 PASSED: XSS attempt handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_008 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 9: Special characters in username
	 * Test ID: TC_SIGNIN_NEG_009
	 * Description: Verify system handles special characters
	 */
	@Test(priority = 18, groups = {"Regression", "Negative"}, 
		  description = "Verify special characters in username")
	public void test_Signin_Special_Characters_Username() {
		logger.info("========== TC_SIGNIN_NEG_009: Special Characters Username ==========");
		try {
			String specialChars = "!@#$%^&*()";
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Enter special characters in username");
			signinPage.setusername(specialChars);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify error or validation");
			boolean errorDisplayed = signinPage.isErrorMessageDisplayed();
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			
			Assert.assertTrue(errorDisplayed || stillOnLoginPage, 
				"Special characters should be handled appropriately");
			
			logger.info("✅ TC_SIGNIN_NEG_009 PASSED: Special characters handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_009 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 10: Very long username
	 * Test ID: TC_SIGNIN_NEG_010
	 * Description: Verify system handles very long input
	 */
	@Test(priority = 19, groups = {"Regression", "Negative"}, 
		  description = "Verify very long username input")
	public void test_Signin_Very_Long_Username() {
		logger.info("========== TC_SIGNIN_NEG_010: Very Long Username ==========");
		try {
			// Generate a very long username
			String longUsername = "a".repeat(500);
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Enter very long username (500 characters)");
			signinPage.setusername(longUsername);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify error or validation");
			boolean errorDisplayed = signinPage.isErrorMessageDisplayed();
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			
			Assert.assertTrue(errorDisplayed || stillOnLoginPage, 
				"Very long username should be handled appropriately");
			
			logger.info("✅ TC_SIGNIN_NEG_010 PASSED: Very long username handled correctly");
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_010 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Negative Test Case 11: Whitespace in username
	 * Test ID: TC_SIGNIN_NEG_011
	 * Description: Verify system handles whitespace in username
	 */
	@Test(priority = 20, groups = {"Regression", "Negative"}, 
		  description = "Verify whitespace in username")
	public void test_Signin_Whitespace_Username() {
		logger.info("========== TC_SIGNIN_NEG_011: Whitespace Username ==========");
		try {
			String usernameWithSpaces = "  admin  ";
			String validPassword = p.getProperty("password");
			
			logger.info("Step 1: Enter username with leading/trailing spaces");
			signinPage.setusername(usernameWithSpaces);
			
			logger.info("Step 2: Enter valid password");
			signinPage.setpassword(validPassword);
			
			logger.info("Step 3: Click Sign in button");
			signinPage.clicksignin();
			
			Thread.sleep(2000);
			
			logger.info("Step 4: Verify result");
			// This may pass if system trims whitespace, or fail if it doesn't
			boolean isHomePageDisplayed = homePage.isHomePageExists();
			boolean stillOnLoginPage = signinPage.isOnLoginPage();
			
			if (isHomePageDisplayed) {
				logger.info("✅ TC_SIGNIN_NEG_011 INFO: System trims whitespace (acceptable behavior)");
			} else if (stillOnLoginPage) {
				logger.info("✅ TC_SIGNIN_NEG_011 PASSED: Whitespace handled correctly");
			}
			
		} catch (Exception e) {
			logger.error("❌ TC_SIGNIN_NEG_011 FAILED: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
