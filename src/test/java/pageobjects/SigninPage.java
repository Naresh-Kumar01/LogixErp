package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SigninPage extends BasePage {

	public SigninPage(WebDriver driver) {
		super(driver);
		
		
	}
	@FindBy(xpath ="//input[@name='username']")
	WebElement Username;
	@FindBy(xpath = "//input[@name='password']")
	WebElement Password;
	@FindBy(xpath ="//button[normalize-space()='Sign in']")
	WebElement Signin;
	
	
	public void setusername(String admin)
	{
		Username.sendKeys(admin);
	}
	
	public void setpassword(String logixerp)
	{
		Password.sendKeys(logixerp);
	}
	
	public void clicksignin()
	{
		Signin.click();
	}
	

}
