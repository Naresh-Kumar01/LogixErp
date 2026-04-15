package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageobjects.HomePage;
import pageobjects.SigninPage;
import testBase.BaseClass;

public class TC001_SigninTest extends BaseClass {
	@Test(groups= {"Sanity","Master"})
	public void verify_signin()
	{
		logger.info("******** Starting TC_001_SigninTest *****");
		try {
			logger.info("Navigating to application URL...");
			driver.get(p.getProperty("appURL"));
			logger.info("Current URL: " + driver.getCurrentUrl());
			
			logger.info("Creating SigninPage object...");
			SigninPage sp=new SigninPage(driver);
			
			// Get credentials from config.properties
			String username = p.getProperty("username");
			String password = p.getProperty("password");
			logger.info("Using username: " + username);
			
			logger.info("Setting username...");
			sp.setusername(username);
			logger.info("Setting password...");
			sp.setpassword(password);
			logger.info("Clicking signin...");
			sp.clicksignin();
			
			// Wait a bit for page to load after login
			Thread.sleep(3000);
			logger.info("Current URL after login: " + driver.getCurrentUrl());
			logger.info("Page title after login: " + driver.getTitle());
			
			logger.info("Creating HomePage object...");
			HomePage hp=new HomePage(driver);
			logger.info("Checking if home page exists...");
		boolean	targetpage=hp.isHomePageExists();
		logger.info("Home page exists: " + targetpage);
		Assert.assertTrue(targetpage, "Home page verification failed - expected element not found");
		} catch (Exception e) {
			logger.error("Exception occurred: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Test failed due to exception: " + e.getMessage());
		}
		logger.info("****** Finished TC_001_SigninTest *****");
	}

}