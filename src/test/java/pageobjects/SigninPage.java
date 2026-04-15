package pageobjects;

import org.openqa.selenium.By;
<<<<<<< HEAD
import org.openqa.selenium.StaleElementReferenceException;
=======
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SigninPage extends BasePage {
<<<<<<< HEAD
	private static final By USERNAME_INPUT = By.xpath("//input[@name='username' or contains(@name,'user') or contains(@id,'user')]");
	private static final By PASSWORD_INPUT = By.xpath("//input[@name='password' or contains(@name,'pass') or contains(@id,'pass')]");
	private static final By SIGNIN_BUTTON = By.xpath("//button[normalize-space()='Sign in' or normalize-space()='Signin' or contains(normalize-space(),'Sign')]");
=======
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f

	public SigninPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath ="//input[@name='username']")
	WebElement Username;
	
	@FindBy(xpath = "//input[@name='password']")
	WebElement Password;
	
	@FindBy(xpath ="//button[normalize-space()='Sign in']")
	WebElement Signin;
	
	// Error message elements - common selectors for error messages
	@FindBy(xpath = "//div[contains(@class,'error') or contains(@class,'alert') or contains(@class,'danger')]")
	List<WebElement> ErrorMessages;
	
	@FindBy(xpath = "//div[contains(@class,'alert')]")
	List<WebElement> AlertMessages;
	
	@FindBy(xpath = "//span[contains(@class,'error')]")
	List<WebElement> FieldErrors;
	
	// Page elements
	@FindBy(xpath = "//input[@name='username']")
	WebElement UsernameField;
	
	@FindBy(xpath = "//input[@name='password']")
	WebElement PasswordField;
	
	public void setusername(String username)
	{
<<<<<<< HEAD
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		for (int attempt = 1; attempt <= 2; attempt++) {
			try {
				WebElement usernameInput = wait.until(
						ExpectedConditions.elementToBeClickable(USERNAME_INPUT));
				usernameInput.clear();
				usernameInput.sendKeys(username);
				return;
			} catch (StaleElementReferenceException e) {
				if (attempt == 2) {
					throw e;
				}
			}
		}
=======
		Username.clear();
		Username.sendKeys(username);
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
	}
	
	public void setpassword(String password)
	{
<<<<<<< HEAD
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		for (int attempt = 1; attempt <= 2; attempt++) {
			try {
				WebElement passwordInput = wait.until(
						ExpectedConditions.elementToBeClickable(PASSWORD_INPUT));
				passwordInput.clear();
				passwordInput.sendKeys(password);
				return;
			} catch (StaleElementReferenceException e) {
				if (attempt == 2) {
					throw e;
				}
			}
		}
=======
		Password.clear();
		Password.sendKeys(password);
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
	}
	
	public void clicksignin()
	{
<<<<<<< HEAD
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		for (int attempt = 1; attempt <= 2; attempt++) {
			try {
				WebElement signInButton = wait.until(
						ExpectedConditions.elementToBeClickable(SIGNIN_BUTTON));
				signInButton.click();
				return;
			} catch (StaleElementReferenceException e) {
				if (attempt == 2) {
					throw e;
				}
			}
		}
=======
		Signin.click();
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
	}
	
	// Clear username field
	public void clearUsername() {
<<<<<<< HEAD
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(USERNAME_INPUT));
		usernameInput.clear();
=======
		Username.clear();
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
	}
	
	// Clear password field
	public void clearPassword() {
<<<<<<< HEAD
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(PASSWORD_INPUT));
		passwordInput.clear();
=======
		Password.clear();
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
	}
	
	// Clear both fields
	public void clearFields() {
		clearUsername();
		clearPassword();
	}
	
	// Check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class,'error') or contains(@class,'alert') or contains(@class,'danger')]")));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// Get error message text
	public String getErrorMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[contains(@class,'error') or contains(@class,'alert') or contains(@class,'danger')]")));
			
			// Try to find error message in various locations
			List<WebElement> errors = driver.findElements(
				By.xpath("//div[contains(@class,'error') or contains(@class,'alert') or contains(@class,'danger')]"));
			
			if (!errors.isEmpty()) {
				return errors.get(0).getText().trim();
			}
			
			// Try alert messages
			List<WebElement> alerts = driver.findElements(By.xpath("//div[contains(@class,'alert')]"));
			if (!alerts.isEmpty()) {
				return alerts.get(0).getText().trim();
			}
			
			// Try field-level errors
			List<WebElement> fieldErrors = driver.findElements(By.xpath("//span[contains(@class,'error')]"));
			if (!fieldErrors.isEmpty()) {
				return fieldErrors.get(0).getText().trim();
			}
			
			return "";
		} catch (Exception e) {
			return "";
		}
	}
	
	// Check if still on login page
	public boolean isOnLoginPage() {
		try {
			String currentUrl = driver.getCurrentUrl().toLowerCase();
			return currentUrl.contains("login");
		} catch (Exception e) {
			return false;
		}
	}
	
	// Check if username field is displayed
	public boolean isUsernameFieldDisplayed() {
		try {
			return UsernameField.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	// Check if password field is displayed
	public boolean isPasswordFieldDisplayed() {
		try {
			return PasswordField.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	// Check if signin button is enabled
	public boolean isSigninButtonEnabled() {
		try {
			return Signin.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}
	
	// Get username field value
	public String getUsernameValue() {
		try {
			return UsernameField.getAttribute("value");
		} catch (Exception e) {
			return "";
		}
	}
	
	// Get password field value (usually empty for security)
	public String getPasswordValue() {
		try {
			return PasswordField.getAttribute("value");
		} catch (Exception e) {
			return "";
		}
	}
	
	// Check if username field has required attribute
	public boolean isUsernameRequired() {
		try {
			String required = UsernameField.getAttribute("required");
			return required != null && required.equals("true");
		} catch (Exception e) {
			return false;
		}
	}
	
	// Check if password field has required attribute
	public boolean isPasswordRequired() {
		try {
			String required = PasswordField.getAttribute("required");
			return required != null && required.equals("true");
		} catch (Exception e) {
			return false;
		}
	}

}
