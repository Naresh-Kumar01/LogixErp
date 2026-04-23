package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageobjects.HomePage;
import pageobjects.ProductCreationPage;
import pageobjects.SigninPage;
import testBase.BaseClass;

/**
 * Test Suite: Product Creation Scenarios
 *
 * Covers TC_001 – TC_010 for various product configurations:
 * FMFO, FEFO, Batch, ASN, FIFO, Skip Putaway, Skip Pickup,
 * Serial, Non-Serial, and a combined scenario.
 */
public class TC004_ProductCreationScenariosTest extends BaseClass {

    // ─── Helper: sign in and return a ready ProductCreationPage ──────────────

    private ProductCreationPage loginAndOpenNewProduct() throws Exception {
        driver.get(p.getProperty("appURL"));

        SigninPage sp = new SigninPage(driver);
        sp.setusername(p.getProperty("username"));
        sp.setpassword(p.getProperty("password"));
        sp.clicksignin();
        Thread.sleep(3000);

        HomePage hp = new HomePage(driver);
        Assert.assertTrue(hp.isHomePageExists(), "Login failed – home page not visible");

        ProductCreationPage pp = new ProductCreationPage(driver);
        pp.navigateToProducts();
        Thread.sleep(1000);
        pp.clickAddProduct();
        Thread.sleep(1000);
        return pp;
    }

    // ─── TC_001: FMFO Product Creation ───────────────────────────────────────

