package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageobjects.HomePage;
import pageobjects.ProductPage;
import pageobjects.SigninPage;
import testBase.BaseClass;

public class TC003_ProductCreationTest extends BaseClass {

    @Test(groups = {"Sanity","Regression"})
    public void verify_product_creation() {
        logger.info("***** Starting TC003_ProductCreationTest *****");
        try {
            // Ensure at application URL
            logger.info("Navigating to application URL...");
            driver.get(p.getProperty("appURL"));

            // Signin
            SigninPage sp = new SigninPage(driver);
            String username = p.getProperty("username");
            String password = p.getProperty("password");
            logger.info("Signing in as " + username);
            sp.setusername(username);
            sp.setpassword(password);
            sp.clicksignin();

            // Wait briefly for login to complete
            Thread.sleep(3000);

            HomePage hp = new HomePage(driver);
            Assert.assertTrue(hp.isHomePageExists(), "Login failed - home page does not exist");

            // Navigate to Products and create a new product
            ProductPage pp = new ProductPage(driver);
            pp.navigateToProducts();
            Thread.sleep(1000);
            pp.clickAddProduct();

            // Create product with random values
            String productName = "AutoProd_" + randomeString();
            String sku = "SKU_" + randomeNumber();
            String price = "10"; // minimal price for test

            logger.info("Creating product: " + productName + ", sku: " + sku);
            pp.createProduct(productName, sku, price);

            // Wait for creation to take effect
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("Product created: " + created);
            Assert.assertTrue(created, "Product was not found after creation");

        } catch (Exception e) {
            logger.error("Exception in TC003_ProductCreationTest: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
        logger.info("***** Finished TC003_ProductCreationTest *****");
    }

}
