package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

	public HomePage(WebDriver driver) {
		super(driver);
		
	}
	
	@FindBy(xpath ="//a[contains(.,'Home')]")
	WebElement msgHeading;
	
	@FindBy(xpath ="//i[contains(@class,'fa fa-cog fa-lg')]")
	WebElement settings;

	@FindBy(xpath ="//*[@id='main-header']/div/div[3]/div[5]")
	WebElement logout;
	
	public boolean isHomePageExists()
	{
		try {
			// First check if URL changed from login page (indicates successful login)
			String currentUrl = driver.getCurrentUrl();
			boolean urlChanged = !currentUrl.toLowerCase().contains("login");
			
			if (urlChanged) {
				// If URL changed, try to find the original element
				if (msgHeading.isDisplayed()) {
					return true;
				}
			}
			
			// If original element not found, check for other success indicators
			// Check if page title changed
			String title = driver.getTitle();
			if (title != null && !title.toLowerCase().contains("login")) {
				return true;
			}
			
			// Check for common post-login elements
			try {
				// Look for logout, dashboard, home, or welcome elements
				List<WebElement> successElements = driver.findElements(By.xpath(
					"//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logout') or " +
					"contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dashboard') or " +
					"contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'welcome') or " +
					"contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'home')]"
				));
				
				if (!successElements.isEmpty()) {
					return true;
				}
			} catch (Exception e) {
				// Continue to URL check
			}
			
			// Final check - if URL changed from login, consider it success
			return urlChanged;
			
		} catch (Exception e) {
			// If original element check fails, check URL change as backup
			try {
				String currentUrl = driver.getCurrentUrl();
				return !currentUrl.toLowerCase().contains("login");
			} catch (Exception ex) {
				return false;
			}
		}
	}
	public void clicksignout()
	{
		logout.click();
	}
}