    /**
     * TC_001 – FMFO Product Creation
     * Steps : Login → Product Creation → Enter 'Biscuits' → Enable Mfg Date → Select FMFO → Save
     * Expected: Product created; Picking based on oldest manufacturing date
     */
    @Test(priority = 1, groups = {"Sanity", "Regression"},
          description = "TC_001: FMFO product – enable Mfg Date, select FMFO picking strategy")
    public void TC_001_FMFOProductCreation() throws Exception {
        logger.info("***** TC_001: FMFO Product Creation *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Biscuits_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("mfg date");          // Enable Manufacturing Date
            pp.selectPickingStrategy("FMFO");      // First Manufactured First Out

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_001 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_001 FAILED: FMFO product 'Biscuits' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_001 Exception: " + e.getMessage());
            Assert.fail("TC_001 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_001 PASSED *****");
    }

    // ─── TC_002: FEFO Product Creation ───────────────────────────────────────

    /**
     * TC_002 – FEFO Product Creation
     * Steps : Enter 'Medicine' → Enable Batch + Expiry → Select FEFO → Save
     * Expected: Product created; Expiry mandatory; Picking based on earliest expiry
     */
    @Test(priority = 2, groups = {"Sanity", "Regression"},
          description = "TC_002: FEFO product – enable Batch and Expiry, select FEFO picking strategy")
    public void TC_002_FEFOProductCreation() throws Exception {
        logger.info("***** TC_002: FEFO Product Creation *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Medicine_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("batch");              // Enable Batch tracking
            pp.enableToggle("expiry");             // Enable Expiry Date (mandatory)
            pp.selectPickingStrategy("FEFO");      // First Expired First Out

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_002 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_002 FAILED: FEFO product 'Medicine' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_002 Exception: " + e.getMessage());
            Assert.fail("TC_002 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_002 PASSED *****");
    }

    // ─── TC_003: Batch Product ────────────────────────────────────────────────

    /**
     * TC_003 – Batch Product
     * Steps : Enter 'Paint' → Enable Batch → Save
     * Expected: Product created; Batch selection available during picking
     */
    @Test(priority = 3, groups = {"Sanity", "Regression"},
          description = "TC_003: Batch product – enable Batch tracking only")
    public void TC_003_BatchProduct() throws Exception {
        logger.info("***** TC_003: Batch Product *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Paint_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("batch");              // Enable Batch

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_003 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_003 FAILED: Batch product 'Paint' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_003 Exception: " + e.getMessage());
            Assert.fail("TC_003 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_003 PASSED *****");
    }

    // ─── TC_004: ASN Product ──────────────────────────────────────────────────

    /**
     * TC_004 – ASN Product
     * Steps : Enter product → Enable ASN → Save
     * Expected: Product created; ASN-based receiving/picking allowed
     */
    @Test(priority = 4, groups = {"Sanity", "Regression"},
          description = "TC_004: ASN product – enable ASN flag")
    public void TC_004_ASNProduct() throws Exception {
        logger.info("***** TC_004: ASN Product *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "ASNProd_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("asn");                // Enable ASN

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_004 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_004 FAILED: ASN product was not created successfully");

        } catch (Exception e) {
            logger.error("TC_004 Exception: " + e.getMessage());
            Assert.fail("TC_004 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_004 PASSED *****");
    }

    // ─── TC_005: FIFO Product ─────────────────────────────────────────────────

    /**
     * TC_005 – FIFO Product
     * Steps : Enter 'Milk' → Select FIFO → Save
     * Expected: Product created; Old stock picked first
     */
    @Test(priority = 5, groups = {"Sanity", "Regression"},
          description = "TC_005: FIFO product – select FIFO picking strategy")
    public void TC_005_FIFOProduct() throws Exception {
        logger.info("***** TC_005: FIFO Product *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Milk_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.selectPickingStrategy("FIFO");      // First In First Out

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_005 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_005 FAILED: FIFO product 'Milk' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_005 Exception: " + e.getMessage());
            Assert.fail("TC_005 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_005 PASSED *****");
    }

    // ─── TC_006: Skip Auto Putaway ────────────────────────────────────────────

    /**
     * TC_006 – Skip Auto Putaway
     * Steps : Enter product → Enable Skip Putaway → Save
     * Expected: Product created; No auto location assignment
     */
    @Test(priority = 6, groups = {"Sanity", "Regression"},
          description = "TC_006: Skip Auto Putaway – no automatic location assignment on receipt")
    public void TC_006_SkipAutoPutaway() throws Exception {
        logger.info("***** TC_006: Skip Auto Putaway *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "SkipPutaway_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("skip");               // Enable Skip Auto Putaway
            // Also try more specific keyword in case both skip toggles exist
            pp.enableToggle("putaway");

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_006 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_006 FAILED: Skip Auto Putaway product was not created successfully");

        } catch (Exception e) {
            logger.error("TC_006 Exception: " + e.getMessage());
            Assert.fail("TC_006 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_006 PASSED *****");
    }

    // ─── TC_007: Skip Auto Pickup ─────────────────────────────────────────────

    /**
     * TC_007 – Skip Auto Pickup
     * Steps : Enter product → Enable Skip Pickup → Save
     * Expected: Product created; Manual picking location required
     */
    @Test(priority = 7, groups = {"Sanity", "Regression"},
          description = "TC_007: Skip Auto Pickup – manual picking location required")
    public void TC_007_SkipAutoPickup() throws Exception {
        logger.info("***** TC_007: Skip Auto Pickup *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "SkipPickup_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("pickup");             // Enable Skip Auto Pickup

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_007 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_007 FAILED: Skip Auto Pickup product was not created successfully");

        } catch (Exception e) {
            logger.error("TC_007 Exception: " + e.getMessage());
            Assert.fail("TC_007 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_007 PASSED *****");
    }

    // ─── TC_008: Serial Product ───────────────────────────────────────────────

    /**
     * TC_008 – Serial Product
     * Steps : Enter 'Laptop' → Enable Serial → Save
     * Expected: Product created; Serial number mandatory during transactions
     */
    @Test(priority = 8, groups = {"Sanity", "Regression"},
          description = "TC_008: Serial product – serial number mandatory during transactions")
    public void TC_008_SerialProduct() throws Exception {
        logger.info("***** TC_008: Serial Product *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Laptop_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("serial");             // Enable Serial tracking

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_008 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_008 FAILED: Serial product 'Laptop' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_008 Exception: " + e.getMessage());
            Assert.fail("TC_008 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_008 PASSED *****");
    }

    // ─── TC_009: Non-Serial Product ───────────────────────────────────────────

    /**
     * TC_009 – Non-Serial Product
     * Steps : Enter 'Pen' → Disable Serial → Save
     * Expected: Product created; Quantity tracking only
     */
    @Test(priority = 9, groups = {"Sanity", "Regression"},
          description = "TC_009: Non-Serial product – serial disabled, quantity tracking only")
    public void TC_009_NonSerialProduct() throws Exception {
        logger.info("***** TC_009: Non-Serial Product *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "Pen_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.disableToggle("serial");            // Ensure Serial is OFF

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_009 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_009 FAILED: Non-Serial product 'Pen' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_009 Exception: " + e.getMessage());
            Assert.fail("TC_009 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_009 PASSED *****");
    }

    // ─── TC_010: Combined Scenario ────────────────────────────────────────────

    /**
     * TC_010 – Combined Scenario
     * Steps : Enter 'Medicine' → Enable Batch + Expiry → FEFO → Skip Putaway → Save
     * Expected: Product created; Expiry-based picking; No auto location assignment
     */
    @Test(priority = 10, groups = {"Regression"},
          description = "TC_010: Combined – Batch + Expiry + FEFO + Skip Putaway")
    public void TC_010_CombinedScenario() throws Exception {
        logger.info("***** TC_010: Combined Scenario *****");
        try {
            ProductCreationPage pp = loginAndOpenNewProduct();

            String productName = "MedicineCombined_" + randomeString();
            pp.enterProductName(productName);
            pp.enterSku("SKU_" + randomeNumber());

            pp.enableToggle("batch");              // Enable Batch
            pp.enableToggle("expiry");             // Enable Expiry Date
            pp.selectPickingStrategy("FEFO");      // First Expired First Out
            pp.enableToggle("putaway");            // Skip Auto Putaway

            pp.clickSave();
            Thread.sleep(3000);

            boolean created = pp.isProductCreated(productName);
            logger.info("TC_010 – Product created: " + created);
            Assert.assertTrue(created,
                "TC_010 FAILED: Combined scenario product 'Medicine' was not created successfully");

        } catch (Exception e) {
            logger.error("TC_010 Exception: " + e.getMessage());
            Assert.fail("TC_010 failed due to exception: " + e.getMessage());
        }
        logger.info("***** TC_010 PASSED *****");
    }
}
